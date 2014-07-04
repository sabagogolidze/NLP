package model;

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
			}
		}
	}

	public void doLDA() {
		getReadyForLDA();
		for(int i = 0; i < iterations; i++){ //Main loop for lda
			for(Document document : documents){
				for(Word word : document.getWords()){
					
				}
			}
		}
	}
}
