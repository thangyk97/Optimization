package cbls;

public class NQueenBackTrack {
	private int n;
	private boolean found;
	private int[] x;
	
	public boolean check(int v, int k) {
		for(int j=1; j <= k-1; j++) {
			if(x[j] == v) return  false;
			if(x[j] + j == v + k) return false;
			if (x[j] - j == v - k) return false;
		}
		return true;
	}
	
	public void TRY(int k) {
		if(found) return;
		
		for(int v = 1; v <= n; v++) {
			if(check(v, k)) {
				x[k] = v;
				if(k==n) solution();
				else {
					TRY(k+1);
				}
			}
		}
	}
	
	public void solve(int n) {
		this.n = n;
		x = new int[n+1];
		found = false;
		TRY(1);
	}
	
	public void solution() {
		found = true;
		for(int i = 1; i<=n; i++) System.out.println(x[i] + " ");
		
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		NQueenBackTrack s = new NQueenBackTrack();
		s.solve(29);

	}

}
