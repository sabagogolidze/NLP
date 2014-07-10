package model;

import java.util.HashMap;

public class Topic {
	private HashMap<String, Integer> wordMap;
	private int wordSum = 0;
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
		setWordSum(getWordSum() + 1);
	}
	
	public void removeWord(String word){
		wordMap.put(word, wordMap.get(word) - 1);
		setWordSum(getWordSum() - 1);
	}
	
	public int getWordCount(String word){
		if(wordMap.containsKey(word))
			return wordMap.get(word);
		else
			return 0;
	}

	public int getWordSum() {
		return wordSum;
	}

	private void setWordSum(int wordSum) {
		this.wordSum = wordSum;
	}
	
	public double getProbabaility(Word w){
		double A = 0;
		if(wordMap.get(w.getWord()) != null){
			A = wordMap.get(w.getWord());
		}
		return ((double)(A) + TopicModel.beta) / 
				((double)wordSum + TopicModel.beta * TopicModel.vocabulary);
	}
}
