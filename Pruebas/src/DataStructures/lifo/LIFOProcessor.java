package DataStructures.lifo;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import DataStructures.MultithreadedComputation;
import DataStructures.Processor;

public class LIFOProcessor extends Thread implements Processor {
	// The multithreaded computation that the processor has to execute
	private MultithreadedComputation computation;
	
	// Id of the processor
	private Integer id;
	
	// Array that stores the spawned task/vertices that need to be enqueued
	private ArrayList<Integer> verticesToEnqueue;
	
	// Ready dequeue of the processor which will be ordered by priority of the vertices/tasks
	private ArrayList <Integer> readyDequeue;
	
	// Flag variable that indicates if a the processor is stealing
	private Boolean isStealing;
	
	// Amount of task executed by the processor
	private int tasksExecuted;
	
	// Processor's execution time
	private long executionTime;
	
	private LinkedBlockingQueue<String> output;
	
	private int iteration;
	private int totalTasks;
	
	/**
	 * Constructor of the class
	 * @param pComputation The computation that the processor is going to execute
	 * @param pId The id of the processor
	 */
	public LIFOProcessor(MultithreadedComputation pComputation, int pId, int totalTasks, int iteration) {	
		computation =pComputation;
		id = pId;
		isStealing = false;
		verticesToEnqueue = new ArrayList<Integer>();
		readyDequeue = new ArrayList<Integer>();
		executionTime=0;
		output = new LinkedBlockingQueue<String>();	
		this.iteration = iteration;
		this.totalTasks = totalTasks;
	}
	
	/**
	 * Visits a vertex in the ready dequeue of the processor. 
	 * If after visiting the vertex the ready dequeue of the processor is empty, the processor begins work stealing, else the processor visits the top priority vertex in the ready dequeue.
	 * If the incident vertices haven't been visited, the processor stalls.
	 * @param vertex The vertex to be visited
	 */
	@Override
	public synchronized void visitVertex(int indexVertexToVisit) {		
		if (readyDequeue.isEmpty())	{
			//Prints the state of the processor 
			//System.out.println("LIFO processor "+ this.id + " ready dequeue is empty.");
			this.steal();
		} else {
			Integer vertex = this.readyDequeue.get(indexVertexToVisit);
			
			// Visits the vertex if all of its predecessors have been visited, else the processor stalls. 
			if(computation.getIncidentVertices().get(vertex) == 0)	{
				
				// Remove vertex from the ready dequeue of the processor
				this.readyDequeue.remove(indexVertexToVisit);
				// Prints the id of the processor and the vertex/task that has visited/completed
				//System.out.println("LIFO processor " + this.id + " has completed task " + vertex + ".");
				// Adds one to the count of the tasks executed by the processor.
				this.tasksExecuted ++;
				// Update visitedVertices, incidentVertices and enqueuedVertices arrays in computation so that the vertex/task appears as visited/completed
				// Enqueue spawned tasks in the ready dequeue of the processor if exist and haven't been enqueued in other processor's ready dequeue
				this.verticesToEnqueue = this.computation.updateVisitedVertices(vertex, this.id);
				
				// Adds all the spawned tasks at the head of the ready dequeue following FIFO
				this.readyDequeue.addAll(0, this.verticesToEnqueue);			
			} else	{
				//Prints the state of the processor 
				//System.out.println("LIFO processor " + this.id + " has stalled.");
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
		for(int i=0; i < this.readyDequeue.size() ; i++) {
			if (this.computation.getIncidentVertices().get(this.readyDequeue.get(i).intValue())==0) {
				this.visitVertex(i);
				break;
			} else if (i == this.readyDequeue.size()-1)	{
				this.steal();
			}
		}
	}
	
	/**
	 * Steals a vertex/task that is enqueued in another processor ready dequeue. 
	 */
	@Override
	public void steal() {
		// Prints the state of the processor 
		//System.out.println("Processor " + this.id + " is work stealing.");
		this.isStealing = true;
		
		if (this.computation.numberOfVisitedVertices() != this.computation.getNumberVerticesG()) {
			Integer stolenVertex = this.computation.stealVertex(this.id);
			
			// Prints the id and the task that had been stolen by the processor
			//System.out.println("LIFO processor " + this.id + " steals task " + stolenVertex + ".");
			
			if(stolenVertex.intValue() != -1 && stolenVertex.intValue() != -2)	{
				//System.out.println("Task " + stolenVertex + " added to LIFO processor " + this.id + " ready dequeue.");
				this.readyDequeue.add(stolenVertex);
				this.visitVertex(this.readyDequeue.size()-1);
			} else if (stolenVertex.intValue() == -2) {
				this.visitVertex(0);
			}
		}
		this.isStealing=false;
	}
	
	/**
	 * When possible, a vertex/task is given to another processor to be visited. 
	 */
	@Override
	public synchronized boolean setVertexToSteal() {
		Boolean vertexSet = false;
		if (this.readyDequeue.size()>1) {
			// Prints the task/vertex that is being given by the processor
			//System.out.println("LIFO processor " + this.id + " gives task " + readyDequeue.get(0) + " to steal.");
			
			// Gives to steal the task that is at the tail of the ready dequeue
			this.computation.setVertexToSteal(this.readyDequeue.get(this.readyDequeue.size()-1));
			this.readyDequeue.remove(this.readyDequeue.size()-1);
			
			vertexSet = true;
		}
		return vertexSet;
	}
	
	/**
	 * Start method for a processor
	 */
	@Override
	public void run() {
		
		long startTime = System.nanoTime();
		if (this.id==0) 
			this.readyDequeue.add(0);
		while(this.computation.numberOfVisitedVertices() != this.computation.getNumberVerticesG()) {
			this.visitVertex(0);
			try {
				sleep(150);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		this.executionTime = System.nanoTime() - startTime;
		System.out.println("LIFO," + this.totalTasks + "," + this.iteration + "," + (this.id + 1) + "," + this.tasksExecuted + "," + this.executionTime*1E-6);
	}
	
	//Get Methods 
	@Override
	public Boolean getIsStealing() {
		return this.isStealing;
	}
	
	@Override
	public long getExecutionTime() {
		return executionTime;
	}
	
	public String getOutput() {
		String res = "";
		for(String s : output) 
			res += s;
		return res;
	}
}
