/////////////////////////////////////////////////////////////////////////////////
//                   
// Class File:       GraphProcessor.java
// Semester:         Spring 2018
//
// Author:           Yaakov Levin, Anthony Leung, Haoran Li, Ben Lewis
// Credits:          none
/////////////////////////////////////////////////////////////////////////////////

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class adds additional functionality to the graph as a whole.
 */
public class GraphProcessor {
    
    protected class vertexNode{
        String vertex;
        int weight;
        String predecessor;
        
        public vertexNode(String vertex) {
            this.vertex = vertex;
            weight = Integer.MAX_VALUE;
        }
        
        @Override
        public boolean equals(Object n) {
            if (n instanceof vertexNode) {
                if (((vertexNode) n).vertex == this.vertex)
                    return true;
                else
                    return false;
            } else {
                return false;
            }
        }
    }
    
    private int numOfVertices;
    
    /**
     * Data structure to hold shortest path between every vertices
     */
    HashMap<String, ArrayList<String>> shortestPath = new HashMap<String, ArrayList<String>>();
    
    /**
     * Graph which stores the dictionary words and their associated connections
     */
    private GraphADT<String> graph;

    /**
     * Constructor for this class. Initializes instances variables to set the starting state of the object
     */
    public GraphProcessor() {
        this.graph = new Graph<String>();
    }
        
    /**
     * Builds a graph from the words in a file. Populate an internal graph, by adding words from the dictionary as vertices
     * and finding and adding the corresponding connections (edges) between 
     * existing words.
     * 
     * Reads a word from the file and adds it as a vertex to a graph.
     * Repeat for all words.
     * 
     * For all possible pairs of vertices, finds if the pair of vertices is adjacent {@link WordProcessor#isAdjacent(String, String)}
     * If a pair is adjacent, adds an undirected and unweighted edge between the pair of vertices in the graph.
     * 
     * @param filepath file path to the dictionary
     * @return Integer the number of vertices (words) added
     * @throws IOException 
     */
    public Integer populateGraph(String filepath) {
        Stream<String> wordStream;
        try {
            wordStream = WordProcessor.getWordStream(filepath);
            List<String> wordString = wordStream.collect(Collectors.toList());
            this.numOfVertices = wordString.size();
            
            for(String word : wordString)
                graph.addVertex(word);
            
            for(String word1 : wordString)
                for(String word2 : wordString)
                    if (WordProcessor.isAdjacent(word1, word2))
                        graph.addEdge(word1, word2);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return numOfVertices;
    }

    
    /**
     * Gets the list of words that create the shortest path between word1 and word2
     * 
     * Example: Given a dictionary,
     *             cat
     *             rat
     *             hat
     *             neat
     *             wheat
     *             kit
     *  shortest path between cat and wheat is the following list of words:
     *     [cat, hat, heat, wheat]
     * 
     * @param word1 first word
     * @param word2 second word
     * @return List<String> list of the words
     */
    public List<String> getShortestPath(String word1, String word2) {
        return shortestPath.get(word1 + "|" + word2);
    
    }
    
    /**
     * Gets the distance of the shortest path between word1 and word2
     * 
     * Example: Given a dictionary,
     *             cat
     *             rat
     *             hat
     *             neat
     *             wheat
     *             kit
     *  distance of the shortest path between cat and wheat, [cat, hat, heat, wheat]
     *   = 3 (the number of edges in the shortest path)
     * 
     * @param word1 first word
     * @param word2 second word
     * @return Integer distance
     */
    public Integer getShortestDistance(String word1, String word2) {
        return shortestPath.get(word1 + "|" + word2).size() - 1;
    }
    
    /**
     * Computes shortest paths and distances between all possible pairs of vertices.
     * This method is called after every set of updates in the graph to recompute the path information.
     * Any shortest path algorithm can be used (Djikstra's or Floyd-Warshall recommended).
     */
    public void shortestPathPrecomputation() {
        Iterator<String> vertices = graph.getAllVertices().iterator();
        while(vertices.hasNext()) {
            String root = vertices.next();
            ArrayList<vertexNode> pathList = generatePathList(root);
            buildMap(pathList, root);
        }
    }
    
    /**
     * Using Djikstra's Algorithm to calculate the shortest path to all reachable vertices
     * 
     * @param root the starting word of path
     * @return the whole tree containing all the vertex reachable from root
     */
    private ArrayList<vertexNode> generatePathList(String root){
        ArrayList<vertexNode> table = new ArrayList<vertexNode>();
        ArrayList<vertexNode> removedNode = new ArrayList<vertexNode>();
        
        Iterator<String> vertices = graph.getAllVertices().iterator();
        while(vertices.hasNext())
            table.add(new vertexNode(vertices.next()));
        
        table.get(table.indexOf(new vertexNode(root))).weight = 0;
        
        while(!table.isEmpty()) {
            vertexNode current = table.remove(table.indexOf(Collections.min(table, new Comparator<vertexNode>() {
                public int compare(vertexNode n1, vertexNode n2) {
                    return n1.weight - n2.weight;
                }
            })));
            if (current.weight == Integer.MAX_VALUE)
                break;
            removedNode.add(current);
            Iterator<String> successors = graph.getNeighbors(current.vertex).iterator();
            while(successors.hasNext()) {
                String temp = successors.next();
                if (removedNode.contains(new vertexNode(temp)))
                    continue;
                int position = table.indexOf(new vertexNode(temp));
                if (table.get(position).weight > (current.weight + 1)) {
                    table.get(position).predecessor = current.vertex;
                    table.get(position).weight = current.weight + 1;
                }
            }
        }
        return removedNode;
    }
    
    /**
     * build the path list from starting word to target word from the given tree
     * 
     * @param pathList the whole tree containing all the vertex reachable from root
     * @param root the starting word of path
     */
    private void buildMap(ArrayList<vertexNode> pathList, String root) {
        Iterator<String> vertices = graph.getAllVertices().iterator();
        while(vertices.hasNext()) {
            ArrayList<String> wordList = new ArrayList<String>();
            String target = vertices.next();
            if (pathList.contains((new vertexNode(target)))) {
                String predecessor = target;
                while(predecessor != null) {
                    wordList.add(predecessor);
                    predecessor = pathList.get(pathList.indexOf(new vertexNode(predecessor))).predecessor;
                }
                Collections.reverse(wordList);
                shortestPath.put(root + "|" + target, wordList);
            }
        }
    }
}
