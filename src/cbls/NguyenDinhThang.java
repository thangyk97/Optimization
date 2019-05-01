package cbls;

import java.util.ArrayList;
import java.util.Random;

import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.basic.FuncMult;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.functions.sum.Sum;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class NguyenDinhThang {
	
	LocalSearchManager mgr;
	VarIntLS[] x;
	ConstraintSystem S;
	
	int n; // so loai chat long
	int m; // so thung
	ArrayList<Integer>[] y; // danh sach khong duoc de gan nhau
	int[] v; // the tich cua thung
	int[] max_v;
	
	public void stateModel() {
		mgr = new LocalSearchManager();
		x = new VarIntLS[n];
		for (int i = 0; i < n; i++) {
			x[i] = new VarIntLS(mgr, 0, m-1);
		}

		S = new ConstraintSystem(mgr);
		
		// volume constraint
		for (int i = 0; i < m; i++) {
			S.post(new LessOrEqual(new ConditionalSum(x, v, i), max_v[i]));
		}
		
		// Rang buoc tap cac chat long ko duoc de cung nhau
		for (int i = 0; i < y.length; i++) {
			// Tao mang tam con
			VarIntLS[] temp = new VarIntLS[y[i].size()];
			for (int j = 0; j < temp.length; j++) {
				temp[j] = x[y[i].get(j)];
			}
			// add constraint
			// 
			Sum sumTemp = new Sum(temp);
			for (int j = 0; j < temp.length; j++) {
				S.post(new NotEqual(sumTemp, new FuncMult(temp[j], temp.length)));
			}
		}

		mgr.close();
	}
	
	public void hillClimbing(IConstraint S, int maxIter) {
		VarIntLS[] x = S.getVariables();
		ArrayList<AssignMove> candidates = new ArrayList<AssignMove>();
		Random R = new Random();
		
		int it = 0;
		while (it < maxIter && S.violations() > 0) {
			// step 1: collect candidates
			candidates.clear();
			int minDelta = Integer.MAX_VALUE;
			for (int i = 0; i < x.length; i++) {
				for(int v = x[i].getMinValue(); v <= x[i].getMaxValue(); v ++) {
					int d = S.getAssignDelta(x[i], v);
					if (d < minDelta) {
						candidates.clear();
						candidates.add(new AssignMove(i, v));
						minDelta = d;
					} else if (d == minDelta) {
						candidates.add(new AssignMove(i, v));
					}
				}
			}
			
			// step 2: random selection
			int idx = R.nextInt(candidates.size());
			AssignMove m = candidates.get(idx);
			x[m.i].setValuePropagate(m.v);
			
			// Log
			System.out.println("step " + it + ", violation = " + S.violations());
			it ++;
		}
	}
	
	public void search() {
		hillClimbing(S, 10000);
	}
	
	public void solve() {
		stateModel();
		search();
		System.out.println();
		for (int i = 0; i < x.length; i++) {
			System.out.print(x[i].getValue() + " ");
		}
		
		System.out.println();
		for (int i = 0; i < 6; i++) {
			System.out.println("thung " + i);
			for (int j = 0; j < 20; j ++) {
				
				if (x[j].getValue() == i) {
					System.out.println(j);
				}
			}

		}
	}
	
	public NguyenDinhThang(int n, int m, ArrayList<Integer>[] y, int[] v, int[] max_v) {
		this.n = n;
		this.m = m;
		this.y = y;
		this.v = v;
		this.max_v = max_v;
	}

	public static void main(String[] args) {
		int[] v = {20,15,10,20,20,25,30,15,10,10,20,25,20,10,30,40,25,35,10,10};
		int[] max_v = {60, 70, 90, 80, 100};
		
		ArrayList<Integer> [] y = new ArrayList[6];
        for (int i = 0; i < 6; i++) { 
            y[i] = new ArrayList<Integer>(); 
        } 
		y[0].add(0); y[0].add(1);
		y[1].add(7); y[1].add(8);
		y[2].add(12); y[2].add(17);
		y[3].add(8); y[3].add(9);
		y[4].add(1); y[4].add(2); y[4].add(9);
		y[5].add(0); y[5].add(9); y[5].add(12);
		
		
		NguyenDinhThang s = new NguyenDinhThang(20, 5, y, v, max_v);
		s.solve();
	}

}
