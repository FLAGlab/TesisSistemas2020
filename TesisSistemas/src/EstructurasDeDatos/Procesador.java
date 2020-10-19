package EstructurasDeDatos;

import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.Bag;



public class Procesador extends Thread{
	
	// The multithreaded computation that the processor has to execute
	MultithreadedComputation computation;
	
	// Id of the processor
	Integer id;
	
	// Array that stores the spawned task/vertices that need to be enqueued
	ArrayList<Integer> verticesToEnqueue;
	
	// Ready dequeue of the processor which will be ordered by priority of the vertices/tasks
	ArrayList <Integer> readyDequeue;
	
	/**
	 * Constructor of the class
	 * @param pComputation The computation that the processor is going to execute
	 * @param pId The id of the processor
	 */
	public Procesador(MultithreadedComputation pComputation, int pId) {	
		
		computation =pComputation;
		id = pId;
		verticesToEnqueue = new ArrayList<Integer>();
		readyDequeue = new ArrayList<Integer>();
	}
	
	/**
	 * Visits a vertex in the ready dequeue of the processor. 
	 * If after visiting the vertex the ready dequeue of the processor is empty, the processor begins work stealing, else the processor visits the top priority vertex in the ready dequeue.
	 * If the incident vertices haven't been visited, the processor stalls.
	 * @param vertex The vertex to be visited
	 */
	public void visitVertex(int indexVertexToVisit)
	{		
		if (readyDequeue.isEmpty())
		{
			//Prints the state of the processor 
			System.out.println("Processor's "+ this.id + " ready dequeue is empty.");
			
			this.steal();
		}
		else
		{
			Integer vertex = readyDequeue.get(indexVertexToVisit);
			
			// Visits the vertex if all of its predecessors have been visited, else the processor stalls. 
			
			if (computation.getIncidentVertices().get(vertex) == 0)
			{
				
				// Remove vertex from the ready dequeue of the processor
				readyDequeue.remove(indexVertexToVisit);
				
				// Prints the id of the processor and the vertex/task that has visited/completed
				System.out.println("Processor " + this.id + " has completed task " + vertex + ".");

				// Update visitedVertices, incidentVertices and enqueuedVertices arrays in computation so that the vertex/task appears as visited/completed
				// Enqueue spawned tasks in the ready dequeue of the processor if exist and haven't been enqueued in other processor's ready dequeue
				verticesToEnqueue = computation.updateVisitedVertices(vertex,this.id);
				
				for (int i = 0 ; i < verticesToEnqueue.size() ; i++)
				{
					Integer adjacentVertex = verticesToEnqueue.get(i);
					
					// Prints the task that is being enqueued in the ready dequeue of the processor
					System.out.println("Task " + adjacentVertex + " enqueued in the processor's " + this.id + " ready dequeue. ");
						
					if (readyDequeue.isEmpty() || computation.getPriorityVertices().get(adjacentVertex.intValue()) >= computation.getPriorityVertices().get(readyDequeue.get(0).intValue()))
					{
						readyDequeue.add(0, adjacentVertex);
					}
					else if (computation.getPriorityVertices().get(adjacentVertex.intValue()) <= computation.getPriorityVertices().get(readyDequeue.size()-1))
					{
						readyDequeue.add(adjacentVertex);
					}
					else
					{
						for (int j = 0 ; j < readyDequeue.size()-1 ; j++)
						{
							if (computation.getPriorityVertices().get(adjacentVertex.intValue()) <= computation.getPriorityVertices().get(readyDequeue.get(j).intValue()) && computation.getPriorityVertices().get(adjacentVertex.intValue()) >= computation.getPriorityVertices().get(readyDequeue.get(j+1).intValue()))
							{
								readyDequeue.add(j+1, adjacentVertex);
								break;
							}
						}
					}			
				}
			}
			else
			{
				//Prints the state of the processor 
				System.out.println("Processor " + this.id + " has stalled.");
				
				//Processor enters in stall
				this.stall(indexVertexToVisit);
			}
			
		}
	}
	
	/**
	 * If the processor is stalled seeks in its ready dequeue for a vertex to be visited (task to be executed), else the processor begins work stealing.
	 */
	private void stall(int indexVertexToVisit) 
	{
		// Processor checks if there is any task/vertex in its ready dequeue that can be visited. If not, begins work stealing
		for (int i = indexVertexToVisit+1 ; i < readyDequeue.size() ; i++)
		{
			if (computation.getIncidentVertices().get(readyDequeue.get(i).intValue())==0)
			{
				this.visitVertex(i);
				break;
			}
			else if (i == readyDequeue.size()-1)
			{
				this.steal();
			}
		}

	}
	
	/**
	 * Steals a vertex/task that is enqueued in another processor ready dequeue. 
	 */
	private void steal() 
	{
		// Prints the state of the processor 
		System.out.println("Processor " + this.id + " is work stealing.");
		
		if (computation.numberOfVisitedVertices()!=computation.getNumberVerticesG())
		{
			readyDequeue.add(computation.stealVertex(this.id));
			this.visitVertex(readyDequeue.size()-1);
		}
	}
	
	/**
	 * When possible, a vertex/task is given to another processor to be visited. 
	 */
	public boolean setVertexToSteal()
	{
		Boolean vertexSet = false;
		if (this.readyDequeue.size()>1)
		{
			// Prints the task/vertex that is being given by the processor
			System.out.println("Processor " + this.id + " gives task " + readyDequeue.get(0) + " to steal.");
			
			computation.setVertexToSteal(this.readyDequeue.get(0));
			readyDequeue.remove(0);
			
			vertexSet = true;
		}
		return vertexSet;
	}
	
	/**
	 * Start method for a processor
	 */
	public void run()
	{
		if (this.id==0)
		{
			readyDequeue.add(0);
		}
		while(computation.numberOfVisitedVertices()!= computation.getNumberVerticesG())
		{
			this.visitVertex(0);
		}
	}

}

