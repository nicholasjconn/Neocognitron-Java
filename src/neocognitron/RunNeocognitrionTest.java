/**
 * 
 */
package neocognitron;

import java.io.File;
import java.io.IOException;

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
		
		File file = new File ("data\\Training Images\\0_00.bmp");
		PrintLine("\nReading input file \"" + file.getName() + "\" now...");

		double[][] input;
		try {
			input = NeocognitronStructure.readImage(file);
		}
		catch (IOException e) {
			PrintLine("\nERROR!");
			return;
		}
		
		PrintLine("\nAttempting to propigate input signal...");
		neoNet.propagate(input, false);
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
