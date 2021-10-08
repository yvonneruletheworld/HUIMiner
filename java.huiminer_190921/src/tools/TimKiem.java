package tools;

import java.util.List;

import components.PhanTu;

/**
 * Class chua cac thuat toan tim kiem
 * @author HaVy
 *
 */
public class TimKiem 
{
	
	/**
	 * Thuat toan tim kiem Buoc nhay trong List 
	 * @param pts List <PhanTu> chua cac phantu trong UL
	 * @param tid TID target de tim kiem
	 * @return phan tu co tid trung | null neu ko co phan tu trung.
	 */
	public PhanTu jumpSearchIterative(List<PhanTu> pts, int tid)
	{
		int blockSize = (int) Math.sqrt(pts.size());
		int start = 0;
		int next = blockSize;
		
		while( start < pts.size() && tid > pts.get(next-1).tid ) 
		{
			start = next;
			next = next + blockSize;
			
			if ( next >= pts.size() )
				next = pts.size();
		}
		
		for( int i=start; i<next; i++ ) 
		{
			if ( tid == pts.get(i).tid)
				return pts.get(i);
		}
		
		return null;
	}
}
