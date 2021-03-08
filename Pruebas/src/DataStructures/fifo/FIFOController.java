package DataStructures.fifo;

import DataStructures.Controller;
import edu.princeton.cs.algs4.Digraph;

public class FIFOController extends Controller {
	
	public FIFOController(Digraph graph, int numberOfProcessors, int totalTasks, int iteration) {
		super(graph, numberOfProcessors);
		//Create and store the processors
		for (int i=0; i<numberOfProcessors; i++)
			this.workStealingController.addProcessor(new FIFOProcessor(this.multithreadedComputation,i,totalTasks,iteration));
		this.setProcessors(this.workStealingController.getProcessors());
	}
}
