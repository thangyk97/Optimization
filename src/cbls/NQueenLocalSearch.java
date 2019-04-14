package cbls;
import java.util.ArrayList;
import java.util.Random;

public class NQueenLocalSearch {
	
	private int[] x;
	private int n;
	
	public boolean isAttack(int v, int k) {
			if(x[v] == x[k]) return  true;
			if(x[v] + v == x[k] + k) return true;
			if(x[v] - v == x[k] - k) return true;
		return false;
	}
	
	public int selectMostViolationQueen() {
		int r=1;
		int[] temp = new int[n+1];
		int v=0;
		for (int i = 1; i<= n; i++) {
			v = 0;
			for (int j=1; j<=n; j++) {
				if(i!=j) {
					if(isAttack(i, j)) v++;
				}
			}
			temp[i] = v;
		}
		ArrayList<Integer> mostVioQ = new ArrayList<>();
		
		int max = 0;
		for(int i = 1; i<= n; i++) {
			max = max < temp[i]?temp[i]:max;
		}
		for(int i = 1; i<= n; i++) {
			if(temp[i] == max) mostVioQ.add(i);
		}
		
		Random rand = new Random();
		
		r = mostVioQ.get(rand.nextInt(mostVioQ.size()));
		
		return r;
	}
	
	public int selectRow(int sel_q) {
		ArrayList<Integer> cand = new ArrayList<Integer>();
		int minV = Integer.MAX_VALUE;
		for(int r = 1; r <= n; r++) if(r != x[sel_q]) {
			int old = x[sel_q];
			x[sel_q] = r;
			int v = violation();
			if(v < minV) {
				cand.clear(); cand.add(r); minV = v;
			} else if(v == minV) {
				cand.add(r);
			}
			x[sel_q] = old;
			
		}
		
		Random rand = new Random();
		return cand.get(rand.nextInt(cand.size()));
	}
	
	public int violation() {
		int v=0;
		for (int i = 1; i<= n; i++) {
			for (int j=i+1; j<=n; j++) {
				if(i!=j) {
					if(isAttack(i, j)) v++;
				}
			}
		}
		return v;
	}
	
		
	public void initSolution() {
		x = new int[n+1];
		Random rand = new Random();
		for(int i=0; i<=n; i++) {
			x[i] = rand.nextInt() % n + 1;
		}
	}
	
	public void localSearch(int maxIter) {
		for(int it = 1; it < maxIter; it ++) {
			int vio = violation();
			System.out.println("Step " + it + ", violations = " + vio);
			if (vio == 0) break;
			// Select queen attack most
			int sel_q = selectMostViolationQueen();
			//select row s.t violations reduces most
			int sel_r = selectRow(sel_q);
			
			x[sel_q] = sel_r;
			
		}
	}
	
	public void solve(int n) {
		this.n = n;
		initSolution();
		localSearch(100);
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NQueenLocalSearch s = new NQueenLocalSearch();
		s.solve(1000);
	}

}
