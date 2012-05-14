/**
 * 
 */
package neocognitron;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

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
	
	public double getSingleOutput(Location l) {
		return outputs[l.getPlane()][l.getPoint().x][l.getPoint().y];
	}
	
	public double getSingleOutput(int k, int n, int m) {
		return outputs[k][n][m];
	}
	
	// TODO Finish commenting OutputConnections
	public double[][][] getSquareWindows(int n, int m, int windowSize) {
		double[][][] out = new double[K][windowSize][windowSize];

		for (int k = 0; k < K; k++)
			out[k] = getSquareWindowInPlane(k, n, m, windowSize);

		return out;
	}
	
	public Location getLocationOfMax(double[][][] sColumn, Point center,int windowSize) {
		Location maxL = null;
		double maxValue = 0;
		
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

		int offset = (windowSize - (windowSize%2)) / 2;

		if (maxL != null) {
			Point p = maxL.getPoint();
			p.setLocation(p.x+center.x-offset, p.y+center.y-offset);
			maxL.setPoint(p);
		}
		
		return maxL;
	}
	
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
	
	//output is Point[k]
	public Point[] getRepresentativeCells(int windowSize) {
		List<Location> points = new ArrayList<Location>();
		Location temp;
		
		int offset = (windowSize - 1)/2;
				
		double[][][] sColumn;
		
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
			
		Point[] reps = new Point[K];
		for (int k = 0; k < K; k++)
			reps[k] = getMaxPerPlane(k, points);
			
		// Must only leave 1 per plane
		return reps;
	}
	
	public double[][][] toArray() {
		return outputs;
	}
	
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.##E0");
		DecimalFormat df1 = new DecimalFormat("#.##");
		
		String outputS = "Number of Planes: " + K + "\n";
		outputS += "Size: " + size + " by " + size + "\n\n";
		
		double value;
		
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
}
