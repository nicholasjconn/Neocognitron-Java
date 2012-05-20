package neocognitron;

import java.io.Serializable;

/**
 * The vc-cell contains all the constants needed to propagate a signal correctly
 * for the neocognitron. This is the v-cell which will be part of the c-layer.
 * Additionally, the vc-cell is serializable so that it can be saved for further
 * use.
 * 
 * @author Nicholas J. Conn
 *
 */
public class VCCell implements Serializable {

	// UID number used for object serialization
	private static final long serialVersionUID = 7536521085321150125L;

	// Weight array d[window]
	private double[] d;

	/**
	 * Initialize the vc-cell, with weight d.
	 * 
	 * @param initD	Initial value for weight d[window]
	 */
	public VCCell(double[] initD) {
		d = initD;
	}
	
	/**
	 * Propagate the input signal input[window]. 
	 * 
	 * @param inputs	input window in vector form; input[window]
	 * @return			output from v-cell
	 */
	public double propagate(double[][] inputs) {
		
		double output = 0;
		
		// where input is inputs[sk][window]
		// a window in each plane
		for(int sk = 0; sk < inputs.length; sk++) {
			output += NeocognitronStructure.arrayMultiply(d, inputs[sk]);
		}
		
		return output/inputs.length;
	}
}
