package neocognitron;

/**
 * 
 * @author Nicholas
 *
 */
public class SLayer {
	
	private int planes;
	private int size;
	private int windowSize;
	
	private SCell[][][] sCells;
	private VSCell[][] vsCells;
	
	// For every plane, there exists a b[k]
	private double[] b;
	
	// For every plane, there exists an a[k][ck][v]
	// where k is the location in the s plane
	// ck, is the location of the incoming window from the c plane
	// and v is the window
	private double[][][] a;
	
	
	public SLayer(int layer, NeocognitronStructure s) {//, int numPlanes, int initSize, int inputWindowSize, double r, double[] c) {
		
		if (s.c[layer].length != Math.pow(s.sWindowSize[layer], 2) )
			throw new IllegalArgumentException("The number of weights in array c is incorrect!");
		
		size = s.sLayerSizes[layer];//initSize;
		planes = s.numSPlanes[layer]; //numPlanes;
		windowSize = s.sWindowSize[layer]; //inputWindowSize;
		
		sCells = new SCell[planes][size][size];
		vsCells = new VSCell[size][size];
		
		int previousPlanes;
		if (layer == 0)
			previousPlanes = 1;
		else
			previousPlanes = s.numCPlanes[layer-1];
		
		InitializeA(previousPlanes);
		InitializeB();
		InitializeCells(s.r[layer], s.c[layer]);
	}
	
	public void InitializeA(int previousPlanes) {
		// TODO initialize random values or whatever is needed
		// for now, generate all as 1
		a = new double[planes][previousPlanes][(int)Math.pow(windowSize,2)];
		for (int k = 0; k < planes; k++) {
			for (int ck = 0; ck < previousPlanes; ck++) {
				for (int w = 0; w < Math.pow(windowSize, 2); w++ ) {
					a[k][ck][w] = .1;
				}
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
	
	public void InitializeCells(double r, double[] c) {
		for (int n = 0 ; n < size; n++) {
			for (int m = 0; m < size; m++) {
				vsCells[n][m] = new VSCell(c);
				for (int k = 0; k < planes; k++) {
					sCells[k][n][m] = new SCell(r);
				}
			}
		}
	}
	
	public OutputConnections propagate(OutputConnections input) {
		
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
		
		return output;
	}
	
}
