package DataStructures.lifo;

import DataStructures.Controller;
import edu.princeton.cs.algs4.Digraph;

public class LIFOController extends Controller {
	
	public LIFOController(Digraph graph, int numberOfProcessors, int totalTasks, int iteration) {
		super(graph, numberOfProcessors);
		//Create and store the processors
		for (int i = 0; i < numberOfProcessors; i++)
			this.workStealingController.addProcessor(new LIFOProcessor(this.multithreadedComputation,i,totalTasks,iteration));
		this.setProcessors(this.workStealingController.getProcessors());
	}
}
