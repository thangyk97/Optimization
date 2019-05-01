package tsp;

import java.util.ArrayList;
import java.util.Collections;

public class VarTSP {
	public ArrayList<Integer> route;
	private int length;
	public float[][] D;
	
	
	public VarTSP(int length) {
		this.length = length;
		this.route = new ArrayList<Integer>();
		for (int i = 1; i <= this.length; i ++) {
			this.route.add(i);
		}
	}
	
	public void onePointMove(int x, int y) {
		this.route.remove(new Integer(x));
		int _index = this.route.indexOf(new Integer(y));
		this.route.add(_index + 1, x);
	}
	
	public void towOptMove(int x, int y) {
		int _indexX = this.route.indexOf(new Integer(x));
		int _indexY = this.route.indexOf(new Integer(y));
		
		for (int i = 0; i < (_indexY - _indexX) / 2; i ++) {
			Collections.swap(this.route, _indexX + 1 + i, _indexY - i);
		}
	}
	
	public void swapTwoPoint(int i, int j) {
		Collections.swap(this.route, i, j);
	}
}
