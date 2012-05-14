package neocognitron;

import java.io.Serializable;

public class VSCell implements Serializable {
	
	private static final long serialVersionUID = 7536521085321150127L;
	
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
