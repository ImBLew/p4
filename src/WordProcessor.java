import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.*;

/**
 * This class contains some utility helper methods
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class WordProcessor {
	
	/**
	 * Gets a Stream of words from the filepath.
	 * 
	 * The Stream should only contain trimmed, non-empty and UPPERCASE words.
	 * 
	 * @see <a href="http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html">java8 stream blog</a>
	 * 
	 * @param filepath file path to the dictionary file
	 * @return Stream<String> stream of words read from the filepath
	 * @throws IOException exception resulting from accessing the filepath
	 */
	public static Stream<String> getWordStream(String filepath) throws IOException {
		/**
		 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html">java.nio.file.Files</a>
		 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/nio/file/Paths.html">java.nio.file.Paths</a>
		 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html">java.nio.file.Path</a>
		 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html">java.util.stream.Stream</a>
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
		 * 		1. map( )      [changes a line to the result of the applied function. Mathematically, line = operation(line)]
		 * 			-  trim all the lines
		 * 			-  convert all the lines to UpperCase
		 * 			-  example takes each of the lines one by one and apply the function toString on them as line.toString() 
		 * 			   and returns the Stream:
		 * 			        streamOfLines = streamOfLines.map(String::toString) 
		 * 
		 * 		2. filter( )   [keeps only lines which satisfy the provided condition]  
		 *      	-  can be used to only keep non-empty lines and drop empty lines
		 *      	-  example below removes all the lines from the Stream which do not equal the string "apple" 
		 *                 and returns the Stream:
		 *      			streamOfLines = streamOfLines.filter(x -> x != "apple");
		 *      			 
		 * 		3. collect( )  [collects all the lines into a java.util.List object]
		 * 			-  can be used in the function which will invoke this method to convert Stream<String> of lines to List<String> of lines
		 * 			-  example below collects all the elements of the Stream into a List and returns the List:
		 * 				List<String> listOfLines = streamOfLines.collect(Collectors::toList); 
		 * 
		 * Note: since map and filter return the updated Stream objects, they can chained together as:
		 * 		streamOfLines.map(...).filter(a -> ...).map(...) and so on
		 */
	    
	    Stream<String> stream = Files.lines(Paths.get(filepath)).filter(x-> !x.equals("") || x!=null).map(String::trim).map(String::toUpperCase);

	    return stream;
	}
	
	/**
	 * Adjacency between word1 and word2 is defined by:
	 * if the difference between word1 and word2 is of
	 * 	1 char replacement
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
        int numDiffChar = 0;

	    if (word1.equals(word2)) {
	        return false;
	    }
	    
	    int lengthDiff = Math.abs(word1.length() - word2.length());
	    if (lengthDiff > 1) {
	        return false; 
	    }
	    // Check replacement 
	    if (word1.length() == word2.length()) {
	        int wordLength = word1.length();
	        
	        for (int i = 0, j = wordLength -1, counter = 0; counter <= wordLength/2; i++, j--, counter++) {
	            char left1 = word1.charAt(i);
	            char right1 = word1.charAt(j);
	            char left2 = word2.charAt(i);
	            char right2 = word2.charAt(j);
	            
	            if (left1 != left2) {
	                numDiffChar++;
	            }	   
	            
	            // Avoid checking on the middle char if the length of words are odd
	            if (i != j) {
	                if (right1 != right2) {
	                    numDiffChar++;
	                }
	            }
  
	            if (numDiffChar > 1) {
	                return false;
	            }
	        }
	    }
	    
	    if (word1.length() != word2.length()) {
	        

    	    // Check addition and deletion
    	    int diffCharIndex = 0;
    	    String longerWord = word1.length() > word2.length() ? word1 : word2;
    	    String shorterWord = word1.length() < word2.length() ? word1 : word2;
    	    
            if (longerWord.length() == 2 && shorterWord.length() == 1) {
                for (int i = 0; i < longerWord.length(); i++) {
                    Character a = longerWord.charAt(i);
                    if (shorterWord.equals(a.toString())) {
                        return true;
                    }
                }
                return false;
            }
    	    
    	    // Look for the index where the two words are starting to differ
    	    for (int i = 0; i < longerWord.length(); i++) {
    	        if (word1.charAt(i) != word2.charAt(i)) {
    	            diffCharIndex = i;
    	            break;
    	        }
    	    }
    	    
    	    String beforeDiffChar1 = longerWord.substring(0, diffCharIndex);
    	    String beforeDiffChar2 = shorterWord.substring(0, diffCharIndex);
    	    String afterDiffChar1 = longerWord.substring(diffCharIndex + 1, longerWord.length());
    	    String afterDiffChar2 = shorterWord.substring(diffCharIndex, shorterWord.length());
    	    
    	    if (!(beforeDiffChar1.equals(beforeDiffChar2)) || !(afterDiffChar1.equals(afterDiffChar2))) {
    	        return false;
    	    }
	    }
    
		return true;	
	}
	
	public static void main(String[] args) {
//	    try {
//	        Stream<String> stream = getWordStream("word_list.txt");
//	        Object[] string = stream.toArray();
//	        for (Object o : string)
//	            System.out.println(o);
//	    } catch (Exception e ) { 
//	        e.printStackTrace();
//	    }

	    System.out.println(isAdjacent("A","AC"));
	    System.out.println(isAdjacent("AB","GAB"));
	    System.out.println(isAdjacent("meet","met"));
	    System.out.println(isAdjacent("met","met"));
	    System.out.println(isAdjacent("heiah","heaih"));
	    System.out.println(isAdjacent("were","where"));
	    System.out.println(isAdjacent("e","r"));

	}
	
}

