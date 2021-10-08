package tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import components.PhanTu;
import components.UtilityList;

/**
 * Class xây dựng những chiến thuật tỉa của thuật toán: Tỉa TWU,...
 * @author HaVy
 *
 */
public class CacChienThuatTia {
	
	private int huiCount = 0;

	/**
	 * Chiến thuật tỉa thứ nhất, áp dụng sau lần quét dữ liệu và tính TWU của mỗi item
	 * nếu TWU(item) < minutil thì loại. Đây là bước định nghĩa các List<UL> cũng như 
	 * gán UL cho mỗi item.
	 * @param _minutil minutil ngưỡng lợi nhuận thấp nhất mà ng dùng đề ra 
	 * @param utilityLists danh sách chứa các UL của item
	 * @param mapItemToTWU map chứa các entrySet <item, TWU>
	 * @param mapUtilityListOfItem map chứa các entrySet <item, UL>
	 */
	public void twuPurningStrategy(float _minutil, List<UtilityList> utilityLists,  Map<String, Integer> mapItemToTWU, Map<String, UtilityList> mapUtilityListOfItem) {
		//kiểm tra valid cho mỗi parameter truyền vào
		if(_minutil <= 0 || mapItemToTWU.isEmpty())
			return;
		//nếu hợp lệ thì bắt đầu thực hiện tỉa
		else {
			mapItemToTWU.entrySet().forEach(item -> {					
				//Duyệt từng item có trong map tính TWU và tạo UL cho item đó.
				// Mục đích để giảm thiểu vòng lập
				if(item.getValue() >= _minutil) {			//Nếu mà TWU(item) >= minutil
					UtilityList utilityList = new UtilityList(item.getKey());
					//tạo một UL mới cho item đó
					mapUtilityListOfItem.put(item.getKey(), utilityList);
					//mapping UL với item.
					utilityLists.add(utilityList);
					//vì thỏa điều kiện nên thêm vào list UL để bắt đầu sort
				}
			});
			//kết thúc lặp
			
			//bắt đầu sort
			Collections.sort(utilityLists, new Comparator<UtilityList>() {
				@Override
				public int compare(UtilityList o1, UtilityList o2) {
					// TODO Auto-generated method stub
					return soSanhTWUItems(o1.item, o2.item, mapItemToTWU);
				}
			});
			//kết thúc sort
			
			//print
			utilityLists.forEach(ul -> {
				System.out.print(ul.item + "  ");
			});
		}
	}
	

//	Input: P.UL, the utility-list of itemset P, initially
//	empty;
//	ULs, the set of utility-lists of all P’s
//	1-extensions;
//	minutil, the minimum utility threshold.
//	Output: all the high utility itemsets with P as prefix.
//	1 foreach utility-list X in ULs do
//	2 if SUM(X.iutils)≥minutil then
//	3 output the extension associated with X;
//	4 end
//	5 if SUM(X.iutils)+SUM(X.rutils)≥minutil then
//	6 exULs = NULL;
//	7 foreach utility-list Y after X in ULs do
//	8 exULs = exULs+Construct(P.UL, X, Y );
//	9 end
//	10 HUI-Miner(X, exULs, minutil);
//	11 end
//	12 end
	public void HUI_MinerAlgorithm (String[] prefix, int prefixCount, BufferedWriter writer,
			List<UtilityList> ULs, UtilityList P, float minutil) throws IOException {
		
		//1. Duyệt qua từng ul trong ULs
//		for (UtilityList X : ULs) {
		for(int i = 0; i< ULs.size(); i++) {
			UtilityList X = ULs.get(i);
			//2. Kiểm tra u(X) >= minutil
			if(X.sumIutil >= minutil)
			{
				//những itemset thỏa mãn iutil trong các trans >= min
				System.out.print(X.item + "  ");
				X.inDanhSachPhanTu();
				printItemset(prefix, prefixCount, X.item, X.sumIutil, huiCount, writer);
			}
			//5. Đối với các phần tử ko thỏa mãn thì bắt đầy xét cột 2 và 3
			// để ghép cặp.
//			else if((X.sumIutil + X.sumRutil) >= minutil ){
			if((X.sumIutil + X.sumRutil) >= minutil ){
				//6. Tao exULs = null
				List<UtilityList> exULs = new ArrayList<UtilityList>();
				
				//7.Duyet qua tung ul Y dung sau X co trong ULs.
				System.out.println("Xét item: " + X.item);
				for(int j = i+1; j < ULs.size(); j ++) {
					// 8. exULs = exULs+Construct(P.UL, X, Y );
					UtilityList Y = ULs.get(j);
					//9. 
					exULs.add(Construct(P, X, Y));
				}
//				System.out.println("P length = " + P.danhSachPhanTu.size());
				System.out.println("P length = " + exULs.size());
				
//				9 end
//				10 HUI-Miner(X, exULs, minutil);
				prefix[prefixCount] = X.item; // gan prefix dau tien la item hien tai
				HUI_MinerAlgorithm(prefix, prefixCount + 1, writer, exULs,X,minutil);
			}
		}
	}
	
	
private void printItemset(String[] prefix, int prefixCount, String item, double sumIutil, int huiCount, BufferedWriter writer) throws IOException {
		// TODO Auto-generated method stub
		huiCount ++;
		
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i< prefixCount; i++)
		{
			builder.append(prefix[i]);
			builder.append(", ");
		}
		//bat dau append ptu sau prefix
		builder.append(item);
		builder.append(" : ");
		builder.append(sumIutil);
		writer.write(builder.toString());
		writer.newLine();
	}


	//	Input: P.UL, the utility-list of itemset P;
//	Px.UL, the utility-list of itemset Px;
//	Py.UL, the utility-list of itemset Py.
//	Output: Pxy.UL, the utility-list of itemset Pxy.
//	1 Pxy.UL = NULL;
//	2 foreach element Ex ∈ Px.UL do
//	3 if ∃Ey∈Py.UL and Ex.tid==Ey.tid then
//	4 if P.UL is not empty then
//	5 search such element E∈P.UL that
//	E.tid==Ex.tid;
//	6 Exy=<Ex.tid, Ex.iutil+Ey.iutil-E.iutil,
//	Ey.rutil>;
//	7 else
//	8 Exy=<Ex.tid, Ex.iutil+Ey.iutil, Ey.rutil>;
//	9 end
//	10 append Exy to Pxy.UL;
//	11 end
//	12 end
//	13 return Pxy.UL;
	private UtilityList Construct(UtilityList P, UtilityList xUL, UtilityList yUL) {
		// TODO Auto-generated method stub
		
		//1. Tao mot UL moi de nhan ul cua itemset xy sau khi ghep cap
		// chứa các x-extension
		UtilityList Pxy = new UtilityList(yUL.item);
		
		//2. duyet tung PhanTu Ex co trong xUL : <tid, iutil, rutil>
		List<PhanTu> xElementsInUL =  xUL.danhSachPhanTu;
		
		System.out.println("--------------------------");
		System.out.println("Tid trùng: ");
//		for(int i = 0; i< xElementsInUL.size(); i ++)
		for(PhanTu Ex : xElementsInUL)
		{
//			PhanTu Ex = xElementsInUL.get(i);
			
			//Tim kiem cac phan tu trong yUL o cung giao dich voi Ex
//			PhanTu Ey = new TimKiem().jumpSearchIterative(xElementsInUL, Ex.tid);
			PhanTu Ey = new TimKiem().jumpSearchIterative(yUL.danhSachPhanTu, Ex.tid);
			if(Ey == null)
				continue;
				//có giao dịch trùng nên bắt đầu gép cặp
//				System.out.println(yUL.item + ": <"+ Ey.tid +"," + Ey.iutil + "," + Ey.rutil + ">  ");
				
			if(P == null) {
				//P rỗng => P.UL rỗng xảy ra khi k< 3. Chạy xong lớp 2 thì k rỗng.
				//và ul của k-itemset (k≥3) bắt đầu xây dựng khi 
				//P.UL không rỗng.
				
//					5. search such element E∈P.UL that E.tid==Ex.tid;
				//Kiểm tra giao dịch trùng
				PhanTu Exy = new PhanTu(Ex.tid, Ex.iutil + Ey.iutil, Ey.rutil);
//					Pxy.danhSachPhanTu.add(Exy);
				Pxy.themPhanTu(Exy);
				System.out.println(yUL.item + ": <"+ Exy.tid +"," + Exy.iutil + "," + Exy.rutil + ">  ");
			
			}else{
				// Nếu P.UL != rỗng => Đang xét k >= 3 như trong thuật toán.
//						6 Exy=<Ex.tid, Ex.iutil+Ey.iutil-E.iutil,
//						Ey.rutil>;

				PhanTu Ep = new TimKiem().jumpSearchIterative(P.danhSachPhanTu, Ex.tid);
				if(Ep != null){
				// nếu tồn tại giao dịch trùng.	
					PhanTu Exy = new PhanTu(Ex.tid, Ex.iutil + Ey.iutil - Ep.iutil, Ey.rutil);
//						Pxy.danhSachPhanTu.add(Exy);
					Pxy.themPhanTu(Exy);
					System.out.println(yUL.item + ": <"+ Exy.tid +"," + Exy.iutil + "," + Exy.rutil + ">  ");
				}
				}
//					9 end
//					10 append Exy to Pxy.UL;	
			}
		return Pxy;
	}
	

	/**
	 * So sánh hai TWU của các item. Dựa vào các map<item - twu>
	 * trả về < 0 (bé hơn), = 0 (bằng) và > 0 (lớn hơn).
	 * @param item1
	 * @param item2
	 * @param _mapInput 
	 * @return 
	 */
	private int soSanhTWUItems(String item1, String item2, Map<String, Integer> _mapInput) {
		// TODO Auto-generated method stub
		int rs = _mapInput.get(item1) - _mapInput.get(item2);
		//Nếu trường hợp bằng thì sao ???
		return (rs == 0)? 0 : rs;
	}
	
}
