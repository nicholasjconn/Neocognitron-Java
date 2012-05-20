package neocognitron;

import java.awt.Point;
//import java.text.DecimalFormat;
import java.io.Serializable;

/**
 * The SLayer object contains all the s-cells in each s-plane within a given
 * s-layer. The s-layer receives an input OutputConnection object from a
 * previous layer, and outputs an OutputConnection object. The object is
 * also serializable so that it can be saved for further use.
 * 
 * @author Nicholas J. Conn
 *
 */
public class SLayer implements Serializable {

	// UID number used for object serialization
	private static final long serialVersionUID = 7536521085321150126L;

	// Structural values saved for speedy access
	private int planes;
	private int size;
	private int windowSize;
	private int columnSize;

	// All the layer's s-cells, organized as sCell[plane][n][m] 
	private SCell[][][] sCells;
	// The single v-plane of v-cells, organized as vcCells[n][m]
	private VSCell[][] vsCells;
	
	// Learning constant q
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
	
	/**
	 * Constructor for SLayer. Is initialized to be a specific layer, using
	 * a NeocognitronStructure object.
	 * 
	 * @param layer		Layer used to access values from the NeocognitronStucture
	 * @param s			NeocognitronStructure used to dictate the structure of the layer
	 */
	public SLayer(int layer, NeocognitronStructure s) {//, int numPlanes, int initSize, int inputWindowSize, double r, double[] c) {
	
		// Initial values
		size = s.sLayerSizes[layer];
		planes = s.numSPlanes[layer];
		windowSize = s.sWindowSize[layer];
		columnSize = s.sColumnSize[layer];
		
		sCells = new SCell[planes][size][size];
		vsCells = new VSCell[size][size];
		
		q = s.q[layer];
		
		// Determine number of planes in previous c-layer
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
	
	/**
	 * Initializes each a weight matrix for a[planes][c-planes][window]
	 * 
	 * @param previousPlanes	Number of planes in the previous c-layer
	 */
	public void InitializeA(int previousPlanes) {
		a = new double[planes][previousPlanes][(int)Math.pow(windowSize,2)];
		
		for (int k = 0; k < planes; k++) {
			for (int ck = 0; ck < previousPlanes; ck++) {
				for (int w = 0; w < Math.pow(windowSize, 2); w++ ) {
						a[k][ck][w] = Math.random()*.5;
				}
			}
		}
	}
	
	/**
	 * Initialize each b weight to zero
	 */
	public void InitializeB() {
		b = new double[planes];
		for (int k = 0; k < planes; k++) {
			b[k] = 0;
		}
	}
	
	/**
	 * Initialize each s-cell.
	 * 
	 * @param r		Constant r
	 */
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
	
	/**
	 * For a given input, determine the output for this layer. The input
	 * and output object are both OutputConnections. This method is also
	 * used to train the Neocognitron.
	 * 
	 * @param input	A square image containing the character to be recognized.
	 * @param train	A boolean value which determines if the layer should be
	 * 				trained or not
	 * @return		The output of the layer.
	 */
	public OutputConnections propagate(OutputConnections input, boolean train) {

		// Initialize output object
		OutputConnections output = new OutputConnections(planes, size);
		
		double[][] windowsFromEachPlane;
		double[][] vOutput = new double[size][size];
		double value;

		// For every cell location in each plane, propagate the input 
		for (int n = 0 ; n < size; n++) {
			for (int m = 0; m < size; m++) {
				// Get the window array for a specific location (n,m).
				windowsFromEachPlane = input.getWindows(n, m, windowSize);

				// Determine v-cell output for specific location
				vOutput[n][m] = vsCells[n][m].propagate(windowsFromEachPlane);
				
				// Cycle through each plane and determine the output for a specific location (n,m)
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
	
	/**
	 * Train the s-layer. Modifies the weights based on the input and output of the layer.
	 * 
	 * @param input		Input to the layer
	 * @param output	Output for the given input
	 * @param vOutput	v-plane output for the given input
	 */
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
	}
}
