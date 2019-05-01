package cbls;

import java.util.ArrayList;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class ScheduleTeacher {
	
	LocalSearchManager mgr;
	VarIntLS[] x;
	ConstraintSystem cS;
	
	int[] soTietCuaMon;
	ArrayList<Integer>[] C;
	int[][] S;
	int alpha, beta;
	
	public ScheduleTeacher(int[] soTietCuaMon, ArrayList<Integer>[] C,
			int[][] S, int alpha, int beta) {
		this.soTietCuaMon = soTietCuaMon;
		this.C = C;
		this.S = S;
		this.alpha = alpha;
		this.beta = beta;
	}
	
	
	public void createModel() {
		this.mgr = new LocalSearchManager();
		
		// Initial Variable
		x = new VarIntLS[this.soTietCuaMon.length];
		for (VarIntLS xi : x) {
			xi = new VarIntLS(mgr, 1, C.length);
		}
		// Initial Constraint
		this.cS = new ConstraintSystem(mgr);
		for (int[] couple: S) {
			this.cS.post(new NotEqual(x[couple[0]], x[couple[1]]));
		}
		
		// So tin > alpha va < beta
		for (int i = 0; i < this.C.length; i ++) {
			IConstraint soTietMoreThanAlpha = new LessOrEqual(this.alpha,
								new ConditionalSum(this.x, this.soTietCuaMon, i));
			this.cS.post(soTietMoreThanAlpha);
			IConstraint soTietLessThanBeta = new LessOrEqual(
								new ConditionalSum(this.x, this.soTietCuaMon, i),
								this.beta);
			this.cS.post(soTietLessThanBeta);
		}
		
		
		
		
		
		
		
		this.mgr.close();
	}
	
	public void search() {
		
	}
	
	public void solve() {
		
	}
	
		

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ScheduleTeacher s = new ScheduleTeacher(null, null, null, 0, 1000);
		s.solve();
	}

}
