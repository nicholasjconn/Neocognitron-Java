/**
 * 
 */
package neocognitron;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

/**
 * The object which contains all the outputs from a specific neocognitron
 * layer. There is a set number of planes and a set size for each plane.
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
	 * Set an entire plane's output matrix
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
	
	/**
	 * For a specific location and window size, return the resulting window
	 * as a single dimensional array. 
	 * 
	 * @param k				plane location k
	 * @param n				location n
	 * @param m				location m
	 * @param windowSize	size of the square window
	 * @return				array representation of the window
	 */
	public double[] getWindowInPlane(int k, int n, int m, int windowSize) {
		// Ensure that all inputs are correct
		if (windowSize % 2 == 0 && windowSize != size)
			throw new IllegalArgumentException(
					"Window size not odd nor size of plane!");

		// Initialize the output array
		double[] out = new double[(int) Math.pow(windowSize, 2)];

		// If window size is the entire plane, return the entire plane
		if (windowSize == size) {
			int count = 0;
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					out[count] = outputs[k][x][y];
					count++;
				}
			}
		// otherwise, convert the window into an array
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
	
	/**
	 * Get array of windows for a specific point in each plane and a given window
	 * size. The output is formated so that output[planes][window]. 
	 * 
	 * @param n				location n
	 * @param m				location m
	 * @param windowSize	size of the square window
	 * @return				array representation of each window in each plane
	 */
	public double[][] getWindows(int n, int m, int windowSize) {
		double[][] out = new double[K][(int) Math.pow(windowSize, 2)];

		for (int k = 0; k < K; k++)
			out[k] = getWindowInPlane(k, n, m, windowSize);

		return out;
	}

	/**
	 * For a specific location and window size, return the resulting window
	 * as a two dimensional array. 
	 * 
	 * @param k				plane location k
	 * @param n				location n
	 * @param m				location m
	 * @param windowSize	size of the square window
	 * @return				array representation of the window
	 */
	public double[][] getSquareWindowInPlane(int k, int n, int m, int windowSize) {

		if (windowSize % 2 == 0 && windowSize != size)
			throw new IllegalArgumentException(
					"Window size not odd nor size of plane!");

		double[][] out = new double[windowSize][windowSize];

		if (windowSize == size) {
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					out[x][y] = outputs[k][x][y];
				}
			}
		} else {
			int offset = (windowSize - 1) / 2;
			for (int x = n - offset; x <= n + offset; x++) {
				for (int y = m - offset; y <= m + offset; y++) {
					try {
						out[x-n+offset][y-m+offset] = outputs[k][x][y];
					} catch (ArrayIndexOutOfBoundsException e) {
						out[x-n+offset][y-m+offset] = 0;
					}
				}
			}
		}

		return out;
	}

	/**
	 * Get array of two dimensional window matrices for a specific point in each
	 * plane and a given window size. The output is formated so that
	 * output[planes][window]. 
	 * 
	 * @param n				location n
	 * @param m				location m
	 * @param windowSize	size of the square window
	 * @return				array representation of each window in each plane
	 */
	public double[][][] getSquareWindows(int n, int m, int windowSize) {
		double[][][] out = new double[K][windowSize][windowSize];

		for (int k = 0; k < K; k++)
			out[k] = getSquareWindowInPlane(k, n, m, windowSize);

		return out;
	}
	
	/**
	 * Get a specific output for a given location.
	 * 
	 * @param l	Location for determining the output
	 * @return	Output at location l.
	 */
	public double getSingleOutput(Location l) {
		return outputs[l.getPlane()][l.getPoint().x][l.getPoint().y];
	}
	
	/**
	 * Get a specific output for a given location, plane k, and point (n,m).
	 * 
	 * @param k		Plane k
	 * @param n		location n
	 * @param m		location m
	 * @return		output at location k (n,m)
	 */
	public double getSingleOutput(int k, int n, int m) {
		return outputs[k][n][m];
	}
		
	/**
	 * For a given s-column, determine the location of the maximum output value. This
	 * requires knowledge of the window size and the center point of the s-column.
	 * 
	 * @param sColumn		three dimensional s-column array
	 * @param center		center location of the column
	 * @param windowSize	window size used to generate the s-column
	 * @return				Location of maximum value
	 */
	public static Location getLocationOfMax(double[][][] sColumn, Point center,int windowSize) {
		Location maxL = null;
		double maxValue = 0;
		
		// Find maximum value and corresponding location
		for (int k = 0; k < sColumn.length; k++) {
			for (int n = 0; n < sColumn[0].length; n++) {
				for (int m = 0; m < sColumn[0][0].length; m++) {
					if ( sColumn[k][n][m] > maxValue) {
						maxValue = sColumn[k][n][m];
						maxL = new Location(k,n,m);
					}
				}
			}
		}

		// Determine offset for calculating overall location of max
		int offset = (windowSize - (windowSize%2)) / 2;

		// If a max exists, generate location object
		if (maxL != null) {
			Point p = maxL.getPoint();
			p.setLocation(p.x+center.x-offset, p.y+center.y-offset);
			maxL.setPoint(p);
		}
		
		return maxL;
	}
	
	/**
	 * Given a list of possible representative cells, and a desired plane, determine the location
	 * of the maximum point. Typically there will only be one possible point for a given plane.
	 * 
	 * @param plane	Plane under test
	 * @param l		list of locations for possible representative cells 
	 * @return		specific point in the given plane of the maximum output
	 */
	public Point getMaxPerPlane(int plane, List<Location> l) {
		Point p = null;
		double maxValue = 0;
		Location temp;
		
		for (int i = 0; i < l.size(); i++) {
			temp = l.get(i);
			if (temp == null) {
				p = null;
			}
			else if (temp.getPlane() == plane) {
				if (getSingleOutput(temp) > maxValue) {
					maxValue = getSingleOutput(temp);
					p = temp.getPoint();
				}
			}
		}
		
		return p;
	}

	/**
	 * Get the list of representative cells for this specific set ofoutputs. This
	 * requires a window size, which is used to generate the s-colums. The output
	 * is an array of points, one for every plane; some planes will have a null
	 * point value which means that it does not have a representative cell.
	 * 
	 * @param windowSize	window size used to generate the s-column
	 * @return				the array of representative cells
	 */
	public Point[] getRepresentativeCells(int windowSize) {
		List<Location> points = new ArrayList<Location>();
		Location temp;
		
		int offset = (windowSize - 1)/2;
		
		double[][][] sColumn;
		
		// Create a list of all possible representative cells
		if (windowSize == size) {
			sColumn = getSquareWindows(size/2,size/2,windowSize);
			temp = getLocationOfMax(sColumn, new Point(size/2,size/2), windowSize);
			points.add(temp);
		}
		else {
		
			for ( int n = offset; n < size-offset; n++) {
				for (int m = offset; m < size-offset; m++) {
					sColumn = getSquareWindows(n, m, windowSize);
					temp = getLocationOfMax(sColumn, new Point(n,m), windowSize);
					if ( temp != null )
						if ( !points.contains(temp) )
							points.add(temp);
				}
			}
		}
		
		// Convert list of locations to array of points, one per plane
		Point[] reps = new Point[K];
		for (int k = 0; k < K; k++)
			reps[k] = getMaxPerPlane(k, points);
			
		// Must only leave 1 per plane
		return reps;
	}
	
	/**
	 * Convert entire OutputConnections to a three dimensional array.
	 * Where the output is o[planes][size][size]
	 * 
	 * @return	array output
	 */
	public double[][][] toArray() {
		return outputs;
	}

	/**
	 * Convert an array to a string.
	 * 
	 * @param a		Array to be converted
	 * @return		String output
	 */
	public static String arrayToString(double[] a) {
		DecimalFormat df = new DecimalFormat("0.##E0");
		DecimalFormat df1 = new DecimalFormat("#.##");
		
		String s = "";
		double value;
		
		for(int x = 0; x < a.length; x++) { 
				value = a[x];
				if (value < 999 && value > .01)
					s += df1.format(value) + "\t";
				else if (value == 0)
					s += value + "\t";
				else
					s += df.format(value) + "\t";
		}
		return s;
	}
	
	/**
	 * Convert a two dimensional array to a string.
	 * 
	 * @param a		Array to be converted
	 * @return		String output
	 */
	public static String arrayToString(double[][] a) {
		DecimalFormat df = new DecimalFormat("0.##E0");
		DecimalFormat df1 = new DecimalFormat("#.##");
		
		String s = "";
		double value;
		
		for(int x = 0; x < a.length; x++) { 
			for (int y = 0; y < a[x].length; y++) {
				value = a[x][y];
				if (value < 999 && value > .01)
					s += df1.format(value) + "\t";
				else if (value == 0)
					s += value + "\t";
				else
					s += df.format(value) + "\t";
			}
			s += "\n";
		}
		return s;
	}

	@Override public String toString() {
		// Formats for decimals
		DecimalFormat df = new DecimalFormat("0.##E0");
		DecimalFormat df1 = new DecimalFormat("#.##");

		String outputS = "Number of Planes: " + K + "\n";
		outputS += "Size: " + size + " by " + size + "\n\n";
		
		double value;
		
		// Loop through all the planes and all the locations in each plane
		for (int k = 0; k < K; k++) {
			outputS += "Plane " + k + ":\n";
			for (int m = 0; m < size; m++) {
				for (int n = 0; n < size; n++) {
					value = outputs[k][n][m];
					if (value < 999 && value > .01)
						outputS += df1.format(value) + "\t";
					else if (value == 0)
						outputS += value + "\t";
					else
						outputS += df.format(value) + "\t";
				}
				outputS += '\n';
			}
			outputS += '\n';
		}
		
		return outputS;
	}
}
