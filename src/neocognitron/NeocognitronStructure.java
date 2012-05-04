package neocognitron;

import java.awt.Color;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

public class NeocognitronStructure {

	public int inputLayerSize = 16;
	public int numLayers = 3;
	public int[] numSPlanes = {8, 8, 8};
	public int[] numCPlanes = {8, 8, 8};
	public int[] sLayerSizes = {16, 8, 4};
	public int[] cLayerSizes = {12, 6, 1};
	public int[] sWindowSize = {5, 5, 3};
	public int[] cWindowSize = {5, 3, 4};
	
	public double[] r = {4, 1.5, 1.5};
	public double[][] c;
	

	public double[][] d;
	public double alpha = .5;
	
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
		c[0] = generateMonotonic(sWindowSize[0], 1);
		for (int l = 1; l < numLayers; l++) {
			c[l] = generateMonotonic(sWindowSize[l], numCPlanes[l-1]);
		}
	}

	
	// D is a monotonic decreasing function
	// an array for each layer
	// d[l][window]
	// for each c sub-layer
	public void generateD() {
		
		// For first layer, depends on input
		for (int l = 0; l < numLayers; l++) {
			d[l] = generateMonotonic(cWindowSize[l], numSPlanes[l]);
		}
	}
		
	public double[] generateMonotonic(int size, int planes) {
		double[] output = new double[(int) Math.pow(size,2)];
		
		// TODO create basis monotonic function, for now just create 1s everywhere
		for(int w = 0; w < Math.pow(size,2); w++) {
			output[w] = 1;
		}
		
		double sum = 0;
		for (int w = 0; w < Math.pow(size,2); w++) {
			sum += output[w];
		}
		// Normalize with respect to # of planes
		double norm = 1/( (double)planes * sum);
		for (int w = 0; w < Math.pow(size,2); w++) {
			output[w] = output[w]*norm;
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
				System.out.print((int)output[x][y]);
			}
			System.out.println();
		}
		
		return output;
	}
}
