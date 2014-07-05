package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Document {

	private HashMap<Topic, Integer> topicCount;
	private ArrayList<Word> words;

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
<<<<<<< HEAD
		return ((double)(topicCount.get(t))) / ((double)(words.size() - 1));
=======
		}
		double a = (double)topicCount.get(t) + 2;
//		if(a<=0){
//			a=0.1;
//		}
		double b = (double)(words.size() - 1) + 2*2;
		
		return (a) / (b);
>>>>>>> 0bce4cad1562b57198184d513001c04c681f54bd
	}
	
}
