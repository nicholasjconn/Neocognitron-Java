package neocognitron;

import java.awt.Point;
//import java.text.DecimalFormat;
import java.io.Serializable;

/**
 * 
 * @author Nicholas
 *
 */
public class SLayer implements Serializable {
	
	private static final long serialVersionUID = 7536521085321150126L;
	
	private int planes;
	private int size;
	private int windowSize;
	private int columnSize;
	
	private SCell[][][] sCells;
	private VSCell[][] vsCells;
	
	private double q; 
	
	// For every plane, there exists a b[k]
	private double[] b;
	
	// For every plane, there exists an a[k][ck][v]
	// where k is the location in the s plane
	// ck, is the location of the incoming window from the c plane
	// and v is the window
	private double[][][] a;
	
	// Weights for v-cells c[window]
	private double[] c;
	
	
	public SLayer(int layer, NeocognitronStructure s) {//, int numPlanes, int initSize, int inputWindowSize, double r, double[] c) {
		
		if (s.c[layer].length != Math.pow(s.sWindowSize[layer], 2) )
			throw new IllegalArgumentException("The number of weights in array c is incorrect!");
		
		size = s.sLayerSizes[layer];//initSize;
		planes = s.numSPlanes[layer]; //numPlanes;
		windowSize = s.sWindowSize[layer]; //inputWindowSize;
		columnSize = s.sColumnSize[layer];
		
		sCells = new SCell[planes][size][size];
		vsCells = new VSCell[size][size];
		
		q = s.q[layer];
		
		int previousPlanes;
		if (layer == 0)
			previousPlanes = 1;
		else
			previousPlanes = s.numCPlanes[layer-1];
		
		c = s.c[layer];
		
		InitializeA(previousPlanes);
		InitializeB();
		InitializeCells(s.r[layer]);
	}
	
	public void InitializeA(int previousPlanes) {
		// TODO initialize random values or whatever is needed
		// for now, generate all as random
		a = new double[planes][previousPlanes][(int)Math.pow(windowSize,2)];
		
		for (int k = 0; k < planes; k++) {
			for (int ck = 0; ck < previousPlanes; ck++) {
				for (int w = 0; w < Math.pow(windowSize, 2); w++ ) {
					//if (Math.random() < 1)
						a[k][ck][w] = Math.random();
				}
				//a[k][ck] = c;
			}
		}
	}
	
	public void InitializeB() {
		// TODO initialize random values or whatever is needed
		// for now, generate all 1s
		b = new double[planes];
		for (int k = 0; k < planes; k++) {
			b[k] = 0;
		}
	}
	
	public void InitializeCells(double r) {
		for (int n = 0 ; n < size; n++) {
			for (int m = 0; m < size; m++) {
				vsCells[n][m] = new VSCell(c);
				for (int k = 0; k < planes; k++) {
					sCells[k][n][m] = new SCell(r);
				}
			}
		}
	}
	
	public OutputConnections propagate(OutputConnections input, boolean train) {
		
		OutputConnections output = new OutputConnections(planes, size);
		
		double[][] windowsFromEachPlane;
		double[][] vOutput = new double[size][size];
		double value;
		
		for (int n = 0 ; n < size; n++) {
			for (int m = 0; m < size; m++) {				
				windowsFromEachPlane = input.getWindows(n, m, windowSize);
				
				vOutput[n][m] = vsCells[n][m].propagate(windowsFromEachPlane);
				
				for (int k = 0; k < planes; k++) {
					value = sCells[k][n][m].propagate(windowsFromEachPlane, vOutput[n][m], b[k], a[k] );
					output.setSingleOutput(k, n, m, value);
				}
			}
		}
		
		if (train) {
			train(input, output, vOutput);
			output = this.propagate(input, false);
		}
		
		return output;
	}
	
	public void train(OutputConnections input, OutputConnections output, double[][] vOutput) {
		//DecimalFormat df = new DecimalFormat("#.00E0");
		
		//System.out.println("vOutput:\n" + OutputConnections.arrayToString(vOutput));

		// Determine length of the weight array that will be changed (for each window)
		int weightLength = (int) Math.pow(windowSize,2);
		double delta;
		
		// Get the representative cell locations from the output
		Point[] repLoc = output.getRepresentativeCells(columnSize);
		
		// For every plane in this specific S-layer
		for (int k = 0; k < planes; k++) {
			// As long as there is a representative cell, update the plane weights
			if (repLoc[k] != null) {
				// Get specific representative location
				Point p = repLoc[k]; 

				// Update b weights, one value for each plane (not dependent on (n,m) )
				delta = q/2 * vOutput[p.x][p.y];
				b[k] += delta;
				//System.out.println("\nChange in b weights for plane " + k + ": " + df.format(delta) );
				
				// Loop for every plane in the input (from the previous C-layer) 
				for (int ck = 0; ck < a[k].length; ck++) {
					// Get the output for the previous C-layer (input for this layer)
					double[] in = input.getWindowInPlane(ck, p.x, p.y, windowSize);
					
					//System.out.print("Changes in weights for a in plane " + k + " and input plane " + ck + ":\t");
										
					// Loop through every weight a[k][ck][window] in the given window 
					for(int w = 0; w < weightLength; w++) {
						delta = q * c[w] * in[w];
						a[k][ck][w] += delta;
						//System.out.print(df.format(delta) + "\t");
					}
				}				
			}
		}
		
//		System.out.print("\nCurrent values of b weights: ");
//		for (int k = 0; k < planes; k++) {
//			System.out.print(b[k] + "\t");
//		}
//		System.out.println();
//		
//		System.out.println("Current values for a weights:");
//		for (int k = 0; k < planes; k++) {
//			System.out.print(OutputConnections.arrayToString(a[k]));
//			System.out.println();
//		}
	}
	
}
