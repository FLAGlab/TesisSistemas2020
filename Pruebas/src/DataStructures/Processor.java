package DataStructures;

public interface Processor {
	/**
	 * Visits a vertex in the ready dequeue of the processor. 
	 * If after visiting the vertex the ready dequeue of the processor is empty, the processor begins work stealing, else the processor visits the top priority vertex in the ready dequeue.
	 * If the incident vertices haven't been visited, the processor stalls.
	 * @param vertex The vertex to be visited
	 */
	public void visitVertex(int indexVertexToVisit);

	/**
	 * If the processor is stalled seeks in its ready dequeue for a vertex to be visited (task to be executed), else the processor begins work stealing.
	 */
	public void stall();

	/**
	 * Steals a vertex/task that is enqueued in another processor ready dequeue. 
	 */
	public void steal();

	/**
	 * When possible, a vertex/task is given to another processor to be visited. 
	 */
	public boolean setVertexToSteal();
	
	/**
	 * Start method for a processor
	 */
	public void run();

	public void start();
	
	//Get Methods 	
	public Boolean getIsStealing();

	public long getExecutionTime();
}
