package cbls;
import java.io.File;
import java.util.Scanner;

import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.LessThan;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class BACP {
	int N;
	int M;
	int[] I;
	int[] J;
	int minCrd;
	int maxCrd;
	int minCourses;
	int maxCourses;
	
	LocalSearchManager mgr;
	VarIntLS[] x;
	ConstraintSystem S;
	int[] crd;
	
	IFunction[] credits; // credits[i] so tin hoc ky i
	IFunction[] nbCourses;
	
	
	public void stateModel() {
		mgr = new LocalSearchManager();
		x = new VarIntLS[N];
		for (int i = 0; i < N; i ++) {
			x[i] = new VarIntLS(mgr, 0, M-1);
		}
		S = new ConstraintSystem(mgr);
		for (int k = 0; k < I.length; k++) {
			S.post(new LessThan(x[I[k]], x[J[k]]));
		}
		
		credits = new IFunction[M];
		for (int i = 0; i < M; i++) {
			credits[i] = new ConditionalSum(x, crd, i);
			S.post(new LessOrEqual(credits[i], maxCrd));
			S.post(new LessOrEqual(minCrd, credits[i]));
		}
		
		nbCourses = new IFunction[M];
		for (int i = 0; i < M; i++) {
			nbCourses[i] = new ConditionalSum(x, i);
			S.post(new LessOrEqual(nbCourses[i], maxCourses));
			S.post(new LessOrEqual(minCourses, nbCourses[i]));
		}
		mgr.close();
	}
	
	public void search() {
		HillClimbingSearch.hillClimbing(S, 10000);
	}
	
	public void readData(String str) {
		try {
			Scanner scanner = new Scanner(new File(str));
			M = scanner.nextInt();
			N = scanner.nextInt();
			minCrd = scanner.nextInt();
			maxCrd = scanner.nextInt();
			minCourses = scanner.nextInt();
			maxCourses = scanner.nextInt();
			
			crd = new int[N];
			for (int i = 0; i < N; i++) {
				crd[i] = scanner.nextInt();
			}
			int K = scanner.nextInt();
			I = new int[K];
			J = new int[K];
			for(int k = 0; k < K; k++) {
				I[k] = scanner.nextInt();
				J[k] = scanner.nextInt();
			}
			scanner.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printResult() {
		for (int i= 0; i < M; i++) {
			System.out.println("HK " + i + ": ");
			for(int j = 0; j < N; j++) {
				if (x[j].getValue() == i) System.out.println(j + ", ");
			}
			System.out.print(", load = " + credits[i].getValue());
			System.out.println(", nbCourses = " + nbCourses[i].getValue());
		}
	}
	

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BACP s = new BACP();
		s.readData("data/BACP/bacp.in01");
		s.stateModel();
		s.search();

	}

}
