///////////////////////////////////////////////////////////////////////////////
//                   
// Class File:       WordProcessor.java
// Semester:         Spring 2018
//
// Author:           Yaakov Levin, Anthony Leung, Haoran Li, Ben Lewis
// Credits:          none
/////////////////////////////////////////////////////////////////////////////////
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * This class contains some utility helper methods for getting words from the file
 * 
 */
public class WordProcessor {
    
    /**
     * Gets a Stream of words from the filepath.
     * 
     * The Stream should only contain trimmed, non-empty and UPPERCASE words.
     * 
     * @param filepath file path to the dictionary file
     * @return Stream<String> stream of words read from the filepath
     * @throws IOException exception resulting from accessing the filepath
     */
    public static Stream<String> getWordStream(String filepath) throws IOException {
        /**
         * 
         * class Files has a method lines() which accepts an interface Path object and 
         * produces a Stream<String> object via which one can read all the lines from a file as a Stream.
         * 
         * class Paths has a method get() which accepts one or more strings (filepath),  
         * joins them if required and produces a interface Path object
         * 
         * Combining these two methods:
         *     Files.lines(Paths.get(<string filepath>))
         *     produces
         *         a Stream of lines read from the filepath
         * 
         * Once this Stream of lines is available, you can use the powerful operations available for Stream objects to combine 
         * multiple pre-processing operations of each line in a single statement.
         * 
         * Few of these features:
         *      1. map( )      [changes a line to the result of the applied function. Mathematically, line = operation(line)]
         *          -  trim all the lines
         *          -  convert all the lines to UpperCase
         *          -  example takes each of the lines one by one and apply the function toString on them as line.toString() 
         *             and returns the Stream:
         *                  streamOfLines = streamOfLines.map(String::toString) 
         * 
         *      2. filter( )   [keeps only lines which satisfy the provided condition]  
         *          -  can be used to only keep non-empty lines and drop empty lines
         *          -  example below removes all the lines from the Stream which do not equal the string "apple" 
         *                 and returns the Stream:
         *                  streamOfLines = streamOfLines.filter(x -> x != "apple");
         *                   
         *      3. collect( )  [collects all the lines into a java.util.List object]
         *          -  can be used in the function which will invoke this method to convert Stream<String> of lines to List<String> of lines
         *          -  example below collects all the elements of the Stream into a List and returns the List:
         *              List<String> listOfLines = streamOfLines.collect(Collectors::toList); 
         * 
         * Note: since map and filter return the updated Stream objects, they can chained together as:
         *      streamOfLines.map(...).filter(a -> ...).map(...) and so on
         */
        
        Stream<String> wordStream = Files.lines(Paths.get(filepath)).map(String::toUpperCase).filter(x -> x != "" && !x.isEmpty());
        
        return wordStream;
    }
    
    /**
     * Adjacency between word1 and word2 is defined by:
     * if the difference between word1 and word2 is of
     *  1 char replacement
     *  1 char addition
     *  1 char deletion
     * then 
     *  word1 and word2 are adjacent
     * else
     *  word1 and word2 are not adjacent
     *  
     * Note: if word1 is equal to word2, they are not adjacent
     * 
     * @param word1 first word
     * @param word2 second word
     * @return true if word1 and word2 are adjacent else false
     */
    public static boolean isAdjacent(String word1, String word2) {
        // Convert the two words into char array
        char[] w1 = word1.toCharArray();
        char[] w2 = word2.toCharArray();
        
        // If the lengths of the two words differ by more 1, then not adjacent
        if (Math.abs(w1.length - w2.length) > 1) {
            return false;
        }
        
        
        // If the two words have same length, then check for replacement 
        if (w1.length == w2.length) {
            int count = 0;
            for (int i = 0; i < w1.length; i++) {
                if (w1[i] != w2[i])
                    count++;
                if (count > 1)
                    return false;
            }
        } 
        // If the two words' length differ by 1, check for addition or deletion
        else {
            // If word2 is longer than word1
            if (w2.length > w1.length) {
                int count = 0;
                for (int i = 0; i < w1.length; i++) {
                    if (w2[i + count] != w1[i]) {
                        if (count != 0) {
                            return false;
                        } else {
                            i--;
                            count++;
                        }
                    }
                        
                }
            } 
            // If word1 is longer than word2
            else {
                int count = 0;
                for (int i = 0; i < w2.length; i++) {
                    if (w1[i + count] != w2[i]) {
                        if (count != 0) {
                            return false;
                        } else {
                            i--;
                            count++;
                        }
                    }
                }
            }
        }
        return true;    
    }
}
