package DataStructures;

import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;

public class MultithreadedComputation {
	
	// The digraph that models the multithreaded computations
	private Digraph G;

	// Number of tasks/vertices of the multithreaded computation
	private int numberVerticesG; 
	
	// Number of processors executing the computation
	private int numberOfProcessors;
	
	// Array that stores, for each vertex, the number of incident vertices that have been visited
	private ArrayList<Integer> incidentVertices;
	
	// Array that tells if a given vertex has been visited. The i-th entry equals 0 if the vertex i has
	// not been visited yet and 1 if the vertex has been visited
	private ArrayList<Integer> visitedVertices;
	
	// Array that tells if a given vertex has been enqueued. The i-th entry equals -1 if the vertex i has
	// not been enqueued yet and the id of the processor if the task was enqueued in the ready dequeue of the latter
	private ArrayList<Integer> enqueuedVertices;
	
	// Counts the number o of processors that are stealing
	
	private int numberOfProcessorsStealing; 
	
	// Array that stores all the tasks that can be stolen
	private Integer vertexToSteal;
	
	// Flag variable that indicates if there is a vertex/task to be stolen
	private Boolean availableVertexToSteal;
	
	
	public MultithreadedComputation(Digraph pG, int pNumberOfProcessors) {
		G = pG ;
		numberVerticesG = G.V();
		incidentVertices = new ArrayList<Integer>();
		visitedVertices = new ArrayList<Integer>();
		enqueuedVertices = new ArrayList<Integer>();
		numberOfProcessors = pNumberOfProcessors;
		numberOfProcessorsStealing = 0;
		vertexToSteal = -1;
		availableVertexToSteal = false;
		
		//Fill the incidentVertices array with the number of incident vertices to each vertex
		for (int v = 0 ; v < numberVerticesG; v++)
			incidentVertices.add(G.indegree(v));
		
		//Fill each visitedVertices and enqueuedVertices array entry with 0 and -1 respectively since none of the vertices of G have been visited or enqueued
		for (int v = 0 ; v < numberVerticesG ; v++) {
			visitedVertices.add(0);
			enqueuedVertices.add(-1);
		}
	}
	
	/**
	 * Counts the number of vertices that have been visited
	 * @return The number of vertices/tasks that have been visited/completed
	 */
	public Integer numberOfVisitedVertices() {
		Integer numVisitedVertices = 0;
		for (int i = 0 ; i < visitedVertices.size() ; i++)
			numVisitedVertices = numVisitedVertices + visitedVertices.get(i);
		return numVisitedVertices;
	}
	
	/**
	 * Updates visitedVertices, incidentVertices and enqueuedVertices arrays when a processor completes/visits a task/vertex
	 * @param vertex The vertex that was visited
	 * @param processor The id of the processor that visited the vertex
	 */
	public synchronized ArrayList<Integer> updateVisitedVertices(Integer vertex, int processor) {
		// The array that stores the vertices that are going to be enqueued by the processor
		ArrayList<Integer> verticesToEnqueue = new ArrayList<Integer>();
		
		// Vertex marked as visited
		visitedVertices.set(vertex.intValue(), 1);
		
		Bag<Integer> adjacentVertices = (Bag<Integer>) G.adj(vertex);
		Iterator<Integer> iteratorAdjacentVertices = adjacentVertices.iterator();
		while(iteratorAdjacentVertices.hasNext()) {
			Integer adjacentVertex = iteratorAdjacentVertices.next();
			
			// Update incident vertices list of the child vertices
			incidentVertices.set(adjacentVertex.intValue(), incidentVertices.get(adjacentVertex)-1);
			
			// If the adjacent vertex has already been enqueued by other processor then it is not enqueued
			if(enqueuedVertices.get(adjacentVertex.intValue()) == -1) {
				enqueuedVertices.set(adjacentVertex.intValue(), processor);
				verticesToEnqueue.add(adjacentVertex);
				// Prints the task that is being enqueued in the ready dequeue of the processor
				//System.out.println("Task " + adjacentVertex + " enqueued in the LIFO processor's " + processor + " ready dequeue. ");
			}		
		}
		return verticesToEnqueue;
	}	
	
	/**
	 * Returns a vertex/task that can be stolen
	 * @return The vertex/task to be stolen
	 */
	public synchronized Integer stealVertex(Integer id) {	
		numberOfProcessorsStealing ++;
		//System.out.println("The number of LIFO processors stealing is: " + this.numberOfProcessorsStealing +".");
		
		// While there is not vertex/task to steal processor waits
		while (availableVertexToSteal == false) {
			if (this.numberOfProcessorsStealing == this.numberOfProcessors) { 
				try {
					wait(300);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
				numberOfProcessorsStealing = numberOfProcessorsStealing -1;
				return -2;
			} else if (this.numberOfVisitedVertices() == numberVerticesG) {
				numberOfProcessorsStealing = numberOfProcessorsStealing -1;
				return -1;
			} else {
				try {
					wait(300);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		// When available the vertex/task is assigned to the processor
		numberOfProcessorsStealing = numberOfProcessorsStealing -1;
		availableVertexToSteal = false;
		return vertexToSteal;
	}
	
	/**
	 * Sets a vertex/task to be stolen
	 * @param vertex
	 */
	public synchronized void setVertexToSteal( Integer vertex ) {
		vertexToSteal = vertex;
		availableVertexToSteal =  true;
		notifyAll();
	}

	// Get methods
	public Boolean getAvailableVertexToSteal() {
		return availableVertexToSteal;
	}

	public ArrayList<Integer> getEnqueuedVertices() {
		return enqueuedVertices;
	}

	public ArrayList<Integer> getIncidentVertices() {
		return incidentVertices;
	}
	
	public ArrayList<Integer> getVisitedVertices() {
		return visitedVertices;
	}
	
	public int getNumberOfProcessorsStealing() {
		return numberOfProcessorsStealing;
	}
	
	public int getNumberVerticesG() {
		return numberVerticesG;
	}
	
	public Digraph getG() {
		return G;
	}
}
