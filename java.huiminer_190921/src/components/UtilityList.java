package components;

import java.util.ArrayList;
import java.util.List;

/**
 * Mỗi dòng trong danh sách là một PhanTu. UtilityList gắn với mỗi item
 * Có Sum iutil để tính tổng các Utility trong danh sách 
 * và Sum rutil để tính tổng các ru còn lại trong danh sách.
 * @author HP
 *
 */
public class UtilityList {
	// item gắn với UL
	public String item;
	// Sum của các iutil
	public double sumIutil = 0;
	public double sumRutil = 0;// Sum của các rutil
	public List<PhanTu> danhSachPhanTu = new ArrayList<PhanTu>();// Danh sách các phần tử có trong list
	
	
	/**
	 * Constructor chỉ nhận một param là item tương ứng với UL
	 * các param còn lại sẽ được tính toán trong lớp này. 
	 * @param item
	 */
	public UtilityList(String item) {
		this.item = item;
	}
	
	/**
	 * Thêm một phần tử mới (Transation) vào UL của item đó
	 * @param phanTu
	 * @return
	 */
	public Boolean themPhanTu(PhanTu phanTu)
	{
		
		try {
			danhSachPhanTu.add(phanTu);
			sumIutil += phanTu.iutil;
			sumRutil += phanTu.rutil;
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	/**
	 * In danh sách phần tử 
	 * @return
	 */
	public void inDanhSachPhanTu()
	{
		this.danhSachPhanTu.forEach(pt ->{
			System.out.print("<"+pt.tid+","+pt.iutil+","+pt.rutil+">  ");
		});
	}
	
}
