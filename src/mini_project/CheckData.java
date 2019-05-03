package mini_project;

public class CheckData {
	MinMaxTypeMultiKnapsackInput input;

	public CheckData(String fn) {
		this.input = MinMaxTypeMultiKnapsackInput.loadFromFile(fn);
	}
	
	public static void main(String[] args) {
		CheckData c = new CheckData("/home/thangnd/git/java/Optimization/data/MinMaxTypeMultiKnapsackInput-3000.json");
		System.out.println(c.input.getItems()[0].getBinIndices());
		
		
	}

}
