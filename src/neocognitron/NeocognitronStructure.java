package neocognitron;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

/**
 * The structure which defines an entire neocognitron network. Contains
 * all needed helper functions, in addition to reading an image from a
 * file and converting it to an input.
 * 
 * @author Nicholas J. Conn
 *
 */
public class NeocognitronStructure implements Serializable {

	// UID number used for object serialization
	private static final long serialVersionUID = 7536521085321150122L;
	
	// Values which dictate the structure of the Neocognitron
	public int inputLayerSize = 16;
	public int numLayers = 3;

	// Number of planes in each layer
	private int p = (int) Math.round(Math.random()*10+10);
	public int[] numSPlanes = {p, p, p};
	public int[] numCPlanes = {p, p, p};
	
	// Layer specific values
	public int[] sLayerSizes = {16, 8, 2};
	public int[] cLayerSizes = {10, 6, 1};
	public int[] sWindowSize = {5, 5, 5};
	public int[] cWindowSize = {5, 5, 2};
	public int[] sColumnSize = {5, 5, 2};
	
	//public double[] r = {4, 1.5, 1.5};
	public double[] r = {Math.random()*4+1, Math.random()*1+2, Math.random()*2+2};
	public double[][] c;
	public double[][] d;
	//public double[] q = {.1, 16, 16};
	public double[] q = {Math.random()*.1+.2, Math.random()*4+8, Math.random()*10+6};
	//public double alpha = .4;
	public double alpha = Math.random()*.08+.42;

	// Values used to determine c and d
	double[] gamma = {0.11, 0.42, 0.06};
//	double[] delta = {0.49, 0.87, 0.52};
//	double[] delta_bar = {.39, .68, .39};
//	double[] gamma = {Math.random(), Math.random(), Math.random()};
	double[] delta = {Math.random()*.2+.4, Math.random()*.75+.2, Math.random()*.3+.4};
	double[] delta_bar = {Math.random(), Math.random(), Math.random()};
	
	/**
	 * Initialize the NeocognitronStructure. All values are public
	 * and can be set once instantiated.
	 */
	public NeocognitronStructure() {
		
		c = new double[numLayers][];
		d = new double[numLayers][];
		
		// Initialize Constants
		generateC();
		generateD();
	}
	
	/**
	 * Initializes C using gamma. It is a monotonically decreasing function. There
	 * is one value for each layer. c[layer][window]
	 */
	public void generateC() {
		
		// For first layer, depends on input
		c[0] = generateMonotonic(Math.random()*.75, sWindowSize[0], 1, true);
		for (int l = 1; l < numLayers; l++) {
			c[l] = generateMonotonic(gamma[l], sWindowSize[l], numCPlanes[l-1], true);
		}
	}
	
	/**
	 * Initializes D using delta and delta bar. It is a monotonically decreasing
	 * function. There is one value for each layer. d[layer][window]
	 */
	public void generateD() {
		
		// For first layer, depends on input
		for (int l = 0; l < numLayers; l++) {
			d[l] = generateMonotonic(delta[l], cWindowSize[l], numSPlanes[l], false);
			for (int w = 0; w < d[l].length; w++)
				d[l][w] = d[l][w]*delta_bar[l];
		}
	}
	
	/**
	 * Generate a monotonically decreasing two dimensional function.
	 * 
	 * @param base		Base value for function
	 * @param size		Size of window used
	 * @param planes	Number of planes, used for normalizing
	 * @param norm		Normalize the output to have a sumation of 1
	 * @return			Returns the monotonic two dimensional function
	 */
	public double[] generateMonotonic(double base, int size, int planes, boolean norm) {
		double[] output = new double[(int) Math.pow(size,2)];
		Point2D center = new Point2D.Double( ((double) size - 1)/2, ((double) size - 1)/2);
		
		// Calculated each value
		int index = 0;
		for(int n = 0; n < size; n++) {
			for (int m = 0; m < size; m++) {
				output[index] = Math.pow(base, center.distance(n,m));
				index++;
			}
		}
		
		// Normalize the entire function
		if (norm) {
			double sum = 0;
			for (int w = 0; w < Math.pow(size,2); w++) {
				sum += output[w];
			}
			// Normalize with respect to # of planes
			double multiplier = 1/( (double)planes * sum);
			for (int w = 0; w < Math.pow(size,2); w++) {
				output[w] = output[w]*multiplier;
			}
		}
		return output;
	}
	
	/**
	 * Get the c weights for a specific layer
	 * 
	 * @param layer	The layer.
	 * @return		The C weights for the specific layer
	 */
	public double[] getC(int layer) {
		return c[layer];
	}

	
	/**
	 * Get the r value for a specific layer
	 * 
	 * @param layer	The layer.
	 * @return		The r value for the specific layer
	 */
	public double getR(int layer) {
		return r[layer];
	}
	
	/**
	 * A static function which allows two arrays to be multiplied
	 * together. This represents the dot product of a matrix.
	 * 
	 * @param a		Input vector a 
	 * @param b		Input vector b
	 * @return		Dot product of a*b
	 */
	public static double arrayMultiply(double[] a, double[] b) {
		// Make sure input arguments have the same length
//		if (a.length != b.length)
//			throw new IllegalArgumentException("Arrays must be the same size!");
		
		double output = 0;
		for (int i = 0; i < a.length; i++) {
			output += a[i]*b[i];
		}
		
		return output;
	}
	
	/**
	 * Reads a specific monochromatic image and returns a two dimensional
	 * representation of the image. A zero for black, and a one for all other
	 * colors.
	 * 
	 * @param file			Input file
	 * @return				Resulting double used as an input to the neocognitron.
	 * @throws IOException
	 */
	public static double[][] readImage(File file) throws IOException {
		// Read the file
		BufferedImage img;
		img = ImageIO.read(file);
		
		Color c;
		double[][] output = new double[img.getHeight()][img.getWidth()];
		
		// Loop through every pixel
		for( int x = 0; x < img.getHeight(); x++) {
			for (int y = 0; y < img.getWidth(); y++) {
				c = new Color(img.getRGB(x, y));
				// If the pixel is not black, then it must be white!
				if ( (c.getBlue()+c.getRed()+c.getGreen()) == 0)
					output[x][y] = 1;
				else
					output[x][y] = 0;
			}
		}
		
		return output;
	}
}
