package model;

import java.util.HashMap;

public class Topic {
	private HashMap<String, Integer> wordMap;
	
	public Topic(){
		wordMap = new HashMap<String, Integer>();
	}
	
	public HashMap<String, Integer> getWordMap(){
		return wordMap;
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
	
	public int getWordCount(String word){
		if(wordMap.containsKey(word))
			return wordMap.get(word);
		else
			return 0;
	}
}
