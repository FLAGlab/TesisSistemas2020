package api;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import DataStructures.fifo.FIFOController;
import DataStructures.graph.DagGenerator;
import DataStructures.lifo.LIFOController;
import DataStructures.priorities.PriorityController;
import edu.princeton.cs.algs4.Digraph;

public class Main {
	public static PrintWriter fifoOut;
	public static PrintWriter lifoOut;
	public static PrintWriter priorOut;

	public static void main(String[] args) throws IOException, InterruptedException {
		int ITERATIONS = Integer.valueOf(args[0]);
		int MAX_CORES = Integer.valueOf(args[1]);
		// Array that contains the amount of vertices of the graphs that are going to be tested
		int[] graphSize = {50, 100, 200, 400, 800, 1600};
		// Array containing densities 
		double[] density = {0.2, 0.5, 0.8};
		
		PriorityController controlllerComputation;
		FIFOController fifoController;
		LIFOController lifoController;
		
		// File where the results of the test are going to be printed
		String baseDir = System.getProperty("user.dir");
		baseDir = baseDir.substring(0, baseDir.lastIndexOf("/"));
		
		Main.fifoOut = new PrintWriter(new FileWriter(baseDir + "/fifo_"+MAX_CORES+"_results.csv"));
		Main.lifoOut = new PrintWriter(new FileWriter(baseDir + "/lifo_"+MAX_CORES+"_results.csv"));
		Main.priorOut = new PrintWriter(new FileWriter(baseDir + "/prior_"+MAX_CORES+"_results.csv"));
		
		Main.priorOut.println("size,density,cores,algo,aveg_time_ms");
		Main.lifoOut.println("size,density,cores,algo,aveg_time_ms");
		Main.fifoOut.println("size,density,cores,algo,aveg_time_ms");
		
		// Tests executed
		for(int i= 0 ; i < graphSize.length; i++) {
			System.out.println("LENGTH," + graphSize[i]);
			for(int j = 0 ; j < density.length ; j++) {
				// Creates the DAG generator 
				DagGenerator dagGenerator = new DagGenerator(graphSize[i],density[j]);
				Digraph H = dagGenerator.generateDAG();
				
				//for(int core=0; core<=cores.length; core++) {
				double priorityTime = 0.0;
				double fifoTime = 0.0;
				double lifoTime = 0.0;
					for(int k=1 ; k <= ITERATIONS ; k++) {
						// Creates the controller of the execution
						controlllerComputation = new PriorityController(H, MAX_CORES, graphSize[i],k);
						double priotiryStart = System.currentTimeMillis();
						controlllerComputation.startExecution();
						priorityTime += (System.currentTimeMillis() - priotiryStart);
					}
					Main.priorOut.println(String.format("%d,%f,%d,PRIO,%f", graphSize[i], density[j], MAX_CORES, priorityTime / ITERATIONS));
					System.out.println(String.format("%d,%f,%d,PRIO,%f", graphSize[i], density[j], MAX_CORES, priorityTime / ITERATIONS));
					
					for(int k=1 ; k <= ITERATIONS ; k++) {							
						fifoController = new FIFOController(H,MAX_CORES,graphSize[i],k);
						double fifoStart = System.currentTimeMillis();
						fifoController.startExecution();
						fifoTime += (System.currentTimeMillis() - fifoStart);
					}
					Main.fifoOut.println(String.format("%d,%f,%d,FIFO,%f", graphSize[i], density[j], MAX_CORES, fifoTime / ITERATIONS));
					System.out.println(String.format("%d,%f,%d,FIFO,%f", graphSize[i], density[j], MAX_CORES, fifoTime / ITERATIONS));
					
					for(int k=1 ; k <= ITERATIONS ; k++) {
						lifoController  = new LIFOController(H,MAX_CORES,graphSize[i],k);
						double lifoStart = System.currentTimeMillis();
						lifoController.startExecution();
						lifoTime += (System.currentTimeMillis() - lifoStart);
					}
					Main.lifoOut.println(String.format("%d,%f,%d,LIFO,%f", graphSize[i], density[j], MAX_CORES, lifoTime / ITERATIONS));
					System.out.println(String.format("%d,%f,%d,LIFO,%f", graphSize[i], density[j], MAX_CORES, lifoTime / ITERATIONS));
				//}
			}
		}
		Main.lifoOut.close();
		Main.fifoOut.close();
		Main.priorOut.close();
	}
}
