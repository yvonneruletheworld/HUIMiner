package components;

/**
 * Class định nghĩa mỗi phần tử trong Utility-List của item. Mỗi phần tử có các thuộc tính(tid,iutil,rutil)
 * Ví dụ: item {a} = <1,4,6> ; <4,21,14>
 * Item a xuất hiện ở Trans 4, Utility = 4, Remaining Utility = 6 
 * @author HaVy
 *
 */
public class PhanTu {
	// Định nghĩa ba thuộc tính của mỗi Phần tử
	//
	//ID của mỗi transaction
	public final int tid;
	//
	//Utility của item trong transaction đó
	public final int iutil;
	//
	//Remaining Utility còn lại của item 
	public int rutil;
	
	
	/**
	 * Định nghĩa constructor
	 * @param tid
	 * @param iutil
	 * @param rutil
	 */
	public PhanTu(int tid, int iutil, int rutil) {
		this.tid = tid;
		this.iutil = iutil;
		this.rutil = rutil;
	}
	
	
}
