/**
 * 
 */
package neocognitron;

/**
 * 
 * @author Nicholas
 *
 */
public class Neocognitron {

	// Contains all physical structure information and constants
	private NeocognitronStructure s;
	// Defaults for structure s
	//   inputLayerSize = 16;
	//   numLayers = 3;
	//   numSPlanes = {8, 8, 8};
	//   numCPlanes = {8, 8, 8};
	//   sLayerSizes = {16, 8, 4};
	//   cLayerSizes = {12, 6, 1};
	//   sWindowSize = {5, 5, 3};
	//   cWindowSize = {5, 3, 4};

	// An array of each c and s layers
	private CLayer[] cLayers;
	private SLayer[] sLayers;

	/**
	 * Initialize the neocognitron using the structure dictated in the
	 * NeocognitronStructure class
	 * 
	 * @param initStruct
	 *            Initial structure and constants for the new necognitron
	 */
	public Neocognitron(NeocognitronStructure initStruct) {
		// Set the class's structure
		s = initStruct;
		
		sLayers = new SLayer[s.numLayers];
		cLayers = new CLayer[s.numLayers];

		// Initialize all the needed neural net layers
		for (int l = 0; l < s.numLayers; l++) {
			sLayers[l] = new SLayer(l, s); //s.numSPlanes[l], s.sLayerSizes[l],
					//s.sWindowSize[l], initStruct.getR(l), initStruct.getC(l));
			cLayers[l] = new CLayer(l, s);
		}
	}

	/**
	 * Given an input matrix (character image), the neural network determined
	 * which character the image contains.
	 * 
	 * @param input	A square image containing the character to be recognized
	 * @return		The integer representation of the recognized character
	 */
	public int propagate(double[][] input, boolean train) {

		// If the input matrix is not the correct size, throw an error 
		if (!(input.length == input[0].length && input.length == s.inputLayerSize))
			throw new IllegalArgumentException(
					"Input matrix is not the correct size!");

		// Initialize output class with the input matrix
		OutputConnections output = new OutputConnections(1, s.inputLayerSize);
		output.setPlaneOutput(0, input);

		// Propagate the input through the matrix, layer by layer
		for (int l = 0; l < s.numLayers; l++) {
			//System.out.println("Starting propagation through S-layer " + l + "...");
			output = sLayers[l].propagate(output, train);
			//System.out.println(output.toString());

			//System.out.println("Starting propagation through C-layer " + l + "...");
			output = cLayers[l].propagate(output);
			//System.out.println(output.toString());
			
			System.out.println("\nDone with input layer " + l + ".\n");
		}
		System.out.println(output.toString());

		// Determine the output from the final layer
		return determineOutput(output.getPointsOnPlanes(0, 0));
	}

	/**
	 * TODO Actually figure out how determineOutput works!
	 * 
	 * @param out
	 * @return
	 */
	public int determineOutput(double[] out) {
		double maxValue = out[0];
		int index = 0;
		for (int i = 0; i < out.length; i++) {
			if (out[i] > maxValue) {
				maxValue = out[i];
				index = i;
			}
		}
		return index;
	}

}
