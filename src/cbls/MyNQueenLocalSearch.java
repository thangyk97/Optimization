package cbls;

public class MyNQueenLocalSearch {
	private int[] x;
	private int N;
	private boolean found;
	
	private boolean isValid(int v) {
		for(int i=1; i< v; i++) {
			if(v != i)
				if(x[v] == x[i] ||
					x[v] - v == x[i] - i ||
					x[v] + v == x[i] + i) {
					return false;
				}
		}
		return true;
	}
	
	private void solution() {
		for(int i = 1; i <= N; i++) {
			System.out.print(x[i] + ", ");
		}
	}
	
	private void TRY(int k) {
		if(found) return;
		for(int i = 1; i <= N; i++) {
			if(found) break;
			x[k] = i;
			if(isValid(k)) {
				if(k == N) {
					solution();
					this.found = true;
					return;
				} else {
					TRY(k+1);
				}
			}
		}
	}
	
	public void solve(int N) {
		this.N = N;
		this.found = false;
		this.x = new int[N+1];
		TRY(1);
	}
	
	public static void main(String[] args) {
		MyNQueenLocalSearch s = new MyNQueenLocalSearch();
		s.solve(10);
	}
}
