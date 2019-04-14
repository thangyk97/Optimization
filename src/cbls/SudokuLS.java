package cbls;


import java.util.ArrayList;
import java.util.Random;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class SudokuLS {
	class SwapMove {
		int i;
		int j1;
		int j2;
		public SwapMove(int i, int j1, int j2) {
			this.i = i; this.j1 = j1; this.j2 = j2;
		}
	}
	
	private LocalSearchManager mgr;
	private VarIntLS[][] x;
	private ConstraintSystem S;
	
	public void stateModel() {
		mgr = new LocalSearchManager();
		x = new VarIntLS[9][9];
		// Initialize x
		for (int i = 0; i < 9; ++i) {
			for (int j = 0; j < 9; ++j) {
				x[i][j] = new VarIntLS(mgr, 1, 9);
				x[i][j].setValue(j+1);
			}
		}
		
		S = new ConstraintSystem(mgr);
		// Rows
		for (int i = 0; i < 9; ++i) {
			VarIntLS[] y = new VarIntLS[9];
			for (int j = 0; j < 9; ++j) {
				y[j] = x[i][j];
			}
			S.post(new AllDifferent(y));
		}
		// Columns
		for (int j = 0; j < 9; ++j) {
			VarIntLS[] y = new VarIntLS[9];
			for (int i = 0; i < 9; ++i) {
				y[i] = x[i][j];
			}
			S.post(new AllDifferent(y));
		}
		
		// Child squares
		for (int I = 0; I < 3; ++I) {
			for (int J = 0; J < 3; ++J) {
				VarIntLS[] y = new VarIntLS[9];
				int idx = -1;
				for (int i = 0; i < 3; ++i) {
					for (int j = 0; j < 3; ++j) {
						idx ++;
						y[idx] = x[3*I +i][3*J + j];
					}
				}
				S.post(new AllDifferent(y));
			}
		}
		
		mgr.close(); // mandatory
	}
	
	public void hillClimbing() {
		
	}
	
	public void search() {
//		HillClimbingSearch.hillClimbing(this.S, 1000); // tac violation = 9
		
		ArrayList<SwapMove> candidates = new ArrayList<SwapMove>();
		int it = 0;
		while (it < 10000 && S.violations() > 0) {
			// Collect candidates
			candidates.clear();
			int minDelta = Integer.MAX_VALUE;
			for (int i = 0; i < 9; ++i) {
				for (int j1 = 0; j1 < 9; ++j1) {
					for (int j2 = 0; j2 < 9; ++j2) {
						int delta = S.getSwapDelta(x[i][j1], x[i][j2]);
						if (delta < minDelta) {
							candidates.clear();
							candidates.add(new SwapMove(i, j1, j2));
						} else if (delta == minDelta) {
							candidates.add(new SwapMove(i, j1, j2));
						}
					}
				}
			}
			// Select randomly
			Random rd = new Random();
			
			SwapMove m = candidates.get(rd.nextInt(candidates.size()));
			x[m.i][m.j1].swapValuePropagate(x[m.i][m.j2]);
			System.out.println("Step " + it + ", S = " + S.violations());
			++it;
			
		}
		

	}
	
	public void solve() {
		stateModel();
		search();
	}
	
	public static void main(String[] args) {
		SudokuLS s = new SudokuLS();
		s.solve();
	}
}
