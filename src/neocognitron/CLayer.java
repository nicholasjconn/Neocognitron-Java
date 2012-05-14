/**
 * 
 */
package neocognitron;

import java.io.Serializable;

/**
 * @author Nicholas
 *
 */
public class CLayer implements Serializable {
	
	private static final long serialVersionUID = 7536521085321150123L;

	private int planes;
	private int size;
	private int windowSize;

	private CCell[][][] cCells;
	private VCCell[][] vcCells;
	
	
	
	public CLayer(int layer, NeocognitronStructure s) {
		
		size = s.cLayerSizes[layer];
		planes = s.numCPlanes[layer];
		windowSize = s.cWindowSize[layer];
		
		cCells = new CCell[planes][size][size];
		vcCells = new VCCell[size][size];
		
		InitializeCells(s.d[layer], s.alpha);
		
	}
	
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
	
	public OutputConnections propagate(OutputConnections inputs) {
		
		OutputConnections output = new OutputConnections(planes,size);
		
		double[][] windowInEachPlane;
		double vOutput;
		double value;
		
		for (int n = 0; n < size; n++) {
			for (int m = 0; m < size; m++) {
				windowInEachPlane = inputs.getWindows(n, m, windowSize);
				
				vOutput = vcCells[n][m].propagate(windowInEachPlane);
				for (int k = 0; k < planes; k++) {
					value = cCells[k][n][m].propigate(windowInEachPlane[k], vOutput);
					output.setSingleOutput(k, n, m, value);
				}
			}
		}

		return output;
	}
	
}
