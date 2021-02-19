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
	public static PrintWriter out;
	public static PrintWriter prioritiesOutput;
	public static PrintWriter lifoOutput;
	public static PrintWriter fifoOutput;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		int ITERATIONS = Integer.valueOf(args[0]);
		int MAX_CORES = Integer.valueOf(args[1]);
		// Array that contains the amount of vertices of the graphs that are going to be tested
		int[] graphSize = {50};//, 100, 200, 400, 800, 1600};
		
		// Array containing densities 
		double[] density = {0.2, 0.5, 0.8};
		
		PriorityController controlllerComputation;
		FIFOController fifoController;
		LIFOController lifoController;
		
		// File where the results of the test are going to be printed
		String baseDir = System.getProperty("user.dir");
		baseDir = baseDir.substring(0, baseDir.lastIndexOf("/"));
		
		Main.out = new PrintWriter(new FileWriter(baseDir + "/results.csv")); 
		
		// Tests executed
		Main.out.println("size,density,cores,algo,aveg_time");
		Main.prioritiesOutput = new PrintWriter(new FileWriter(baseDir + "/prorities.txt", true));
		Main.prioritiesOutput.println("proc_id,tasks_executed,time_ms");
		Main.lifoOutput = new PrintWriter(new FileWriter(baseDir + "/lifo.txt", true));
		Main.lifoOutput.println("proc_id,tasks_executed,time_ms");
		Main.fifoOutput = new PrintWriter(new FileWriter(baseDir + "/fifo.txt", true));
		Main.fifoOutput.println("proc_id,tasks_executed,time_ms");
		for(int i= 0 ; i < graphSize.length; i++) {
			for(int j = 0 ; j < density.length ; j++) {
				// Creates the DAG generator 
				DagGenerator dagGenerator = new DagGenerator(graphSize[i],density[j]);
				Digraph H = dagGenerator.generateDAG();
				
				for(int cores = 1 ; cores <= MAX_CORES ; cores++) {
					double priorityTime = 0.0;
					double fifoTime = 0.0;
					double lifoTime = 0.0;
					for(int k = 1 ; k <= ITERATIONS ; k++) {
						// Creates the controller of the execution
						Main.prioritiesOutput.println("---");
						controlllerComputation = new PriorityController(H, cores);
/*						double priotiryStart = System.nanoTime();
						controlllerComputation.startExecution();
						priorityTime += (System.nanoTime() - priotiryStart);
*/						
						Main.fifoOutput.println("---");
						fifoController = new FIFOController(H,cores);
						double fifoStart = System.currentTimeMillis();
						fifoController.startExecution();
						fifoTime += (System.currentTimeMillis() - fifoStart);
					
						Main.lifoOutput.println("---");
						lifoController  = new LIFOController(H,cores);
						double lifoStart = System.currentTimeMillis();
						lifoController.startExecution();
						lifoTime += (System.currentTimeMillis() - lifoStart);
					}
					Main.out.println(String.format("%d,%f,%d,PRIO,%f", graphSize[i], density[j], cores, priorityTime / ITERATIONS));
					Main.out.println(String.format("%d,%f,%d,FIFO,%f", graphSize[i], density[j], cores, fifoTime / ITERATIONS));
					Main.out.println(String.format("%d,%f,%d,LIFO,%f", graphSize[i], density[j], cores, lifoTime / ITERATIONS));
				}
			}
		}
		Main.out.close();
		Main.prioritiesOutput.close();
		Main.fifoOutput.close();
		Main.lifoOutput.close();
	}
}
