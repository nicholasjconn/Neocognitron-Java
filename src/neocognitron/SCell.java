package neocognitron;

import java.io.Serializable;

/**
 * The s-cell contains all the constants needed to propagate a signal correctly
 * for the neocognitron. Additionally, the s-cell is serializable so that it
 * can be saved for further use.
 * 
 * @author Nicholas J. Conn
 *
 */
public class SCell implements Serializable {

	// UID number used for object serialization
	private static final long serialVersionUID = 7536521085321150128L;
	
	// Constant r for specific layer
	double r;

	/**
	 * Initialize the s-cell, with the constant r
	 * 
	 * @param initR		Initial value for constant r
	 */
	public SCell(double initR) {
		r = initR;
	}
	
	/**
	 * Progagate the input signal input[plane][window]. Use the input and the
	 * corresponding v-cell value to determine the output. Additionally, the
	 * weights needed are sent as arguments, rather than stored in the cell.
	 * 
	 * @param inputs	input window in vector form; input[window]
	 * @param vInput	input from corresponding v-cell
	 * @param b			inhibitory weight value  
	 * @param a			weight value for input window
	 * @return			output from the s-cell
	 */
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
