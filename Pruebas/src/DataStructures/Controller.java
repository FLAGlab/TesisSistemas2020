package DataStructures;

import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.Digraph;

public class Controller {
	
	// The multithreaded computation
	protected MultithreadedComputation multithreadedComputation;
	
	// Work stealing controller
	protected ControllerStealing workStealingController;
	
	// Arraylist of processors
	ArrayList<Processor> processors;
	
	public Controller(Digraph pG, int numberOfProcessors) {
		multithreadedComputation = new MultithreadedComputation(pG,numberOfProcessors);
		workStealingController = new ControllerStealing(multithreadedComputation);
		processors = new ArrayList<Processor>();
	}
	
	// Beginning of the execution
	public void startExecution() {	
		// Starts each one of the processors
		Iterator<Processor> it = processors.iterator();
		workStealingController.start();
		while(it.hasNext()) {
			it.next().start();
		}
	}

	public ControllerStealing getWorkStealingController() {
		return workStealingController;
	}

	public void setProcessors(ArrayList<Processor> processors) {
		this.processors = processors;
	}
}
