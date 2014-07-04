package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TopicModel {
	private ArrayList<Topic> topics;
	private ArrayList<Document> documents;
	private HashMap<String, Integer> wordCount;
	private double alpha, beta;
	private int iterations;

	public TopicModel(int iterations) {
		topics = new ArrayList<Topic>();
		documents = new ArrayList<Document>();
		wordCount = new HashMap<String, Integer>();
		this.iterations = iterations;
	}

	public void addTopic(Topic t) {
		topics.add(t);
	}

	public void addDocument(Document d) {
		documents.add(d);
	}

	private void getReadyForLDA() {
		Random rand = new Random();
		for (Document document : documents) {
			for (Word word : document.getWords()) {
				if(wordCount.containsKey(word))
					wordCount.put(word.getWord(), wordCount.get(word) + 1);
				else
					wordCount.put(word.getWord(), 1);
				int randomTopic = rand.nextInt(topics.size());
				topics.get(randomTopic).addWord(word.getWord());
				document.increaseTopicCount(topics.get(randomTopic));
				word.setTopicIndex(randomTopic);
			}
		}
	}

	public void doLDA() {
		getReadyForLDA();
		ArrayList<Double> probs;
		for(int i = 0; i < iterations; i++){ //Main loop for lda
			for(Document document : documents){
				for(Word word : document.getWords()){
					Topic topic = topics.get(word.getIndex());
					topic.removeWord(word.getWord());
					document.decreaseTopicCount(topic);
					probs = new ArrayList<Double>();
					for(Topic t : topics)
						probs.add(getProbability(word, document, t));
					int index = getRandomTopic(probs);
					topic = topics.get(index);
					word.setTopicIndex(index);
					topic.addWord(word.getWord());
					document.increaseTopicCount(topic);
				}
			}
		}
	}
	
	private double getProbability(Word word, Document document, Topic topic){
		double prob = document.getTopicProbability(topic);
		prob *= ((double)topic.getWordCount(word.getWord()) / 
				((double)(wordCount.get(word.getWord()) - 1)));
		return prob;
	}
	
	private int getRandomTopic(ArrayList<Double> probs){
		double total = 0;
		for(double d : probs)
			total += d;
		total *= Math.random();
		for(int i = 0; i < probs.size(); i++){
			if(total <= probs.get(i))
				return i;
			total -= probs.get(i);
		}
		return probs.size() - 1;
	}
	
	public ArrayList<Topic> getTopics(){
		return topics;
	}
}
