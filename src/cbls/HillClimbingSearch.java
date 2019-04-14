package cbls;
import java.util.ArrayList;
import java.util.Random;

import localsearch.model.IConstraint;
import localsearch.model.VarIntLS;

public class HillClimbingSearch {
	/**
	 * 
	 * @param S
	 * @param maxIter
	 */
	public static void hillClimbing(IConstraint S, int maxIter) {
		VarIntLS[] x = S.getVariables();
		ArrayList<AssignMove> candidates = new ArrayList<AssignMove>();
		Random R = new Random();
		
		int it = 0;
		while (it < maxIter && S.violations() > 0) {
			// step 1: collect candidates
			candidates.clear();
			int minDelta = Integer.MAX_VALUE;
			for (int i = 0; i < x.length; i++) {
				for(int v = x[i].getMinValue(); v <= x[i].getMaxValue(); v ++) {
					int d = S.getAssignDelta(x[i], v);
					if (d < minDelta) {
						candidates.clear();
						candidates.add(new AssignMove(i, v));
						minDelta = d;
					} else if (d == minDelta) {
						candidates.add(new AssignMove(i, v));
					}
				}
			}
			
			// step 2: random selection
			int idx = R.nextInt(candidates.size());
			AssignMove m = candidates.get(idx);
			x[m.i].setValuePropagate(m.v);
			
			System.out.println("step " + it + ", violation = " + S.violations());
			it ++;
		}
		
	}
}
