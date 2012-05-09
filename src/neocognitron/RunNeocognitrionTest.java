/**
 * 
 */
package neocognitron;

import java.io.File;
import java.io.IOException;
//import java.util.Random;

/**
 * @author Nicholas
 *
 */
public class RunNeocognitrionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		PrintLine("Starting Neocognitron testing...");
		PrintLine();

		NeocognitronStructure s = new NeocognitronStructure();
		PrintLine("Input Layer Size: " + s.inputLayerSize + "x" + s.inputLayerSize);
		PrintLine("Number of Layers: " + s.numLayers);
		
		Neocognitron neoNet = new Neocognitron(s);
		PrintLine("Neocognitron created successfully!");
		
		File files[] = new File[5];
		files[0] = new File ("data\\Training Images\\0_00.bmp");
		files[1] = new File ("data\\Training Images\\1_00.bmp");
		files[2] = new File ("data\\Training Images\\2_00.bmp");
		files[3] = new File ("data\\Training Images\\3_00.bmp");
		files[4] = new File ("data\\Training Images\\4_00.bmp");
		PrintLine("\nReading input files now...");

		double[][] input;
		//Random r = new Random();

		int loops = 100;
		int[] inNumber = new int[loops];
		int[] outLoc = new int[loops];
		for (int n = 0; n < loops; n++) {
			try {
				//int roll = r.nextInt(5);
				int roll = n%5;
				inNumber[n] = roll;
				input = NeocognitronStructure.readImage(files[roll]);
			}
			catch (IOException e) {
				PrintLine("\nERROR!");
				return;
			}
			
			PrintLine("\nAttempting to propigate input signal...");
		
			outLoc[n] = neoNet.propagate(input, true);
		}
		
		
		PrintLine("\nInput number vs output location");
		for (int n = 0; n < loops; n++) {
			PrintLine(inNumber[n] + "\t" + outLoc[n]);
		}
	}
	
	public static double[][] generateInputMatrix(int size) {
		double[][] output = new double[size][size];
		
		for (int n = 0; n < size; n++) {
			for (int m = 0; m < size; m++ ) {
				output[n][m] = n*m % 2;
			}
		}
		
		return output;
	}
	
	public static void PrintLine() {
		System.out.println();
	}
	
	public static void PrintLine(String s) {
		System.out.println(s);
	}

}
