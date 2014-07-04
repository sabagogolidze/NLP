package model;

import java.util.HashMap;

public class Topic {
	private HashMap<String, Integer> wordMap;
	
	public Topic(){
		wordMap = new HashMap<String, Integer>();
	}
	
	public void addWord(String word){
		if(wordMap.containsKey(word))
			wordMap.put(word, wordMap.get(word) + 1);
		else
			wordMap.put(word, 1);
	}
	
	public void removeWord(String word){
		wordMap.put(word, wordMap.get(word) - 1);
	}
}
