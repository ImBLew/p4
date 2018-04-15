import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.swing.text.html.HTMLDocument.Iterator;

/**
 * Undirected and unweighted graph implementation
 * 
 * @param <E> type of a vertex
 * 
 * @author sapan (sapan@cs.wisc.edu)
 * 
 */
public class Graph<E> implements GraphADT<E> {
    
    /**
     * Instance variables and constructors
     */
	private ArrayList<E> Vertices;
	private ArrayList<ArrayList<Integer>> Matrix;
	
	public Graph() {
		Vertices = new ArrayList<E>();
		Matrix = new ArrayList<ArrayList<Integer>>();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public E addVertex(E vertex) {
        if (vertex == null)
        	return null;
        
        for(int i = 0; i < Vertices.size(); i++) {
        	if(Vertices.get(i).equals(vertex))
        		return null;
        }
        Vertices.add(vertex);
        Matrix.add(new ArrayList<Integer>(Vertices.size()));
        for (int i = 0; i < Vertices.size(); i++) {
            Matrix.get(Vertices.size() - 1).add(i, 0);
            Matrix.get(i).add(Vertices.size() - 1, 0);
        }
        
        return vertex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E removeVertex(E vertex) {
        if(vertex == null)		
        	return null;
        
        //likely to have bugs
        for(int i = 0; i < Vertices.size(); i++) {
        	//.equals could be causing a problem
        	if(Vertices.get(i).equals(vertex)) {
        		Matrix.remove(i);
        		for(int j=0; j < Matrix.size(); j++) {
        			Matrix.get(j).remove(i);
        		}
        		Vertices.remove(i);
        		return vertex;
        	}
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEdge(E vertex1, E vertex2) {
    	if(vertex1.equals(vertex2))
    		return false;
    	
    	int count = 0;
    	int xCor = 0;
    	int yCor = 0;
    	//checks if first vertex is in graph
    	for(int i = 0; i < Vertices.size(); i++) {
    		//.equals could be causing a problem
    		if(Vertices.get(i).equals(vertex1)) {
    			count++;
    			xCor = i;
    			break;
    		}
    	}
    	//checks if second vertex is in graph
    	for(int i =0;i<Vertices.size();i++) {
    		//.equals could be causing a problem
    		if(Vertices.get(i).equals(vertex2)) {
    			count++;
    			yCor = i;
    			break;
    		}
    	}
  
    	if(count == 2) {
    		Matrix.get(xCor).set(yCor,1);
    		Matrix.get(yCor).set(xCor, 1);
    		return true;
    	}
        return false;
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeEdge(E vertex1, E vertex2) {
    	if(vertex1.equals(vertex2))
    		return false;
    	
    	int count = 0;
    	int xCor = 0;
    	int yCor = 0;
    	//checks if first vertex is in graph
    	for(int i =0;i<Vertices.size();i++) {
    		//.equals could be causing a problem
    		if(Vertices.get(i).equals(vertex1)) {
    			count++;
    			xCor = i;
    		}
    	}
    	//checks if second vertex is in graph
    	for(int i =0;i<Vertices.size();i++) {
    		//.equals could be causing a problem
    		if(Vertices.get(i).equals(vertex2)) {
    			count++;
    			yCor = i;
    		}
    	}
  
    	if(count == 2) {
    		Matrix.get(xCor).set(yCor,0);
    		Matrix.get(yCor).set(xCor,0);
    		return true;
    	}
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAdjacent(E vertex1, E vertex2) {
    	if(vertex1.equals(vertex2))
    		return false;
    	
    	int count = 0;
    	int xCor = 0;
    	int yCor = 0;
    	//checks if first vertex is in graph
    	for(int i =0;i<Vertices.size();i++) {
    		//.equals could be causing a problem
    		if(Vertices.get(i).equals(vertex1)) {
    			count++;
    			xCor = i;
    		}
    	}
    	//checks if second vertex is in graph
    	for(int i =0;i<Vertices.size();i++) {
    		//.equals could be causing a problem
    		if(Vertices.get(i).equals(vertex2)) {
    			count++;
    			yCor = i;
    		}
    	}
  
    	if(count >= 2) {
    		if(Matrix.get(xCor).get(yCor) == 1)    			
    		    return true;
    	}
        return false;  	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<E> getNeighbors(E vertex) {
    	ArrayList<E> neighbors = new ArrayList<E>();
    	int index = 0;
    	for(int i =0; i<Vertices.size(); i++) {
    		if(Vertices.get(i).equals(vertex))
    			index = i;
    	}
    	
    	for(int i=0; i<Matrix.get(index).size();i++) {
    		if(Matrix.get(index).get(i)==1) {
    			neighbors.add(Vertices.get(i));
    		}
    	}
    	
    	return neighbors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<E> getAllVertices() {
    	return Vertices;		   
    }

}
