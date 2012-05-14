package neocognitron;

import java.util.ArrayList;
import java.util.List;

public class NeocognitronTrainer {
	
	private NeocognitronStructure s;
	ArrayList<double[][]> inputs;
	ArrayList<double[][]> testInputs;

	public NeocognitronTrainer(NeocognitronStructure struct, ArrayList<double[][]> i, ArrayList<double[][]> t) {
		s = struct;
		inputs = i;
		testInputs = t;
	}
		
	public Neocognitron runTrainingSet(int loops) {
		Neocognitron output = new Neocognitron(s);

		for (int n = 0; n < loops*inputs.size(); n++) {
			output.propagate(inputs.get(n % inputs.size()), true);
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
		System.out.println("Starting Training");
		do {
			do {
				output = runTrainingSet(trainingLoops);
				count++;
				System.out.print("\r" +count);
			} while(!verifyTraining(output));
		}while(!verifyNeocognitron(output,testInputs)) ;
		
		return output;
	}

	public boolean verifyNeocognitron(Neocognitron n, ArrayList<double[][]> t) {
		if (testInputs.size() != inputs.size()) {
			throw new IllegalArgumentException();
		}
		
		boolean output = true;
		
		int trainingOutput, testOutput;
		System.out.println("Training vs Test");
		for (int i = 0; i < inputs.size(); i++) {
			trainingOutput = n.propagate(inputs.get(i), false);
			testOutput = n.propagate(t.get(i), false);

			System.out.println(trainingOutput + "\t" + testOutput);
			
			if (trainingOutput !=  testOutput)
				output = false;
		}
		return output;
	}
}
