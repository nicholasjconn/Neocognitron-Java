/**
 * 
 */
package neocognitron;

import java.io.Serializable;

/**
 * The CLayer object contains all the c-cells in each c-plane within a given
 * c-layer. The c-layer receives an input OutputConnection object from a
 * previous layer, and outputs an OutputConnection object. The object is
 * also serializable so that it can be saved for further use.
 * 
 * @author Nicholas J. Conn
 *
 */
public class CLayer implements Serializable {

	// UID number used for object serialization
	private static final long serialVersionUID = 7536521085321150123L;

	// Structural values saved for speedy access
	private int planes;
	private int size;
	private int windowSize;

	// All the layer's c-cells, organized as cCell[plane][n][m] 
	private CCell[][][] cCells;
	// The single v-plane of v-cells, organized as vcCells[n][m]
	private VCCell[][] vcCells;
	
	/**
	 * Constructor for CLayer. Is initialized to be a specific layer, using
	 * a NeocognitronStructure object.  
	 * 	
	 * @param layer	Layer used to access values from the NeocognitronStucture
	 * @param s		NeocognitronStructure used to dictate the structure of the layer
	 */
	public CLayer(int layer, NeocognitronStructure s) {
		
		// Initialize values
		size = s.cLayerSizes[layer];
		planes = s.numCPlanes[layer];
		windowSize = s.cWindowSize[layer];
		
		cCells = new CCell[planes][size][size];
		vcCells = new VCCell[size][size];
		
		InitializeCells(s.d[layer], s.alpha);
		
	}
	
	/**
	 * Initializes each c-cell.
	 * 
	 * @param d		Initial d weight values
	 * @param alpha	Constant alpha
	 */
	public void InitializeCells(double[] d, double alpha) {
		for (int n = 0 ; n < size; n++) {
			for (int m = 0; m < size; m++) {
				
				vcCells[n][m] = new VCCell(d);
				
				for (int k = 0; k < planes; k++) {
					cCells[k][n][m] = new CCell(d, alpha);
				}
			}
		}
	}
	
	/**
	 * For a given input, determine the output for this layer. The input
	 * and output object are both OutputConnections. 
	 * 
	 * @param inputs	The input to this layer
	 * @return			The output from this layer
	 */
	public OutputConnections propagate(OutputConnections inputs) {
		
		// Initialize output object
		OutputConnections output = new OutputConnections(planes,size);
		
		double[][] windowInEachPlane;
		double vOutput;
		double value;
		
		// For every cell location in each plane, propagate the input 
		for (int n = 0; n < size; n++) {
			for (int m = 0; m < size; m++) {
				// Get the window array for a specific location (n,m).
				windowInEachPlane = inputs.getWindows(n, m, windowSize);
				
				// Determine v-cell output for specific location
				vOutput = vcCells[n][m].propagate(windowInEachPlane);
				// Cycle through each plane and determine the output for a specific location (n,m)
				for (int k = 0; k < planes; k++) {
					value = cCells[k][n][m].propagate(windowInEachPlane[k], vOutput);
					output.setSingleOutput(k, n, m, value);
				}
			}
		}

		return output;
	}
	
}
