package neocognitron;

import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RunTest {

	public static void main(String[] args)
	{
		System.out.println("Starting Test!");

		RepresentativeTest();
	}
	
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
	
	public static void readFile() {
		File file = new File ("data\\Training Images\\0_00.bmp");
		
		try {
			double[][] input = NeocognitronStructure.readImage(file);
			System.out.println(input);
		}
		catch (IOException e) {
			System.out.println("ERROR!");
			return;
		}
		
	}
	
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
	
	public static void testOutputConnections() {
		int K = 2;
		int size = 4;
		
		OutputConnections outputs = generateOutputs(K, size);
		
		System.out.println(outputs.toString());
	}
	
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
		
		
		
		
//		for (int k = 0; k < K; k++) {
//			System.out.println("Object Plane " + k);
//			double plane[][] = outputs.getPlane(k);
//			for(int n = 0; n < size; n++) {
//				for(int m = 0; m < size; m++) {
//					System.out.print(plane[n][m] + "\t");
//				}
//				System.out.println();
//			}
//			System.out.println();
//		}
//		
//		System.out.println("Location Test at (3, 3)!");
//		double[] temp = outputs.getPointsOnPlanes(3, 3);
//		for (int k = 0; k < K; k++)
//			System.out.println(temp[k] + "\t");
}
