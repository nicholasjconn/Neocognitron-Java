/**
 * 
 */
package neocognitron;

/**
 * @author Nicholas
 *
 */
public class RunNeocognitrionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		PrintLine("Starting Neocognitron!");
		PrintLine();

		NeocognitronStructure s = new NeocognitronStructure();
		PrintLine("Input Layer Size: " + s.inputLayerSize + "x" + s.inputLayerSize);
		PrintLine("Number of Layers: " + s.numLayers);
		
		Neocognitron neoNet = new Neocognitron(s);
		PrintLine("Neocognitron created successfully!");
		
		PrintLine();
		
		PrintLine("Attempting to propigate input signal...");
		
		double[][] input = generateInputMatrix(s.inputLayerSize);
		neoNet.propagate(input);
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
