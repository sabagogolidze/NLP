import java.util.ArrayList;
import java.util.List;

import model.Document;

public class Evaluator {

	public Evaluator() {
		train();
	}

	private void train() {
		// TODO Auto-generated method stub
	}

	public double evaluate(Document document, ArrayList<Document> graded) {
		ArrayList<Double[]> vectors = new ArrayList<Double[]>();
		List<String> topics = TopicDetector.getTopics();
		for (Document d : graded) {
			vectors.add(getVector(d, topics));
		}
		Double[] myVec = getVector(document, topics);
		double maxi = Double.MIN_VALUE;
		int index = 0;
		for(int i = 0; i < vectors.size(); i++){
			double cur = 0;
			for(int j = 0; j < vectors.get(i).length; j++){
				cur += vectors.get(i)[j] * myVec[j];
				if(cur > maxi){
					maxi = cur;
					index = i;
				}
			}
		}
		return graded.get(index).getGrade() - document.getCheckerResult().getErrors() / 4.0;
	}

	private Double[] getVector(Document d, List<String> topics) {
		Double[] vec = new Double[topics.size()];
		int counter = 0;
		for (String s : topics)
			vec[counter++] = d.getUsedTopicsInArticle().get(s) == null ? 0.0
					: d.getUsedTopicsInArticle().get(s);
		double length = 0;
		for (int i = 0; i < vec.length; i++)
			length += vec[i] * vec[i];
		length = Math.sqrt(length);
		for (int i = 0; i < vec.length; i++)
			vec[i] /= length;
		return vec;
	}

}
