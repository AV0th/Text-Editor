package spelling;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author Anthony Voth
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;
    

    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
	}
	
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should convert the 
	 * string to all lower case before you insert it. 
	 * 
	 * This method adds a word by creating and linking the necessary trie nodes 
	 * into the trie, as described outlined in the videos for this week. It 
	 * should appropriately use existing nodes in the trie, only creating new 
	 * nodes when necessary. E.g. If the word "no" is already in the trie, 
	 * then adding the word "now" would add only one additional node 
	 * (for the 'w').
	 * 
	 * @return true if the word was successfully added or false if it already exists
	 * in the dictionary.
	 */
	public boolean addWord(String word)	{
		
		if (word.isEmpty()) {
			return false;
		}
		
		String lowerWord = word.toLowerCase();
		
		char[] charArray = lowerWord.toCharArray();
		TrieNode newNode = root;
		TrieNode holderNode;
		
		for (char c : charArray) {
			
			holderNode = newNode.getChild(c);
			
			if (holderNode == null) {
				newNode.insert(c);
				holderNode = newNode.getChild(c);
			}
			
			newNode = holderNode;
		}
	    
		if (newNode.endsWord()) {
			return false;
		} else {
			newNode.setEndsWord(true);
			size++;
		    return true;
		}
		
		
	}
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size() {
	    return size;
	}
	
	
	@Override
	public boolean isWord(String s) {
		if (s.isEmpty()) {
			return false;
		}
		
		String lowerWord = s.toLowerCase();
		
		char[] charArray = lowerWord.toCharArray();
		TrieNode newNode = root;
		TrieNode holderNode;
		
		for (char c : charArray) {
			holderNode = newNode.getChild(c);
			
			if (holderNode == null) {
				return false;
			}
			
			newNode = holderNode;
		}
	    
		if (newNode.endsWord()) {
		    return true;
		} else {
			return false;
		}
	}

	/** 
     * Return a list, in order of increasing (non-decreasing) word length,
     * containing the numCompletions shortest legal completions 
     * of the prefix string. All legal completions must be valid words in the 
     * dictionary. If the prefix itself is a valid word, it is included 
     * in the list of returned words. 
     * 
     * The list of completions must contain 
     * all of the shortest completions, but when there are ties, it may break 
     * them in any order. For example, if there the prefix string is "ste" and 
     * only the words "step", "stem", "stew", "steer" and "steep" are in the 
     * dictionary, when the user asks for 4 completions, the list must include 
     * "step", "stem" and "stew", but may include either the word 
     * "steer" or "steep".
     * 
     * If this string prefix is not in the trie, it returns an empty list.
     * 
     * @param prefix The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to numCompletions best predictions
     */
	@Override
     public List<String> predictCompletions(String prefix, int numCompletions) {
    	 
//       Create a list of completions to return (initially empty)
    	 List<String> completions = new ArrayList<String>();
    	 if (numCompletions == 0) {
    		// Find the stem in the trie.  If the stem does not appear in the trie, return an empty list
    		 return completions;
    	 }
    	
    	 String lowerPrefix = prefix.toLowerCase();
         Queue <TrieNode> holderQueue = new LinkedList<TrieNode>();
         TrieNode holderNode = root;
         
         //Once the stem is found, perform a breadth first search to generate completions

         
         TrieNode secondHolderNode = new TrieNode();
         if (prefix.isEmpty()) {
        	 //if given nothing to start with, add all initial children to queue
        	 for (char c : holderNode.getValidNextCharacters()) {
        		 secondHolderNode = holderNode.getChild(c);
        		 holderQueue.add(secondHolderNode);
        	 }
         } else {
        	 //if given one staring point word
	         for (int i = 0; i < lowerPrefix.length(); i++ ) {
	        	 
	        	 char holderChar = lowerPrefix.charAt(i);
	        	 
	        	 if (holderNode.getChild(holderChar) != null) {
	        		 holderNode = holderNode.getChild(holderChar);
	        	 }
	        	 
	         }
	       //add the node that completes the stem to the back       of the list.    
	        if (holderNode != root) {
	        	holderQueue.add(holderNode);
	        }
         }	 

//       While the queue is not empty and you don't have enough completions:
         while (!holderQueue.isEmpty() && completions.size() < numCompletions) {
//           remove the first Node from the queue
        	 holderNode = holderQueue.remove();
//           If it is a word, add it to the completions list
        	 if (holderNode != null && holderNode.endsWord()) {
        		 completions.add(holderNode.getText());
        	 }
        	 
//           Add all of its child nodes to the back of the queue
        	 
        	 for (char c : holderNode.getValidNextCharacters()) {
        		 secondHolderNode = holderNode.getChild(c);
        		 holderQueue.add(secondHolderNode);
        	 }
         }
    			 
      // Return the list of completions	 
    	 return completions;
     }

 	// For debugging
 	public void printTree() {
 		printNode(root);
 	}

 	public void printNode(TrieNode curr) {
 		if (curr == null) 
 			return;
 		
 		System.out.println(curr.getText());
 		if (curr.endsWord()) {
 			System.out.println("ends word");
 		}
 		
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}