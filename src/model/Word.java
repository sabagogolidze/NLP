package model;

public class Word {
	private int index;
	private String word;
	public Word(String word){
		this.word = word;
	}
	
	public void setTopicIndex(int index){
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
	
	public String getWord(){
		return word;
	}
}
