package model;

public class Topic {
	private int wordCount = 0;

	public int getWordCount() {
		return wordCount;
	}

	public void increaseWordCount() {
		wordCount++;
	}
	
	public void decreaseWordCount() {
		wordCount--;
	}

}
