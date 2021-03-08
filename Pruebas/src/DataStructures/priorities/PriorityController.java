package DataStructures.priorities;

import DataStructures.Controller;
import edu.princeton.cs.algs4.Digraph;

public class PriorityController extends Controller {
	
	private  PriorityMultithreadedComputation multithreadedComputation;
	
	public PriorityController(Digraph graph, int numberOfProcessors, int totalTasks, int iteration) {
		super(graph, numberOfProcessors);
		//Create and store the processors
		this.multithreadedComputation = new PriorityMultithreadedComputation(graph,numberOfProcessors);
		for (int i = 0 ; i < numberOfProcessors ; i++)
			this.workStealingController.addProcessor(new PriorityProcessor(this.multithreadedComputation,i,totalTasks,iteration));
		this.setProcessors(this.workStealingController.getProcessors());
	}
}
