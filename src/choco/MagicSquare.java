package choco;

import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;
import oracle.jrockit.jfr.tools.ConCatRepository;

public class MagicSquare {
	
	int n;
	int M;
	
	Model m = new CPModel();
	IntegerVariable[][] cells;
	
	public MagicSquare(int n) {
		this.n = n;
		this.M = n*(n*n + 1) / 2;
		this.cells = new IntegerVariable[n][n];
	}
	
	/**
	 * 
	 */
	public void createModel() {
		
		for (int i=0; i < n; i ++) {
			for (int j=0; j < n; j++) {
				this.cells[i][j] = Choco.makeIntVar("cell" + j, 1, n*n);
				this.m.addVariable(this.cells[i][j]);
			}
		}
		
		// Constraint over rows
		Constraint[] rows = new Constraint[n];
		for (int i = 0; i < n; i++) {
			rows[i] = Choco.eq(Choco.sum(this.cells[i]), M);
		}
		this.m.addConstraints(rows);
		
		// Constraint over columns
		IntegerVariable[][] cellsDual = new IntegerVariable[n][n];
		for (int i = 0; i < n; i ++) {
			for (int j = 0; j  < n; j ++) {
				cellsDual[i][j] = cells[j][i];
			}
		}
		Constraint[] cols = new Constraint[n];
		for (int i = 0; i < n; i++) {
			cols[i] = Choco.eq(Choco.sum(cellsDual[i]), M);
		}
		this.m.addConstraints(cols);
		// Constraint over diagonal
		IntegerVariable[][] diags = new IntegerVariable[2][n];
		for (int i = 0; i < n; i++) {
			diags[0][i] = cells[i][i];
			diags[1][i] = cells[i][n-1-i];
		}
		this.m.addConstraint(Choco.eq(Choco.sum(diags[0]), M));
		this.m.addConstraint(Choco.eq(Choco.sum(diags[1]), M));
		
		// All cells are difference from each other
		IntegerVariable[] allVar = new IntegerVariable[n*n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				allVar[i*n + j] = cells[i][j];
			}
		}
		this.m.addConstraints(Choco.allDifferent(allVar));
		
		// Logger
		System.out.println("Create model successfully !");
	}
	
	/**
	 * 
	 */
	public void solve() {
		Solver s = new CPSolver();
		s.read(this.m);
		System.out.println("Solving ...");
		s.solve();
		
		// Print variables
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(s.getVar(cells[i][j]).getVal() + " ");
			}
			System.out.println();
		}
		
	}
	

	public static void main(String[] args) {
		MagicSquare magicSquare = new MagicSquare(5);
		magicSquare.createModel();
		magicSquare.solve();
	}

}
