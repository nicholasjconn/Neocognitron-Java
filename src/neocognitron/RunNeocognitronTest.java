/**
 * 
 */
package neocognitron;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Nicholas
 *
 */
public class RunNeocognitronTest {

	Neocognitron neoNet;
	Neocognitron bestNet;
	NeocognitronStructure s = new NeocognitronStructure();
	ArrayList<double[][]> inputs;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		RunNeocognitronTest test = new RunNeocognitronTest();
		test.runTest();
	}
	
	public RunNeocognitronTest() {
		File files[] = new File[5];
		files[0] = new File ("data\\Training Images\\0_00.bmp");
		files[1] = new File ("data\\Training Images\\1_00.bmp");
		files[2] = new File ("data\\Training Images\\2_00.bmp");
		files[3] = new File ("data\\Training Images\\3_00.bmp");
		files[4] = new File ("data\\Training Images\\4_00.bmp");
		
		inputs = new ArrayList<double[][]>();
		for(int i = 0; i < files.length; i++ ) {
			try {
				inputs.add(NeocognitronStructure.readImage(files[i]));
			}
			catch (IOException e) {
				PrintLine("\nERROR!");
			}
		}
	}
		
	public void runTest() {
		
		PrintLine("Run till no zero outputs");
		
		int allZeros = 10;
		int bestZeros = 10000000;
		int[] outputs = null;
		int loop = 0;
		double total = 0;
		bestNet = null;
		while (allZeros > 1 ) {
			
			outputs = singleTrainingTest();
			
			allZeros= 0;
			for (int i = 0; i < outputs.length; i++) {
				if( outputs[i] == -1)
					allZeros++;
			}
			if (allZeros < bestZeros) {
				bestZeros = allZeros;
				bestNet = neoNet;
			}
			
			PrintLine("Current: " + allZeros);
			total += allZeros;

			loop++;
			PrintLine("\nLoop: " + loop);
			PrintLine("Average: " + ((int)total/loop));
			PrintLine("Best: " + bestZeros);
		}

		runAll(4);
	}
	
	public void runAll(int loops) {
		int[] inNumber = new int[loops*inputs.size()];
		int[] outLoc = new int[loops*inputs.size()];
		int roll;
		for (int n = 0; n < loops*inputs.size(); n++) {
			roll = n % inputs.size();
			//int roll = r.nextInt(5);
			inNumber[n] = roll;
			
			//PrintLine("\nAttempting to propigate input signal...");
		
			outLoc[n] = bestNet.propagate(inputs.get(roll), false);
		}

		PrintLine("\nInput number vs output location");
		for (int n = 0; n < loops*inputs.size(); n++) {
			PrintLine(inNumber[n] + "\t" + outLoc[n]);
		}
	}
	
	public int[] singleTrainingTest() {
//		PrintLine("Starting Neocognitron testing...");
//		PrintLine();

		//PrintLine("Input Layer Size: " + s.inputLayerSize + "x" + s.inputLayerSize);
		//PrintLine("Number of Layers: " + s.numLayers);
		
		neoNet = new Neocognitron(s);
//		PrintLine("Neocognitron created successfully!");
		
		//Random r = new Random();

		int loops = 100;
		int[] inNumber = new int[loops];
		int[] outLoc = new int[loops];
		int roll;
		for (int n = 0; n < loops; n++) {
			roll = n%5;
			//int roll = r.nextInt(5);
			inNumber[n] = roll;
			
			//PrintLine("\nAttempting to propigate input signal...");
		
			outLoc[n] = neoNet.propagate(inputs.get(roll), true);
		}
		
		return outLoc;
	}
		
	public static void PrintLine() {
		System.out.println();
	}
	
	public static void PrintLine(String s) {
		System.out.println(s);
	}

}
