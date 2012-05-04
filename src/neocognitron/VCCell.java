package neocognitron;

public class VCCell {
	
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
