package neocognitron;

import java.io.Serializable;

/**
 * The vs-cell contains all the constants needed to propagate a signal correctly
 * for the neocognitron. This is the v-cell which will be part of the s-layer.
 * Additionally, the vs-cell is serializable so that it can be saved for further
 * use.
 * 
 * @author Nicholas J. Conn
 *
 */
public class VSCell implements Serializable {

	// UID number used for object serialization
	private static final long serialVersionUID = 7536521085321150127L;

	// Weight array c[window]
	private double[] c;
	
	/**
	 * Initialize the vs-cell, with weight c.
	 * 
	 * @param initC	Initial value for weight c[window]
	 */
	public VSCell(double[] initC) {
		c = initC;
	}

	/**
	 * Propagate the input signal input[window]. 
	 * 
	 * @param inputs	input window in vector form; input[window]
	 * @return			output from v-cell
	 */
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
