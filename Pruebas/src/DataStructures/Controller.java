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
	//ArrayList<PriorityProcessor> processors;
	
	public Controller(Digraph pG, int numberOfProcessors) {
		multithreadedComputation = new MultithreadedComputation(pG,numberOfProcessors);
		workStealingController = new ControllerStealing(multithreadedComputation);
	}
	
	// Beginning of the execution
	public void startExecution() {	
		// Starts each one of the processors
		for (int i=0; i<processors.size(); i++) {
			if(i == 0) {
				//Notifies that each processor has started its execution
				//System.out.println("Processor " + i + " has started the execution.");
				// Starts the work stealing controller
				//System.out.println("Work stealing controller has started execution.");
				workStealingController.start();
				processors.get(i).start();
			} else {
				//Notifies that each processor has started its execution
				//System.out.println("Processor " + i + " has started the execution.");
				processors.get(i).start();
			}
		}
	}

	public ControllerStealing getWorkStealingController() {
		return workStealingController;
	}

	public void setProcessors(ArrayList<Processor> processors) {
		this.processors = processors;
	}
	
	public String getProcessorOutput() {
		String out = "";
		for(Processor p : this.processors)
			out += p.getOutput();
		return out;
	}
}
