package DataStructures;

import java.util.ArrayList;

public class ControllerStealing extends Thread {	
	// Array that stores the processors
	ArrayList<Processor> processors;
	
	// Computation that is going to be processed 
	MultithreadedComputation computation;
	
	
	public ControllerStealing(MultithreadedComputation pComputacion) {
		processors = new ArrayList<Processor>();
		computation = pComputacion;
	}
	
	/**
	 * Add a processor to the work stealing controller
	 * @param procesador The processor that wants to be added
	 */
	public void addProcessor(Processor procesador) {
		processors.add(procesador);
	}
	
	public void run() {	
		while(computation.numberOfVisitedVertices()!= computation.getNumberVerticesG()) {
			if(computation.getNumberOfProcessorsStealing() != 0) {
				for (int i=0 ; i< processors.size(); i++) {
		 			if (processors.get(i).getIsStealing() == false && processors.get(i).setVertexToSteal()) {
						try {
		 					sleep(300);
		 				} catch (Exception e) {
		 					e.printStackTrace();
		 				}
		 				break;
					}
				}
	 		}
		}
	}
	
	// Get methods
	public ArrayList<Processor> getProcessors() {
		return processors;
	}
}
