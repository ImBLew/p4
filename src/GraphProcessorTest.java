/////////////////////////////////////////////////////////////////////////////////
//                   
// Class File:       GraphProcessorTest.java
// Semester:         Spring 2018
//
// Author:           Yaakov Levin, Anthony Leung, Haoran Li, Ben Lewis
// Credits:          none
/////////////////////////////////////////////////////////////////////////////////

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Junit test class to test class GraphProcessor
 */

public class GraphProcessorTest {
    private GraphProcessor graph;
    
    @Before
    public void setUp() throws Exception {
        this.graph = new GraphProcessor();
        graph.populateGraph(System.getProperty("user.dir") + File.separatorChar + "data" + File.separatorChar + "word_list.txt");
        graph.shortestPathPrecomputation();
    }

    @After
    public void tearDown() throws Exception {
        this.graph = null;
    }
    
    /**
     * Test GraphProcessor's getShortestDistance method with four combo words based on given word list
     */
    @Test
    public final void TestShortestPathLength() {
        int path1 = graph.getShortestDistance("COMEDO", "CHARGE");
        assertEquals("The length of shortest path between COMEDO and CHARGE", 49, path1);
        
        int path2 = graph.getShortestDistance("CHARGE", "GIMLETS");
        assertEquals("The length of shortest path between CHARGE and GIMLETS", 78, path2);
        
        int path3 = graph.getShortestDistance("BELLIES", "JOLLIES");
        assertEquals("The length of shortest path between BELLIES and JOLLIES", 2, path3);
        
        int path4 = graph.getShortestDistance("DEFINE", "SHINNY");
        assertEquals("The length of shortest path between DEFINE and SHINNY", 26, path4);
    }
    
    /**
     * Test GraphProcessor's getShortestPath method with four combo words based on given word list
     */
    @Test
    public final void TestShortestPathList() {
        List<String> path1 = graph.getShortestPath("COMEDO", "CHARGE");
        assertEquals("The length of shortest path between COMEDO and CHARGE", 49, path1.size() - 1);
        
        List<String> path2 = graph.getShortestPath("CHARGE", "GIMLETS");
        assertEquals("The length of shortest path between CHARGE and GIMLETS", 78, path2.size() - 1);
        
        List<String> path3 = graph.getShortestPath("BELLIES", "JOLLIES");
        assertEquals("The length of shortest path between BELLIES and JOLLIES", 2, path3.size() - 1);
        
        List<String> path4 = graph.getShortestPath("DEFINE", "SHINNY");
        assertEquals("The length of shortest path between DEFINE and SHINNY", 26, path4.size() - 1);
    }
}
