package EstructurasDeDatosLIFO;

import java.util.ArrayList;

public class ControllerStealingLIFO extends Thread {
	
	
	// Array that stores the processors
	
	ArrayList<ProcesadorLIFO> processors;
	
	// Computation that is going to be processed 
	MultithreadedComputationLIFO computation;
	
	
	public ControllerStealingLIFO(MultithreadedComputationLIFO pComputacion)
	{
		processors = new ArrayList<ProcesadorLIFO>();
		computation = pComputacion;
	}
	
	/**
	 * Add a processor to the work stealing controller
	 * @param procesador The processor that wants to be added
	 */
	public void addProcessor(ProcesadorLIFO procesador)
	{
		processors.add(procesador);
	}
	
	public void run()
	{	
		while(computation.numberOfVisitedVertices()!= computation.getNumberVerticesG())
		{
			if (computation.getNumberOfProcessorsStealing() != 0)
	 		{
	 			for (int i=0 ; i< processors.size(); i++)
	 			if (processors.get(i).getIsStealing() == false && processors.get(i).setVertexToSteal())
	 			{
	 				try {
	 					sleep(300);
	 				} catch (Exception e) {
	 							// TODO: handle exception
	 					}
	 				break;
	 			}
	 		}
		}
	}
	
	// Get methods
	
	public ArrayList<ProcesadorLIFO> getProcessors()
	{
		return processors;
	}

}
