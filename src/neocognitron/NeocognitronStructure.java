package neocognitron;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

public class NeocognitronStructure implements Serializable {
	
	private static final long serialVersionUID = 7536521085321150122L;
	
	public File logFile = new File ("data\\log\\log.txt");
	public FileWriter out;

	public int inputLayerSize = 16;
	public int numLayers = 3;
	private int p = (int) Math.round(Math.random()*15+5);
	public int[] numSPlanes = {p, p, p};
	public int[] numCPlanes = {p, p, p};
	public int[] sLayerSizes = {16, 8, 2};
	public int[] cLayerSizes = {10, 6, 1};
	public int[] sWindowSize = {5, 5, 5};
	public int[] cWindowSize = {5, 5, 2};
	public int[] sColumnSize = {5, 5, 2};
	
	//public double[] r = {4, 1.5, 1.5};
	public double[] r = {Math.random()*4+2, Math.random()*2+1, Math.random()*2+1};
	public double[][] c;
	
	//public double[] q = {.1, 16, 16};
	public double[] q = {Math.random()*.5, Math.random()*32, Math.random()*32};

	public double[][] d;
	//public double alpha = .4;
	public double alpha = Math.random()/5+.4;
	
//	double[] gamma = {.77, .8, .84};
//	double[] delta = {.34, .25, .44};
//	double[] delta_bar = {.39, .68, .39};
	
	double[] gamma = {Math.random(), Math.random(), Math.random()};
	double[] delta = {Math.random(), Math.random(), Math.random()};
	double[] delta_bar = {Math.random(), Math.random(), Math.random()};
	
	
	public NeocognitronStructure() {
		
		c = new double[numLayers][];
		d = new double[numLayers][];
		
		generateC();
		generateD();
		
	}
	
	// C is a monotonic decreasing function
	// an array for each layer
	// c[l][window]
	// for each s sub-layer
	public void generateC() {
		
		// For first layer, depends on input
		c[0] = generateMonotonic(Math.random()*.5+.5, sWindowSize[0], 1, true);
		for (int l = 1; l < numLayers; l++) {
			c[l] = generateMonotonic(gamma[l], sWindowSize[l], numCPlanes[l-1], true);
		}
	}

	
	// D is a monotonic decreasing function
	// an array for each layer
	// d[l][window]
	// for each c sub-layer
	public void generateD() {
		
		// For first layer, depends on input
		for (int l = 0; l < numLayers; l++) {
			d[l] = generateMonotonic(delta[l], cWindowSize[l], numSPlanes[l], false);
			for (int w = 0; w < d[l].length; w++)
				d[l][w] = d[l][w]*delta_bar[l];
		}
	}
	
	public double[] generateMonotonic(double base, int size, int planes, boolean norm) {
		double[] output = new double[(int) Math.pow(size,2)];
		Point2D center = new Point2D.Double( ((double) size - 1)/2, ((double) size - 1)/2);
		
		// TODO create basis monotonic function using constants shown in new paper
		int index = 0;
		for(int n = 0; n < size; n++) {
			for (int m = 0; m < size; m++) {
				output[index] = Math.pow(base, center.distance(n,m));
				index++;
			}
		}
		
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
	
	public double[] getC(int layer) {
		return c[layer];
	}
	
	public double getR(int layer) {
		return r[layer];
	}
	
	public static double arrayMultiply(double[] a, double[] b) {
		if (a.length != b.length)
			throw new IllegalArgumentException("Arrays must be the same size!");
		
		double output = 0;
		for (int i = 0; i < a.length; i++) {
			output += a[i]*b[i];
		}
		
		return output;
	}
	
	public static double[][] readImage(File file) throws IOException {
		BufferedImage img;
		img = ImageIO.read(file);
		Color c;
		double[][] output = new double[img.getHeight()][img.getWidth()];
		for( int x = 0; x < img.getHeight(); x++) {
			for (int y = 0; y < img.getWidth(); y++) {
				c = new Color(img.getRGB(x, y));
				if ( (c.getBlue()+c.getRed()+c.getGreen()) == 0)
					output[x][y] = 1;
				else
					output[x][y] = 0;
				//System.out.print((int)output[x][y]);
			}
			//System.out.println();
		}
		
		return output;
	}
}
