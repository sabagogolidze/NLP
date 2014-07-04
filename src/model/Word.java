package model;

public class Word {
	private String word;
	private Topic topic = null;
	public Word(String word){
		this.word = word;
	}
	
	public String getWord(){
		return this.word;
	}
	
	public void setWord(String word){
		this.word = word;
	}
	
	
}
