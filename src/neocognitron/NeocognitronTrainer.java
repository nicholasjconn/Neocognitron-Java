package neocognitron;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a helper class which is used to train a Neocognitron. By using ArrayLists
 * of inputs, a neocognitron can be trained and verified. This also contains the
 * methods for determining the error rate of the network.
 * 
 * @author Nicholas J. Conn
 *
 */
public class NeocognitronTrainer {
	
	// Input images used for training and verification
	ArrayList<double[][]> inputs;
	ArrayList<double[][]> testInputs;
	// File to save the most successful neocognitron during training
	File neoFile;

	/**
	 * Initialize the neocognitron trainer with a set of input files, training
	 * files, and the output file for the best neocognitron
	 * 
	 * @param i			Input images for training
	 * @param t			Input images used for verification
	 * @param outFile	File where the Neocognitron is saved
	 */
	public NeocognitronTrainer(ArrayList<double[][]> i, ArrayList<double[][]> t, File outFile) {
		inputs = i;
		testInputs = t;
		neoFile = outFile;
	}
	
	/**
	 * Train the neocognitron using a specific number of loops. Using
	 * a specific training set of images.
	 * 
	 * @param loops	Number of times each image is presented to the network
	 * @return		The trained neocognitron
	 */
	public Neocognitron runTrainingSet(int loops) {
		Neocognitron output = new Neocognitron(new NeocognitronStructure());

		for (int n = 0; n < loops*inputs.size(); n++) {
			output.propagate(inputs.get(n % inputs.size()), true);
			//output.propagate(inputs.get((int)Math.round(Math.random()*(inputs.size()-1))), true);
		}		
		return output;
	}
	
	/**
	 * Determine if training was successful. The neocognitron fails when two characters
	 * return the same result, or if there is no output for any character.
	 * 
	 * @param n		Neocognitron to be verified
	 * @return		true if verified, false if not verified
	 */
	public boolean verifyTraining(Neocognitron n) {

		List<Integer> outLoc = new ArrayList<Integer>();
		int output;
		//System.out.println("Character vs Output");
		for (int i = 0; i < inputs.size(); i++) {
			output = n.propagate(inputs.get(i), false);
			
			//System.out.println(i + "\t" + output);
			
			// If output is already been used, or there is no output
			if (outLoc.contains(output) || output == -1) {
				return false;
			}
			outLoc.add(output);
		}		
		return true;
	}
	
	/**
	 * Get the final neocognitron. Training it with a specific number of loops. The
	 * method does not return a neocognitron until training has been successful.
	 * 
	 * @param trainingLoops		Loops used during training.
	 * @return					Resulting Neocognitron.
	 */
	public Neocognitron getNeocognitron(int trainingLoops) {
		Neocognitron output;
		
		// Initialize Values
		int count = 0;
		double errorRate = 1;
		double bestError = 1;
		//System.out.println("Starting Training");
		do {		// While the error rate is not zero
			do {	// and while the training is not successful
				output = runTrainingSet(trainingLoops);
				count++;
				System.out.print("Loop: " + count + "\t\tBest: " + bestError + "\t\tCurrent: " + errorRate + "\r");
			} while(!verifyTraining(output));
			errorRate = verifyNeocognitron(output,testInputs);
			if (errorRate < bestError) {
				Neocognitron.SaveNeocognitron(output, neoFile);
				bestError = errorRate;
			}
		}while( errorRate != 0) ;
		
		return output;
	}
	
	/**
	 * Calculate the error rate of a specific neocognitron.
	 * 
	 * @param n		The neocognitron under test.
	 * @return		The resulting error rate.
	 */
	public double verifyNeocognitron(Neocognitron n) {
		return verifyNeocognitron(n, testInputs);
	}

	/**
	 * Calculate the error rate of a specific neocognitron using a specific
	 * list of inputs.
	 * 
	 * @param n		The neocognitron under test
	 * @param t		The list of files used to verify the network
	 * @return		The resulting error rate
	 */
	public double verifyNeocognitron(Neocognitron n, ArrayList<double[][]> t) {
		if (t.size() % inputs.size() != 0) {
			throw new IllegalArgumentException();
		}
		
		double output = 0;
		
		int trainingOutput, testOutput;
		System.out.println("Training vs Test");
		for (int i = 0; i < t.size(); i++) {
			trainingOutput = n.propagate(inputs.get(i%inputs.size()), false);
			testOutput = n.propagate(t.get(i), false);

			System.out.println(trainingOutput + "\t" + testOutput);
			
			if (trainingOutput !=  testOutput)
				output++;
		}
		return output/t.size();
	}
}
