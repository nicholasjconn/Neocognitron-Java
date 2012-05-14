package neocognitron;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NeocognitronTrainer {
	
	ArrayList<double[][]> inputs;
	ArrayList<double[][]> testInputs;
	File neoFile;

	public NeocognitronTrainer(ArrayList<double[][]> i, ArrayList<double[][]> t, File outFile) {
		inputs = i;
		testInputs = t;
		neoFile = outFile;
	}
		
	public Neocognitron runTrainingSet(int loops) {
		Neocognitron output = new Neocognitron(new NeocognitronStructure());

		for (int n = 0; n < loops*inputs.size(); n++) {
			//output.propagate(inputs.get(n % inputs.size()), true);
			output.propagate(inputs.get((int)Math.round(Math.random()*(inputs.size()-1))), true);
		}		
		return output;
	}
	
	public boolean verifyTraining(Neocognitron n) {

		List<Integer> outLoc = new ArrayList<Integer>();
		int output;
		//System.out.println("Character vs Output");
		for (int i = 0; i < inputs.size(); i++) {
			output = n.propagate(inputs.get(i), false);
			
			//System.out.println(i + "\t" + output);
			
			if (outLoc.contains(output) || output == -1) {
				return false;
			}
			outLoc.add(output);
		}		
		return true;
	}
	
	public Neocognitron getNeocognitron(int trainingLoops) {
		Neocognitron output;
		
		int count = 0;
		double errorRate = 1;
		double bestError = 1;
		System.out.println("Starting Training");
		do {
			do {
				output = runTrainingSet(trainingLoops);
				count++;
				System.out.println("Loop: " + count + "\t\tBest: " + bestError + "\t\tCurrent: " + errorRate);
			} while(!verifyTraining(output));
			errorRate = verifyNeocognitron(output,testInputs);
			if (errorRate < bestError) {
				Neocognitron.SaveNeocognitron(output, neoFile);
				bestError = errorRate;
			}
		}while( errorRate != 0) ;
		
		return output;
	}

	public double verifyNeocognitron(Neocognitron n, ArrayList<double[][]> t) {
		if (testInputs.size() % inputs.size() != 0) {
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
