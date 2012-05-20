package neocognitron;

import java.awt.Point;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is a static class used to run the entire neocognitron, including
 * the training and analysis.
 * 
 * @author Nicholas J. Conn
 *
 */
public class RunTest {

	public static void main ( String[] args )
	{
		System.out.println("Starting Test!");

		if (args.length != 0 && args[0].equals("verify")) {
				TestBestSaved();
		}
		else
			TrainerTest();
	}
	
	/**
	 * Read in the best networks and determine their structure
	 */
	public static void TestBestSaved() {
		File[] files = new File[5];
		files[0] = new File ("data/Training Images/0_00.bmp");
		files[1] = new File ("data/Training Images/1_00.bmp");
		files[2] = new File ("data/Training Images/2_00.bmp");
		files[3] = new File ("data/Training Images/3_00.bmp");
		files[4] = new File ("data/Training Images/4_00.bmp");
		
		ArrayList<double[][]> inputs = new ArrayList<double[][]>();
		for(int i = 0; i < files.length; i++ ) {
			try {
				inputs.add(NeocognitronStructure.readImage(files[i]));
			}
			catch (IOException e) {
				System.out.println("\nERROR!");
			}
		}
		

		files = new File[15];
		files[0] = new File ("data/Training Images/0_01.bmp");
		files[1] = new File ("data/Training Images/1_01.bmp");
		files[2] = new File ("data/Training Images/2_01.bmp");
		files[3] = new File ("data/Training Images/3_01.bmp");
		files[4] = new File ("data/Training Images/4_01.bmp");
		files[5] = new File ("data/Training Images/0_02.bmp");
		files[6] = new File ("data/Training Images/1_02.bmp");
		files[7] = new File ("data/Training Images/2_02.bmp");
		files[8] = new File ("data/Training Images/3_02.bmp");
		files[9] = new File ("data/Training Images/4_02.bmp");
		files[10] = new File ("data/Training Images/0_03.bmp");
		files[11] = new File ("data/Training Images/1_03.bmp");
		files[12] = new File ("data/Training Images/2_03.bmp");
		files[13] = new File ("data/Training Images/3_03.bmp");
		files[14] = new File ("data/Training Images/4_03.bmp");
		
		ArrayList<double[][]> testInputs = new ArrayList<double[][]>();
		for(int i = 0; i < files.length; i++ ) {
			try {
				testInputs.add(NeocognitronStructure.readImage(files[i]));
			}
			catch (IOException e) {
				System.out.println("\nERROR!");
			}
		}
		
		
		File[] faFiles = new File("data/Saved Networks").listFiles();
		for (File file : faFiles) {
			int dotPosition = file.toString().lastIndexOf(".");
			if (dotPosition != -1) {
			    if (file.toString().substring(dotPosition).equals(".neo")) {
			    	System.out.println();
			    	System.out.println(file.getAbsolutePath());
			    	Neocognitron n = Neocognitron.OpenNeocognitron(file);
			    	NeocognitronStructure s = n.getStructure();
			    	NeocognitronTrainer trainer = new NeocognitronTrainer(inputs, testInputs,null);
			    	double error = trainer.verifyNeocognitron(n,true);
			    	System.out.println("Eror Rate: " + error);
			    	System.out.println("Planes: " + s.numCPlanes[0]);
			    	System.out.println("r values: " + OutputConnections.arrayToString(s.r) );
			    	System.out.println("q values: " + OutputConnections.arrayToString(s.q) );
			    	System.out.println("alpha: " + s.alpha);
			    	System.out.println("gamma: " +  OutputConnections.arrayToString(s.gamma) );
			    	System.out.println("delta: " +  OutputConnections.arrayToString(s.delta) );
			    	System.out.println("delta_bar: " +  OutputConnections.arrayToString(s.delta_bar) );
			    }
			}
		}
	}
	
	/**
	 * Determine best neocognitron by generating many different networks.
	 */
	public static void TrainerTest() {

		File[] files = new File[5];
		files[0] = new File ("data/Training Images/0_00.bmp");
		files[1] = new File ("data/Training Images/1_00.bmp");
		files[2] = new File ("data/Training Images/2_00.bmp");
		files[3] = new File ("data/Training Images/3_00.bmp");
		files[4] = new File ("data/Training Images/4_00.bmp");
		
		ArrayList<double[][]> inputs = new ArrayList<double[][]>();
		for(int i = 0; i < files.length; i++ ) {
			try {
				inputs.add(NeocognitronStructure.readImage(files[i]));
			}
			catch (IOException e) {
				System.out.println("\nERROR!");
			}
		}
		
		files = new File[15];
		files[0] = new File ("data/Training Images/0_01.bmp");
		files[1] = new File ("data/Training Images/1_01.bmp");
		files[2] = new File ("data/Training Images/2_01.bmp");
		files[3] = new File ("data/Training Images/3_01.bmp");
		files[4] = new File ("data/Training Images/4_01.bmp");
		files[5] = new File ("data/Training Images/0_02.bmp");
		files[6] = new File ("data/Training Images/1_02.bmp");
		files[7] = new File ("data/Training Images/2_02.bmp");
		files[8] = new File ("data/Training Images/3_02.bmp");
		files[9] = new File ("data/Training Images/4_02.bmp");
		files[10] = new File ("data/Training Images/0_03.bmp");
		files[11] = new File ("data/Training Images/1_03.bmp");
		files[12] = new File ("data/Training Images/2_03.bmp");
		files[13] = new File ("data/Training Images/3_03.bmp");
		files[14] = new File ("data/Training Images/4_03.bmp");
		
		ArrayList<double[][]> testInputs = new ArrayList<double[][]>();
		for(int i = 0; i < files.length; i++ ) {
			try {
				testInputs.add(NeocognitronStructure.readImage(files[i]));
			}
			catch (IOException e) {
				System.out.println("\nERROR!");
			}
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("-yyyyMMdd-hhmmssSS");		
		NeocognitronTrainer trainer = new NeocognitronTrainer(inputs, testInputs, new File ("data/Saved Networks/BestNeocognitron" + dateFormat.format(new Date())+ ".neo"));
		//Neocognitron n = trainer.getNeocognitron(20);
		Neocognitron n = trainer.getNeocognitron((int)Math.round(Math.random()*15+5));
		trainer.verifyNeocognitron(n, testInputs,false);
	}
	
	/**
	 * Test saving a neocognitron.
	 */
	public static void SaveTest() {
		File f = new File ("data/Saved Networks/BestNeocognitron.neo");
		Neocognitron neo = new Neocognitron(new NeocognitronStructure());
		Neocognitron.SaveNeocognitron(neo,f);
		
		Neocognitron neoLoad = Neocognitron.OpenNeocognitron(f);
		
		System.out.println("Neocognitron Loaded Sucessfully");
		System.out.println(neo.getStructure().alpha == neoLoad.getStructure().alpha);
	}
	
	/**
	 * Test the representative cell methods.
	 */
	public static void RepresentativeTest() {
		//OutputConnections outputs = generateOutputs(2, 4);
		OutputConnections outputs = new OutputConnections(2,4);
		outputs.setSingleOutput(0, 3, 3, 100);
		outputs.setSingleOutput(1, 3, 2, 2);
		System.out.println(outputs);
		
		Point[] p = outputs.getRepresentativeCells(3);
		
		for (int i = 0; i < p.length; i++) {
			System.out.print("For plane " + i + ": ");
			if (p[i] == null)
				System.out.println("none");
			else
				System.out.println("( " + p[i].x + ", " + p[i].y + ")");
		}
	}
	
	/**
	 * Test the Location class
	 */
	public static void locationTest() {
		// Generate list of locations
		List<Location> points = new ArrayList<Location>();
		points.add(new Location(0,0,0));
		points.add(new Location(1,0,0));
		points.add(new Location(0,3,4));
		points.add(new Location(1,3,4));
		
		Location test = new Location(1,0,1);
		if ( points.contains(test) ) {
			System.out.println("Location is already in the array!");
		}
		else {
			System.out.println("Location NOT in the array!");
		}
	}
	
	/** 
	 * Test reading in a specific input file
	 */
	public static void readFile() {
		File file = new File ("data/Training Images/0_00.bmp");
		
		try {
			double[][] input = NeocognitronStructure.readImage(file);
			System.out.println(input);
		}
		catch (IOException e) {
			System.out.println("ERROR!");
			return;
		}
	}
	
	/**
	 * Test the monotonic function generater methods.
	 */
	public static void testMonotonicC() {
		NeocognitronStructure s = new NeocognitronStructure();
		double sum;
		double[] c;
		for (int l = 0; l < s.numLayers; l++) {
			sum = 0;
			c = s.getC(l);
			System.out.println("C weight matrix for layer " + l + ":");
			for (int w = 0; w < c.length; w++) {
				System.out.print(c[w] + "\t");
				sum += c[w];
			}
			System.out.println("\nSum of values: " + sum);
			System.out.println("\n");
		}
	}
	
	/**
	 * Test the OutputConnection class
	 */
	public static void testOutputConnections() {
		int K = 2;
		int size = 4;
		
		OutputConnections outputs = generateOutputs(K, size);
		
		System.out.println(outputs.toString());
	}
	
	/**
	 * Generate an artificial OutputConnection
	 * 
	 * @param planes	Number of planes
	 * @param size		Size of each plane
	 * @return			Resulting OutputConnection
	 */
	public static OutputConnections generateOutputs(int planes, int size) {

		int count = 0;
		double[][][] values = new double[planes][size][size];
		for(int k = 0; k < planes; k++) {
			//System.out.println("Plane " + k);
			for(int n = 0; n < size; n++) {
				for(int m = 0; m < size; m++) {
					count++;
					values[k][n][m] = count;
					//System.out.print(count + "\t");
				}
				//System.out.println();
			}
			//System.out.println();
		}
		
		OutputConnections outputs = new OutputConnections(planes, size);
		for (int k = 0; k < planes; k++) {
			outputs.setPlaneOutput(k, values[k]);
		}
		
		return outputs;
	}
	
	/**
	 * Test the methods within OutputConnections
	 * 
	 * @param outputs	OutputConnections under test
	 * @param K			Number of planes
	 */
	public static void testMethods(OutputConnections outputs, int K) {
		
		System.out.println("Single plane window test:");
		double[] temp = outputs.getWindowInPlane(0, 1, 1, 4);
		for (int i = 0; i < temp.length; i++)
			System.out.print(temp[i] + "\t");
		System.out.println();

		System.out.println("Multiple plane window test:");
		double[][] temp1 = outputs.getWindows(1, 1, 4);
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < temp.length; j++) {
				System.out.print(temp1[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}
}
