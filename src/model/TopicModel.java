package model;

import java.util.ArrayList;

public class TopicModel {
	private ArrayList<Topic> topics;
	private ArrayList<Document> documents;
	private double alpha , beta;
	public TopicModel() {
		topics = new ArrayList<Topic>();
		documents = new ArrayList<Document>();
	}
	
	public void addTopic(Topic t){
		topics.add(t);
	}
	
	public void addDocument(Document d){
		documents.add(d);
	}

}
