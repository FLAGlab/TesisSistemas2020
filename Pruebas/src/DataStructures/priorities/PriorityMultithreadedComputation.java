package DataStructures.priorities;

import java.util.ArrayList;

import DataStructures.MultithreadedComputation;
import DataStructures.graph.LongestPathDAG;
import api.Main;
import edu.princeton.cs.algs4.Digraph;

public class PriorityMultithreadedComputation extends MultithreadedComputation {
	
	// Object that will allow us to calculate the priority for each vertex in the computation
	private LongestPathDAG longestPath;
	
	// Array that stores the priority in the computation of each vertex;
	private ArrayList<Integer> priorityVertices;

	public PriorityMultithreadedComputation(Digraph pG, int pNumberOfProcessors) {
		super(pG, pNumberOfProcessors);
		longestPath = new LongestPathDAG(this.getG());
		priorityVertices = new ArrayList<Integer>();
		
		long startTime = System.nanoTime();
		
		//Fill each priorityVertices array entry with its corresponding vertex priority
		longestPath.calculateLongestPathLengthFromVertex(0);
		priorityVertices = longestPath.getPriorityVerticesDAG();
		
		long executionTime = System.nanoTime()-startTime;
		Main.prioritiesOutput.println("Priority of vertices calculated in " + executionTime*1E-6 + " milliseconds.");
	}
	
	public ArrayList<Integer> getPriorityVertices() {
		return this.priorityVertices;
	}
}
