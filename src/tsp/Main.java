package tsp;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VarTSP x = new VarTSP(6);
		System.out.println(x.route);
		x.towOptMove(4, 6);
		System.out.println(x.route);
	}

}
