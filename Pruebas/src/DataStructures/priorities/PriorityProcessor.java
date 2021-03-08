package DataStructures.priorities;

import java.util.ArrayList;

import DataStructures.Processor;

public class PriorityProcessor extends Thread implements Processor {	
	// The multithreaded computation that the processor has to execute
	PriorityMultithreadedComputation computation;
	
	// Id of the processor
	Integer id;
	
	// Array that stores the spawned task/vertices that need to be enqueued
	ArrayList<Integer> verticesToEnqueue;
	
	// Ready dequeue of the processor which will be ordered by priority of the vertices/tasks
	ArrayList <Integer> readyDequeue;
	
	// Flag variable that indicates if a the processor is stealing
	private Boolean isStealing;
	
	// Amount of task executed by the processor
	int tasksExecuted;
	
	// Processor's execution time
	long executionTime;
	
	private int iteration;
	private int totalTasks;
	
	/**
	 * Constructor of the class
	 * @param pComputation The computation that the processor is going to execute
	 * @param pId The id of the processor
	 */
	public PriorityProcessor(PriorityMultithreadedComputation pComputation, int pId, int totalTasks, int iteration) {	
		computation = pComputation;
		id = pId;
		isStealing = false;
		verticesToEnqueue = new ArrayList<Integer>();
		readyDequeue = new ArrayList<Integer>();
		executionTime=0;
		this.iteration = iteration;
		this.totalTasks = totalTasks;
	}
	
	/**
	 * Visits a vertex in the ready dequeue of the processor. 
	 * If after visiting the vertex the ready dequeue of the processor is empty, the processor begins work stealing, else the processor visits the top priority vertex in the ready dequeue.
	 * If the incident vertices haven't been visited, the processor stalls.
	 * @param vertex The vertex to be visited
	 */
	public synchronized void visitVertex(int indexVertexToVisit) {		
		if(readyDequeue.isEmpty()) {
			this.steal();
		} else {
			Integer vertex = readyDequeue.get(indexVertexToVisit);
			// Visits the vertex if all of its predecessors have been visited, else the processor stalls. 
			if(computation.getIncidentVertices().get(vertex) == 0) {
				// Remove vertex from the ready dequeue of the processor
				readyDequeue.remove(indexVertexToVisit);
				
				// Adds one to the count of the tasks executed by the processor.
				tasksExecuted ++;

				// Update visitedVertices, incidentVertices and enqueuedVertices arrays in computation so that the vertex/task appears as visited/completed
				// Enqueue spawned tasks in the ready dequeue of the processor if exist and haven't been enqueued in other processor's ready dequeue
				verticesToEnqueue = computation.updateVisitedVertices(vertex,this.id);
				
				for(int i = 0 ; i < verticesToEnqueue.size() ; i++) {
					Integer adjacentVertex = verticesToEnqueue.get(i);
					
					if(readyDequeue.isEmpty() || computation.getPriorityVertices().get(adjacentVertex.intValue()) >= computation.getPriorityVertices().get(readyDequeue.get(0).intValue())) {
						readyDequeue.add(0, adjacentVertex);
					} else if (computation.getPriorityVertices().get(adjacentVertex.intValue()) <= computation.getPriorityVertices().get(readyDequeue.size()-1)) {
						readyDequeue.add(adjacentVertex);
					} else {
						for (int j = 0 ; j < readyDequeue.size()-1 ; j++) {
							if (computation.getPriorityVertices().get(adjacentVertex.intValue()) <= computation.getPriorityVertices().get(readyDequeue.get(j).intValue()) && computation.getPriorityVertices().get(adjacentVertex.intValue()) >= computation.getPriorityVertices().get(readyDequeue.get(j+1).intValue())) {
								readyDequeue.add(j+1, adjacentVertex);
								break;
							}
						}
					}			
				}
			} else {
				//Processor enters in stall
				this.stall();
			}	
		}
	}
	
	/**
	 * If the processor is stalled seeks in its ready dequeue for a vertex to be visited (task to be executed), else the processor begins work stealing.
	 */
	@Override
	public void stall() {
		// Processor checks if there is any task/vertex in its ready dequeue that can be visited. If not, begins work stealing
		for (int i=0; i < readyDequeue.size(); i++) {
			if (computation.getIncidentVertices().get(readyDequeue.get(i).intValue())==0) {
				this.visitVertex(i);
				break;
			} else if (i == readyDequeue.size()-1) {
				this.steal();
			}
		}
	}
	
	/**
	 * Steals a vertex/task that is enqueued in another processor ready dequeue. 
	 */
	public void steal() {
		this.isStealing = true;
		
		if(computation.numberOfVisitedVertices()!=computation.getNumberVerticesG()) {
			Integer stolenVertex = computation.stealVertex(this.id);
			
			if(stolenVertex.intValue() != -1 && stolenVertex.intValue() != -2) {
				readyDequeue.add(stolenVertex);
				this.visitVertex(readyDequeue.size()-1);
			} else if (stolenVertex.intValue() == -2) {
				this.visitVertex(0);
			}
		}
		this.isStealing=false;
	}
	
	/**
	 * When possible, a vertex/task is given to another processor to be visited. 
	 */
	public synchronized boolean setVertexToSteal() {
		Boolean vertexSet = false;
		if (this.readyDequeue.size()>1) {
			computation.setVertexToSteal(this.readyDequeue.get(0));
			readyDequeue.remove(0);
			
			vertexSet = true;
		}
		return vertexSet;
	}
	
	/**
	 * Start method for a processor
	 */
	public void run() {
		
		long startTime = System.nanoTime();
		if (this.id==0) {
			readyDequeue.add(0);
		}
		while(computation.numberOfVisitedVertices()!= computation.getNumberVerticesG()) {
			this.visitVertex(0);
			try {
				sleep(150);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		executionTime = System.nanoTime()-startTime;
		System.out.println("PRIO," + this.totalTasks + "," + this.iteration + "," + (this.id + 1) + "," + this.tasksExecuted + "," + this.executionTime*1E-6);
	}
	
	//Get Methods 	
	public Boolean getIsStealing() {
		return this.isStealing;
	}
	
	public long getExecutionTime() {
		return executionTime;
	}
}