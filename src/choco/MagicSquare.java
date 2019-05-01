package choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class MagicSquare {
	Model model = new Model();
	IntVar[][] x;
	int n;
	int m;

	public void stateModel() {
		this.x = new IntVar[this.n][this.n];
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.n; j++) {
				this.x[i][j] = this.model.intVar("x_" + i + j, 1, this.n * this.n);
			}
		}

		// Add constraints
		// row
		for (int i = 0; i < this.n; i++) {
			this.model.sum(this.x[i], "=", this.m).post();
		}
		// column
		for (int i = 0; i < this.n; i++) {
			IntVar[] temp = new IntVar[this.n];
			for (int j = 0; j < this.n; j++) {
				temp[j] = this.x[i][j];
			}
			this.model.sum(temp, "=", this.m).post();
		}
		// diagonal
		IntVar[][] diagonals = new IntVar[2][n];
		for (int i = 0; i < this.n; i++) {
			diagonals[0][i] = this.x[i][i];
			diagonals[1][i] = this.x[i][this.n - 1 - i];
		}
		this.model.sum(diagonals[0], "=", this.m).post();
		this.model.sum(diagonals[1], "=", this.m).post();

		// unique
		IntVar[] temp = new IntVar[this.n * this.n];
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.n; j++) {
				temp[i * this.n + j] = this.x[i][j];
			}
		}
		this.model.allDifferent(temp).post();

		// Logger
		System.out.println("Create model successfully !");
	}

	/**
	 * 
	 */
	public void solve() {
		Solver solver = this.model.getSolver();
		if(solver.solve()){
			for (int i = 0; i < this.n; i++) {
				for (int j = 0; j < this.n; j++) {
					System.out.print(this.x[i][j].getValue() + "\t");
				}
				System.out.println();
				System.out.println();
			}
		} else {
			System.out.println("The solver has proved the problem has no solution");
		}
	}

	public MagicSquare(int n) {
		this.n = n;
		this.m = this.n * (this.n * this.n + 1) / 2;
	}

	public static void main(String[] args) {
		MagicSquare magicSquare = new MagicSquare(11);
		magicSquare.stateModel();
		magicSquare.solve();
	}

}
