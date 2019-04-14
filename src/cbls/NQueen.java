package cbls;
import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;

public class NQueen {
	
	LocalSearchManager mgr;
	VarIntLS[] x;
	ConstraintSystem S;
	
	int n;
	
	public void stateModel() {
		mgr = new LocalSearchManager();
		x = new VarIntLS[n];
		for (int i = 0; i < n; i++) {
			x[i] = new VarIntLS(mgr, 0, n-1);
		}
		S = new ConstraintSystem(mgr);
		S.post(new AllDifferent(x));
		
		IFunction[] f1 = new IFunction[n];
		for (int i = 0; i < n; i++) {
			f1[i] = new FuncPlus(x[i], i);
		}
		S.post(new AllDifferent(f1));
		
		IFunction[] f2 = new IFunction[n];
		for (int i = 0; i < n; i++) {
			f2[i] = new FuncPlus(x[i], -i);
		}
		S.post(new AllDifferent(f2));
		mgr.close();
	}
	
	public void printSolution() {
		for(int i = 0; i < n; i++) {
			System.out.println(x[i].getValue() + " ");
		}
		System.out.println();
	}
	
	public void search() {
		printSolution();
		System.out.println("Init violations = " + S.violations());
		int it = 0;
		
		MinMaxSelector mms = new MinMaxSelector(S);
		
		while(it < 100000 && S.violations() > 0) {
			VarIntLS sel_x = mms.selectMostViolatingVariable();
			int sel_v = mms.selectMostPromissingValue(sel_x);
			sel_x.setValuePropagate(sel_v);
			System.out.println("Step " + it + ", violation = " + S.violations());
			it ++;
		}
	}
	
	public void hillClimbing() {
		int it = 0;
		while (it < 100000 && S.violations() > 0) {
			int min_delta = Integer.MAX_VALUE;
			int seli = -1;
			int selv = -1;
			for (int i = 0; i < n; i++) {
				for (int v = x[i].getMinValue(); v <= x[i].getMaxValue(); v++) {
					int delta = S.getAssignDelta(x[i], v);
					if (delta < min_delta) {
						min_delta = delta;
						seli = i;
						selv = v;
					}
				}
			}
			x[seli].setValuePropagate(selv);
			System.out.println("Step " + it + ", violation = " + S.violations());
			it ++;
		}		
	}
	
	public NQueen(int n) {
		this.n = n;
	}
	
	public void solve() {
		stateModel();
//		search();
		hillClimbing();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start");
		NQueen s = new NQueen(100);
		s.solve();
	}

}
