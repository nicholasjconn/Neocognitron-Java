package neocognitron;

/**
 * 
 * @author Nicholas
 *
 */
public class SCell {
	
	double r;

	public SCell(double initR) {
		r = initR;
	}
	
	public double propagate(double[][] inputs, double vInput, double b, double[][] a) {
		
		double output = 0;
				
		for( int ck = 0; ck < inputs.length; ck++) {
			output += NeocognitronStructure.arrayMultiply(a[ck],inputs[ck]);
		}
		
		double denominator = 1 + 2*r/(1+r)*b*vInput;
		
		output = (1 + output)/denominator - 1;
		
		// Output function, set to zero if negative
		if (output < 0)
			output = 0;
		
		// Final multiplication
		return r*output;
	}
}
