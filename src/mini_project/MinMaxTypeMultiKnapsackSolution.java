package mini_project;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class MinMaxTypeMultiKnapsackSolution {
	
	MinMaxTypeMultiKnapsackInput input;
	
	Model model = new Model();
	IntVar[][] X;
	IntVar[][] Y;
	IntVar[][] Z;
	int numItems;
	int numBins;
	int numTypes;
	int numClass;
	
	/**
	 * Initial variable
	 */
	public void initVariable() {
		X = new IntVar[numItems][numBins];
		for (int i = 0; i < numItems; i++) {
			for (int j = 0; j < numBins; j++) {
				X[i][j] = model.intVar("X_" + i + "_" + j, 0, 1);
			}
		}
		
		// Find the max of types item
		this.numTypes = 1;
		for (int i = 0; i < numItems; i++) {
			if (input.getItems()[i].getT() > numTypes) {
				this.numTypes = input.getItems()[i].getT(); 
			}
		}
		this.numTypes += 1;
		Y = new IntVar[this.numTypes][numBins];
		for (int i = 0; i < this.numTypes; i++) {
			for (int j = 0; j < numBins; j++) {
				Y[i][j] = model.intVar("Y_" + i + "_" + j, 0, 1);
			}
		}
		
		// Find the max class of bins
		this.numClass = 1;
		for (int i = 0; i < this.numItems; i ++) {
			if (input.getItems()[i].getR() > this.numClass) {
				numClass = input.getItems()[i].getR();
			}
		}
		this.numClass += 1;
		Z = new IntVar[this.numClass][numBins];
		for (int i = 0; i < this.numClass; i++) {
			for (int j = 0; j < numBins; j++) {
				Z[i][j] = model.intVar("Z_" + i + "_" + j, 0, 1);
			}
		}
	}
	
	/**
	 * Create Model
	 */
	public void stateModel() {

		this.initVariable();
		
		// assign array p of items
		int[] p = new int[this.numItems]; 
		for (int i = 0; i < this.numItems; i++) {
			p[i] = (int) this.input.getItems()[i].getP();
		}
		// assign array w of items
		int[] w = new int[this.numItems];
		for (int i = 0; i < this.numItems; i++) {
			w[i] = (int) (this.input.getItems()[i].getW()* 1000);
		}
		
		// Add constraints
		
		for (int i = 0; i < this.numItems; i ++) {
			model.sum(this.X[i], "=", 1).post();
		}
		
		// W
		for (int i = 0; i < numBins; i++) {
			IntVar[] temp = new IntVar[this.numItems];
			for (int j = 0; j < this.numItems; j++) {
				temp[j] = X[j][i];
			}
			model.scalar(temp, w, "<=", (int) (this.input.getBins()[i].getCapacity()*1000)).post();
//			model.scalar(temp, w, ">=", (int) (this.input.getBins()[i].getMinLoad()*1000)).post();
		}
		
		// P
		for (int i = 0; i < numBins; i++) {
			IntVar[] temp = new IntVar[this.numItems];
			for (int j = 0; j < this.numItems; j++) {
				temp[j] = X[j][i];
			}
			model.scalar(temp, p, "<=", (int) this.input.getBins()[i].getP()).post();
		}
		// t, r
		for (int i = 0; i < this.numItems; i++) {
			for (int b = 0; b < this.numBins; b++) {
				model.arithm(this.X[i][b], "<=", this.Y[this.input.getItems()[i].getT()][b]).post();
				model.arithm(this.X[i][b], "<=", this.Z[this.input.getItems()[i].getR()][b]).post();
			}
		}
		// 
		IntVar[] temp1 = new IntVar[this.numTypes];
		IntVar[] temp2 = new IntVar[this.numClass];
		for (int i = 0; i < this.numBins; i++) {
			for (int j = 0; j < this.numTypes; j++) {
				temp1[j] = this.Y[j][i];
			}
			model.sum(temp1, "<=", this.input.getBins()[i].getT()).post();
			
			//
			for (int j = 0; j < this.numClass; j++) {
				temp2[j] = this.Z[j][i];
			}
			model.sum(temp2, "<=", this.input.getBins()[i].getR()).post();		
		}
		
		
	}
	
	/**
	 * Search solution
	 */
	public void search() {
		Solver solver = this.model.getSolver();
		if(solver.solve()){
			
			for(int i = 0; i < this.numBins; i ++) {
				int totItemWeight = 0;
				System.out.println("\n");
				System.out.print("Bins " + i + ": ");
				
				for (int j = 0; j < this.numItems; j ++) {
					if (this.X[j][i].getValue() == 1) {
						System.out.print(j + " ");
						totItemWeight += this.input.getItems()[j].getW();
					}
				}
				
				System.out.println("\nmax W: " + this.input.getBins()[i].getCapacity());
				System.out.println("total item weight: " + totItemWeight);
			}
		} else {
			System.out.println("The solver has proved the problem has no solution");
		}
	}
	
	/**
	 * Constructor
	 * @param fn
	 */
	public MinMaxTypeMultiKnapsackSolution(String fn) {
		this.input = MinMaxTypeMultiKnapsackInput.loadFromFile(fn);
		this.numItems = input.getItems().length;
		this.numBins = input.getBins().length;
		System.out.println("the number of items : " + this.numItems);
		System.out.println("the number of bins :  " + this.numBins);
		
		this.numItems = 40;
		this.numBins = 40;
	}

	public static void main(String[] args) {
		MinMaxTypeMultiKnapsackSolution s = new MinMaxTypeMultiKnapsackSolution("/home/thangnd/git/java/Optimization/data/MinMaxTypeMultiKnapsackInput.json");
		System.out.println("Load data okay !");
		s.stateModel();
		System.out.println("Build state model okay !");
		System.out.println("Solving .......");
		s.search();
		System.out.println("Done");
	}

}
