package neocognitron;

public class VSCell {
	
	private double[] c;
	
	public VSCell(double[] initC) {
		c = initC;
	}

	public double propagate(double[][] inputs) {
		double output = 0;
		for (int ck = 0; ck < inputs.length; ck++) {
			for (int w = 0; w < inputs[0].length; w++) {
				inputs[ck][w] = Math.pow(inputs[ck][w],2);
				output += inputs[ck][w] * c[w];
			}
		}
		output = Math.sqrt(output);
		return output;
	}
}
