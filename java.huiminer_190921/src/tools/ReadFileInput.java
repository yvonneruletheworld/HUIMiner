package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import components.Item;
import components.PhanTu;
import components.UtilityList;

/**
 * Class xây dựng các chức năng đọc file và khởi tạo cấu trúc database ban đầu.
 * @author HaVy
 *
 */
public class ReadFileInput {
	
	public BufferedReader bufferedReader;
	public String currentLine;
	
	
	/**
	 * Doc file text và tinh TWU cua moi item
	 * @param fileName ten file hoac duong dan den file text 
	 * @param mapOutput tra ve ket qua la mot map<item,twu> 
	 * @param minutil 
	 * @return 
	 * @throws IOException 
	 */
	public float ReadAndTWU(String fileName, HashMap<String,Integer> mapOutput, float minutil) throws IOException {
		if (fileName.isBlank() && fileName.isEmpty())
			// neu ten file null thi tra ve
			return -1;
		//neu khong thi bat dau doc file
		else {
			int TU = 0; // tao bien de tinh % min 
			try {
				//tao ra bien doc du lieu tu file
				bufferedReader = new BufferedReader(
						 new InputStreamReader(
						 new FileInputStream(
						 new File(fileName))));
				//tao bien nhan gia tri moi dong
				//Duyet tung transaction cho den khi doc het file text 
				while ((currentLine = bufferedReader.readLine()) != null) {
					//neu dong du lieu null va chua ky tu ma hoa thì bo qua
					if(currentLine.isBlank() == true || currentLine.isEmpty() == true){
						continue;
					}
					// Phan nho cac gia tri tu Transaction vua doc duoc.
					String [] transValue = currentLine.split(":");
					// 0: cac item co trong transaction
					// 1: TU cua moi transaction
					// 2: utility cua cac item tuong ung trong transaction do
					String items[] = transValue[0].split(" ");
					String[] utilities = transValue[2].split(" ");
					
					// Cach 1 tính TU
					int transUtility = Integer.parseInt(transValue[1].trim());
					
					//Cach 2
					// parse String array sang Integer Array
					//int [] utilitiesNum = Arrays.stream(utilities).mapToInt(Integer :: parseInt).toArray();
					// Tính TU
					TU+= transUtility;
					//int transUtility = IntStream.of(utilitiesNum).sum();
					
					//duyet qua tat ca cac item có trong transaction
					for(int i = 0; i< items.length; i++)
					{
						tinhTWU(items[i], transUtility, mapOutput);
					}
				}
				
				//Tinh lai min theo % 
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally {
				if(bufferedReader != null)
					bufferedReader.close();
					// dong lai
			}
			return  TU * (minutil / 100);
		}
	}

	/**
	 * 
	 * Đọc lại database một lần nữa để hiệu chỉ thứ tự các item. Ví dụ database mới là một mảng 2 chiều
	 * thì mỗi row là một Transaction, các cell ứng với từng row là cặp <item, util> được sắp xếp tăng dần theo TWU
	 * và tạo các itemset 1-item.
	 * @param fileName đường dẫn đến file txt.
	 * @throws IOException
	 */
	public void reWriteDatabase (String fileName, float minutil, Map<String,Integer> mapOutput, Map<String,UtilityList> map, List<UtilityList> uls) throws IOException
	{
		//kiem tra rỗng
			try {
				bufferedReader = new BufferedReader(
						 new InputStreamReader(
						 new FileInputStream(
						 new File(fileName))));
				System.out.println();
				System.out.println("Database sau khi tạo lại và sắp xếp là: ");
				
				//đếm trans
				int tid = 1;
				while((currentLine = bufferedReader.readLine()) != null) {
					///bd while duyệt từng trans <item, TU, u>
					
					//validate cac dong du lieu
					if(currentLine.isBlank() == true || currentLine.isEmpty() == true) {
						continue;			//nếu k hợp lệ thì bỏ qua
					}
					
					///bắt đầu đọc dữ liệu
					
					String [] transValue = currentLine.split(":");
					// 0: cac item co trong transaction
					// 1: TU cua moi transaction
					// 2: utility cua cac item tuong ung trong transaction do
					String items[] = transValue[0].split(" ");
					String[] utilities = transValue[2].split(" ");
					
					///Ghi lại những item có TWU >= minutil vào trong database mới
					
					List<Item> oneRowItemInNewDB =  new ArrayList<Item>();
					//Danh sách tượng trưng cho một hàng ở Database mới
					
					//Tinh ru của mỗi item (1-item) được tạo mới mỗi khi duyệt xong 
					// một trans
					int  ruItem = 0;
					
					for(int i = 0; i< items.length; i++)
					{
						//đọc và gán u cho item.
						Item item = new Item();
						item.item = items[i];
						item.utility = Integer.parseInt(utilities[i]);
						if(mapOutput.get(item.item) >= minutil)
						{
							oneRowItemInNewDB.add(item);
							ruItem += item.utility;
							// ru = tu - u;
						}
					}
//					
//					System.out.println("Database chưa sắp xếp");
//					oneRowItemInNewDB.forEach(item ->{
//						System.out.print(item.item + " " + item.utility + " | ");
//					});
//					
					
					/// Sort
					Collections.sort(oneRowItemInNewDB, new Comparator<Item>() {

						@Override
						public int compare(Item o1, Item o2) {
							// TODO Auto-generated method stub
							//so sánh TWU để sort
							int rs = mapOutput.get(o1.item) - mapOutput.get(o2.item);
							return (rs == 0) ? 0 : rs;
						}
					});
					System.out.println();
					
					oneRowItemInNewDB.forEach(item ->{
						System.out.print(item.item + " " + item.utility + " | ");
					});
					
					System.out.println();
					
					///tạo 1-item
					taoLop1Item(tid, oneRowItemInNewDB, map, ruItem,uls);
					
					//duyet sang trans khác
					tid++;
					
				}///end while
				
				System.out.println();
				map.entrySet().forEach(itemset -> {
					System.out.print(itemset.getKey()+ "   ");
					UtilityList list = itemset.getValue();
					list.inDanhSachPhanTu();
					System.out.println();
				});
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally {
				if(bufferedReader != null)
					bufferedReader.close();
			}
	}
	
	
    /**
     * Tạo một lớp itemset đầu tiên 1-item. 
     * Bằng cách duyệt các phần tử của list <item, util>
     * dựa vào hai tham số util và tid tính rutil
     * sau đó thêm vào UL của mỗi item
     * @param tid TID của mỗi Trans
     * @param items Danh sách <item, utility>
     * @param mapULOfItem map chứa item và UL của nó (rỗng khi truyền vào)
     * @param ru ru của trans đó
     * @param uls 
     */
    private void taoLop1Item(int tid, List<Item> items, Map<String, UtilityList> mapULOfItem, int ru, List<UtilityList> uls) {
		// TODO Auto-generated method stub
    	
    	///tạo một elemet item<tid,iutil, rutil>
    	// trong items các item sẽ có dạng Item(item, utility) của Trans tid
    	//
    	
    	for (Item item : items) {
    		///tính rutil 
    		ru = ru - item.utility;
    		
    		// Tạo một <tid, iutil, rutil>
    		PhanTu phanTu = new PhanTu(tid, item.utility, ru);
    		
    		//Tìm UL của item đó trong map 
    		// lúc này UL rỗng 
    		UtilityList ulItem = mapULOfItem.get(item.item);
    		
    		//thêm phần tử vào UL
    		ulItem.themPhanTu(phanTu);
		}
	}

	private void tinhTWU(String item, int transUntility, HashMap<String, Integer> mapOutput) {
    	// TODO Auto-generated method stub
    	
    	//kiem tra xem trong map co item do chua
    	Integer twu = mapOutput.get(item);
    	// neu chua co thi them vao con k thi cong don vao gia tri
    	twu = (twu == null) ? transUntility: transUntility + twu;
    	mapOutput.put(item, twu);
    }
}


