package neocognitron;

import java.io.Serializable;

public class VCCell implements Serializable {
	
	private static final long serialVersionUID = 7536521085321150125L;
	
	private double[] d;
	
	public VCCell(double[] initD) {
		d = initD;
	}
	
	public double propagate(double[][] inputs) {
		
		if (inputs[0].length != d.length)
			throw new IllegalArgumentException("Sizes are not equal!");
		
		double output = 0;
		
		// where input is inputs[sk][window]
		// a window in each plane
		
		
		for(int sk = 0; sk < inputs.length; sk++) {
			output += NeocognitronStructure.arrayMultiply(d, inputs[sk]);
		}
		
		return output/inputs.length;
	}
}
