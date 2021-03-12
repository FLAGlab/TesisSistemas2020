package DataStructures;

import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;

public class Controller {
	
	// The multithreaded computation
	protected MultithreadedComputation multithreadedComputation;
	
	// Work stealing controller
	protected ControllerStealing workStealingController;
	
	// Arraylist of processors
	ArrayList<Processor> processors;
	
	public Controller(Digraph pG, int numberOfProcessors) {
		this.multithreadedComputation = new MultithreadedComputation(pG,numberOfProcessors);
		this.setWorkStealingController(this.multithreadedComputation);
		processors = new ArrayList<Processor>();
	}
	
	// Beginning of the execution
	public void startExecution() {	
		// Starts each one of the processors
		for(int i=0; i<processors.size(); i++) {
			if(i == 0) {
				workStealingController.start();
				processors.get(i).start();
			}
			else {
				processors.get(i).start();
			}

		}
	}

	public ControllerStealing getWorkStealingController() {
		return workStealingController;
	}

	public void setWorkStealingController(MultithreadedComputation multithreadedComputation) {
		this.workStealingController = new ControllerStealing(multithreadedComputation);  
	}
	
	public void setProcessors(ArrayList<Processor> processors) {
		this.processors = processors;
	}
}
