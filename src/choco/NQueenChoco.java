package choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

public class NQueenChoco {

	public static void main(String[] args) {
		int n = 8;
		
		Model model = new Model();
		IntVar[] x = new IntVar[n];
		
		for (int i = 0; i < n; i++) {
			x[i] = model.intVar(x+"_i", 1, n);
		}
		
		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				model.arithm(x[i], "!=", x[j], "-", j-i).post();
				model.arithm(x[i], "!=", x[j], "+", j-i).post();
			}
		}
		model.allDifferent(x).post();
		
		
		Solution solution = model.getSolver().findSolution();
		if(solution != null) {
			System.out.println(solution.toString());
		}
	}

}
