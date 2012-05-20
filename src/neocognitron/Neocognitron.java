/**
 * 
 */
package neocognitron;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The neocognitron object contains all needed to train and propagate an input
 * image. The structure of the network is defined by a NeocognitronStructure
 * object. Additionally, the neocognitron is serializable so that it can be
 * saved for further use. The Neocognitron contains the methods needed to load
 * and save a neocognitron. 
 * 
 * @author Nicholas J. Conn
 *
 */
public class Neocognitron implements Serializable {

	// UID number used for object serialization
	private static final long serialVersionUID = 7536521085321150121L;

	// Contains all physical structure information and constants
	private NeocognitronStructure s;

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
	 * Get the structure used to initialize the neocognitron
	 * 
	 * @return	NeocognitronStructure object for the Neocognitron
	 */
	public NeocognitronStructure getStructure() {
		return s;
	}

	/**
	 * Given an input matrix (character image), the neural network determined
	 * which character the image represents. This method is also used to train
	 * the Neocognitron.
	 * 
	 * @param input	A square image containing the character to be recognized.
	 * @param train	A boolean value which determines if the network should
	 * 				be trained or not
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
		//System.out.println(output.toString());

		// Propagate the input through the matrix, layer by layer
		for (int l = 0; l < s.numLayers; l++) {
			//System.out.println("Starting propagation through S-layer " + l + "...");
			output = sLayers[l].propagate(output, train);
			//System.out.println(output.toString());

			//System.out.println("Starting propagation through C-layer " + l + "...");
			output = cLayers[l].propagate(output);
			//System.out.println(output.toString());
			
			//System.out.println("\nDone with input layer " + l + ".\n");
		}
		//System.out.println(output.toString());

		// Determine the output from the final layer
		return determineOutput(output.getPointsOnPlanes(0, 0));
	}

	/**
	 * Given the output from the final layer, determine the output of
	 * the network. The output is an integer which ranges across all
	 * possible outputs.
	 * 
	 * @param out	Output from the last layer in the neocognitron.
	 * @return		The index of the maximum output in the last layer.
	 */
	public int determineOutput(double[] out) {
		double maxValue = 0;
		int index = -1;
		for (int i = 0; i < out.length; i++) {
			if (out[i] > maxValue) {
				maxValue = out[i];
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * Serialize the neocognitron and save it to a specific file. This
	 * method is static and can be used without creating a specific
	 * instance of the Neocognitron.
	 * 
	 * @param n		Neocognitron object to be saved
	 * @param f		Location of the resulting neocognitron.
	 */
	public static void SaveNeocognitron(Neocognitron n, File f) {
	
		try {
			// Write to disk with FileOutputStream
			FileOutputStream f_out = new 
				FileOutputStream(f);
		
			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new
				ObjectOutputStream (f_out);
		
			// Write object out to disk
			obj_out.writeObject ( n );
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Open a previously saved neocognitron file and returns the 
	 * original Neocognitron.
	 * 
	 * @param f		File which contains the neocognitron
	 * @return		The loaded neocognitron
	 */
	public static Neocognitron OpenNeocognitron(File f) {
		// Initialize the output, if not successfully open, returns null.
		Neocognitron output = null;
		
		try {
			// Read from disk using FileInputStream			
			FileInputStream f_in = new 
				FileInputStream(f);
	
			// Read object using ObjectInputStream
			ObjectInputStream obj_in = 
				new ObjectInputStream (f_in);
	
			// Read an object
			Object obj = obj_in.readObject();
	
			// Determine if the object is a neocognitron
			if (obj instanceof Neocognitron)
			{
				// Cast object as a Neocognitron
				output = (Neocognitron) obj;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return output;
	}
}
