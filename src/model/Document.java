package model;

import java.util.ArrayList;
import java.util.HashMap;

import checker.CheckerResult;

public class Document {

	private HashMap<Topic, Integer> topicCount;
	private ArrayList<Word> words;
	private double grade = 0;
	private CheckerResult checkerResult = null;
	private HashMap<String, Integer> usedTopicsInArticle;
	public Document(ArrayList<Word> words) {
		this.words = words;
		topicCount = new HashMap<Topic, Integer>();
	}

	public int getSize() {
		return words.size();
	}

	public ArrayList<Word> getWords() {
		return words;
	}

	public void increaseTopicCount(Topic t) {
		if (topicCount.containsKey(t)) {
			topicCount.put(t, topicCount.get(t) + 1);
		} else {
			topicCount.put(t, 1);
		}
	}

	public void decreaseTopicCount(Topic t) {
		topicCount.put(t, topicCount.get(t) - 1);
	}
	
	public int getTopicCount(Topic t){
		if (topicCount.containsKey(t)) {
			return topicCount.get(t);
		} else {
			return 0;
		}
	}
	
	public double getTopicProbability(Topic t){
		if(topicCount.get(t) == null)
			return 0;
		double a = (double)(topicCount.get(t)) + TopicModel.alpha;
		double b = (double)(words.size() - 1) + 
				TopicModel.alpha * TopicModel.topicCount;
		return (a) / (b);
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public CheckerResult getCheckerResult() {
		return checkerResult;
	}

	public void setCheckerResult(CheckerResult checkerResult) {
		this.checkerResult = checkerResult;
	}

	public HashMap<Integer, Integer> getUsedTopicsInArticle() {
		HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
		for(int i = 0; i < this.getSize(); i++){
			if(map.containsKey(getWords().get(i).getIndex())){
				map.put(getWords().get(i).getIndex(), map.get(getWords().get(i).getIndex())+1);
			}else{
				map.put(getWords().get(i).getIndex(), 1);
			}
		}
		return map;
	}

	public void setUsedTopicsInArticle(HashMap<String, Integer> usedTopicsInArticle) {
		this.usedTopicsInArticle = usedTopicsInArticle;
	}
	
}
