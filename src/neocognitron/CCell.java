package neocognitron;

import java.io.Serializable;

/**
 * The c-cell contains all the constants needed to propagate a signal correctly
 * for the neocognitron. Additionally, the c-cell is serializable so that it
 * can be saved for further use.
 * 
 * @author Nicholas J. Conn
 *
 */
public class CCell implements Serializable {
	
	// UID number used for object serialization
	private static final long serialVersionUID = 7536521085321150124L;

	// Weight array d[window]
	private double[] d;
	
	// Global constant alpha 
	private double alpha;

	/**
	 * Initialize the c-cell, with weight d and constant alpha
	 * 
	 * @param initD		Initial value for weight d[window] 
	 * @param initAlpha	Initial value for alpha
	 */
	public CCell(double[] initD, double initAlpha) {
		d = initD;
		alpha = initAlpha;
	}

	/**
	 * Propagate the input signal input[window]. Use the input and the 
	 * corresponding v-cell value to determine the output. 
	 * 
	 * @param input	input window in vector form; input[window]
	 * @param v		input from corresponding v-cell
	 * @return		output from c-cell
	 */
	public double propagate(double[] input, double v)
	{
		double output = NeocognitronStructure.arrayMultiply(d, input);
		output = (1 + output)/(1 + v) - 1;
		
		output = output/(alpha + output);
		
		// For negative outputs, set to zero
		if (output < 0)
			output = 0;

		return output;
	}
}
