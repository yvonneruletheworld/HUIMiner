package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import components.UtilityList;
import tools.CacChienThuatTia;
import tools.ReadFileInput;

public class Control {

	//Khai báo
	//key: | value:
	Map<String, Integer> mapItemToTWU = null;
	List<UtilityList> utilityLists = null;					//Chứa các UL dùng để thực thi với TWU >= min
	Map<String, UtilityList> mapUtilityListOfItem = null;   // để chứa các cặp item - UL mà ko dùng để thực thi
	CacChienThuatTia cacChienThuatTia = new CacChienThuatTia();
	ReadFileInput input = new ReadFileInput();
	String fileName = "D:\\Applications\\eclipse-workspace\\java.huiminer_190921\\src\\tools\\contextHUIM.txt";
	float minutil;
	final int BUFFERS_SIZE = 200;
	private String [] itemSet = null;
	
	public Control(float minutil) {
		this.minutil = minutil;
	}
	
	//Hàm
	/**
	 * B1: Đọc và tính toán TWU của mỗi phần tử
	 * @throws IOException 
	 */
	
	
	public void Buoc1 () throws IOException
	{
		System.out.println("Mức lấy min = " + this.minutil + "%");
		System.out.println("Doc file va tinh TWU");
		
		mapItemToTWU = new HashMap<String, Integer>(); //Map de ghi TWU cua moi item
		float reMin = input.ReadAndTWU(fileName,(HashMap<String, Integer>)mapItemToTWU, minutil);
		
		mapItemToTWU.entrySet().forEach(entry -> { // print HashMap tính TWU
			System.out.println(entry.getKey() + "  " + entry.getValue());
		});
		
		System.out.println("Minutil = " + reMin);
		this.minutil = reMin;
		
	}
	
	

	/**
	 * Bước 2: Bắt đầu tạo các UL cho các Item sau đó lọc cái item có TWU < minutil
	 * và sắp xếp theo thứ tự tăng dần TWU
	 * @param minutil ngưỡng lợi nhuận thấp nhất mà ng dùng đề ra
	 */
	public void Buoc2 () 
	{
		System.out.println();
		System.out.println("Sắp xếp theo thứ tự tăng dần TWU");
		utilityLists = new ArrayList<UtilityList>();	//List để chứa các UL của các item.
														//sẽ được sort sau.
		mapUtilityListOfItem = new HashMap<String,UtilityList>();
		
		cacChienThuatTia.twuPurningStrategy
		(minutil, utilityLists, mapItemToTWU , mapUtilityListOfItem);
	}

	/**
	 * 
	 * Bắt đầu quét lần 2 database rồi viết lại database theo cấu trúc Đọc lại database một lần nữa để hiệu chỉ thứ tự các item. Ví dụ database mới là một mảng 2 chiều
	 * thì mỗi row là một Transaction, các cell ứng với từng row là cặp <item, util> được sắp xếp tăng dần theo TWU
	 * @throws IOException
	 */
	public void Buoc3() throws IOException {
		System.out.println();
		input.reWriteDatabase(fileName, minutil, mapItemToTWU,mapUtilityListOfItem, utilityLists);
	}
	
	public void Buoc4() throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("./output.txt"));
		itemSet = new String[BUFFERS_SIZE];
		System.out.println("Ket Qua");
		cacChienThuatTia.HUI_MinerAlgorithm(itemSet, 0,writer, utilityLists, null, minutil);
		// close output file
		writer.close();
	}
}
