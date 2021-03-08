package DataStructures;

import java.util.ArrayList;
import java.util.Iterator;

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
				Iterator<Processor> it = processors.iterator();
				while(it.hasNext()) {
					Processor p = it.next();
					if(p.getIsStealing() == false && p.setVertexToSteal()) {
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
