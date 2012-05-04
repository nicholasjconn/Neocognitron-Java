/**
 * 
 */
package neocognitron;

import java.text.DecimalFormat;

/**
 * 
 * @author Nicholas
 *
 */
public class OutputConnections {

	// Number of planes
	private int K;
	// Matrix size of each plane (square)
	private int size;

	// Actual output values (size x size x K)
	private double[][][] outputs;

	/**
	 * Initialize the connection's, for a set number of planes with a set size,
	 * to zero. Results in an output of K, size by size, planes.
	 * 
	 * @param initK		Number of planes in the specific layer
	 * @param initSize	Size for all planes. Each plane is square and the same size
	 */
	public OutputConnections(int initK, int initSize) {
		K = initK;
		size = initSize;

		// Create K outputs, each (size by size)
		outputs = new double[K][size][size];
	}

	/**
	 * Set an entire planes output matrix
	 * 
	 * @param kValue		Plane location
	 * @param newOutputs	Output matrix
	 */
	public void setPlaneOutput(int kValue, double[][] newOutputs) {
		outputs[kValue] = newOutputs;
	}

	/**
	 * Set a single output value.
	 * 
	 * @param k		Plane location
	 * @param n		Matrix location (1st dimension)
	 * @param m		Matrix location (2nd dimension)
	 * @param value	Value to set output
	 */
	public void setSingleOutput(int k, int n, int m, double value) {
		outputs[k][n][m] = value;
	}

	/**
	 * Get an entire planes output matrix.
	 * 
	 * @param k		Plane location
	 * @return		Returns size by size matrix of output values
	 */
	public double[][] getPlane(int k) {
		return outputs[k];
	}

	/**
	 * Get an array of a certain point (n,m) in every plane.
	 * 
	 * @param n		Matrix location (1st dimension)
	 * @param m		Matrix location (2nd dimension)
	 * @return		Array of of output points for each plane 
	 * 				(output[k] = value[k][n][m] )
	 */
	public double[] getPointsOnPlanes(int n, int m) {
		double[] output = new double[K];
		
		// For every plane, grab point (n,m)
		for (int k = 0; k < K; k++)
			output[k] = outputs[k][n][m];

		return output;
	}

	public double[] getWindowInPlane(int k, int n, int m, int windowSize) {
		if (windowSize % 2 == 0 && windowSize != size)
			throw new IllegalArgumentException(
					"Window size not odd nor size of plane!");

		double[] out = new double[(int) Math.pow(windowSize, 2)];

		if (windowSize == size) {
			int count = 0;
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					out[count] = outputs[k][x][y];
					count++;
				}
			}
		} else {
			int offset = (windowSize - 1) / 2;
			int count = 0;
			for (int x = n - offset; x <= n + offset; x++) {
				for (int y = m - offset; y <= m + offset; y++) {
					try {
						out[count] = outputs[k][x][y];
					} catch (ArrayIndexOutOfBoundsException e) {
						out[count] = 0;
					}
					count++;
				}
			}
		}

		return out;
	}

	// TODO Finish commenting OutputConnections
	public double[][] getWindows(int n, int m, int windowSize) {
		double[][] out = new double[K][(int) Math.pow(windowSize, 2)];

		for (int k = 0; k < K; k++)
			out[k] = getWindowInPlane(k, n, m, windowSize);

		return out;
	}
	
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.##E0");
		
		String outputS = "Number of Planes: " + K + "\n";
		outputS += "Size: " + size + " by " + size + "\n\n";
		
		for (int k = 0; k < K; k++) {
			outputS += "Plane " + k + ":\n";
			for (int n = 0; n < size; n++) {
				for (int m = 0; m < size; m++) {
					outputS += df.format(outputs[k][n][m]) + "\t";
				}
				outputS += '\n';
			}
			outputS += '\n';
		}
		
		return outputS;
	}
}
