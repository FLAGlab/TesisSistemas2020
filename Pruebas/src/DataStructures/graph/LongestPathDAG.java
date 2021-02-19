package DataStructures.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;

/* Computes the longest path between the last vertex and any other vertex in a DAG */

public class LongestPathDAG {	
	Digraph G;
	private Topological topologicalOrder;
	List <Integer> topologicalOrderList;
	
	// Array that contains the longest path of each vertex
	private ArrayList<Integer> priorityVerticesDAG;
	
	/*
	 * Constructor for the class
	 */
	public LongestPathDAG(Digraph pG){
		
		G=pG;
		
		// Fills the array that contains the longest path of each vertex with -1
		priorityVerticesDAG = new ArrayList<Integer>();
		for (int i = 0 ; i < G.V() ; i++) {
			priorityVerticesDAG.add(-1);
		}
		
		topologicalOrder = new Topological(G);
		Iterable <Integer> topologicalOrderIterable = topologicalOrder.order();
		Iterator <Integer> iteratorTopologicalOrder = topologicalOrderIterable.iterator();
		
		// Save the elements of topological order in a List
		topologicalOrderList = new ArrayList<>();
		iteratorTopologicalOrder.forEachRemaining(topologicalOrderList::add);
		
		// Prints the elements of the topological order of the graph
		/*System.out.print("The topological order of the graph is:");
		for (int i = 0; i<topologicalOrderList.size(); i++) {	
			System.out.print(" "+topologicalOrderList.get(i).toString());
		}
		System.out.println(".");
		 */		
	}
	
	/**
	 * Calculate the longest path from a given vertex to the last vertex in the graph
	 * @param vertex
	 * @return the longest path to the given vertex
	 */
	public int calculateLongestPathLengthFromVertex(Integer vertex) {
		int longestPathLengthFromVertex = 0;
		int currentVertexLongestPath=0;
		
		if(vertex != G.V()-1) {
			Iterable <Integer> adjacentVertices = G.adj(vertex);
			Iterator <Integer> adjacentVerticesIterator = adjacentVertices.iterator();
			
			while(adjacentVerticesIterator.hasNext()) {	
				Integer currentVertex = adjacentVerticesIterator.next();
				if(priorityVerticesDAG.get(currentVertex.intValue()) != -1) 
					currentVertexLongestPath = priorityVerticesDAG.get(currentVertex.intValue()) + 1;
				else
					currentVertexLongestPath = calculateLongestPathLengthFromVertex(currentVertex)+1;
				
				if (currentVertexLongestPath > longestPathLengthFromVertex)
					longestPathLengthFromVertex = currentVertexLongestPath;
			}
		}
		else
			priorityVerticesDAG.set(vertex.intValue(), 0);
		priorityVerticesDAG.set(vertex.intValue(), longestPathLengthFromVertex);
		return longestPathLengthFromVertex;
	}
	
	public ArrayList<Integer> getPriorityVerticesDAG() {
		/*System.out.print("The priority of the vertices is:");
		for (int i=0 ; i < priorityVerticesDAG.size(); i++) 
			System.out.print(" " + priorityVerticesDAG.get(i));
		System.out.println(".");
		*/
		return priorityVerticesDAG;
	}	
}
