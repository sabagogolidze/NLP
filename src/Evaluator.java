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
			vectors.add(getVector(d, topics, false));
		}
		Double[] myVec = getVector(document, topics, true);
		double maxi = Double.MIN_VALUE;
		int index = 0;
		for (int i = 0; i < vectors.size(); i++) {
			double cur = 0;
			for (int j = 0; j < vectors.get(i).length; j++) {
				cur += vectors.get(i)[j] * myVec[j];
				if (cur > maxi) {
					maxi = cur;
					index = i;
				}
			}
		}
		return graded.get(index).getGrade()
				- document.getCheckerResult().getErrors() / 4.0;
	}

	private Double[] getVector(Document d, List<String> topics, boolean flag) {
		Double[] vec = new Double[topics.size()];
		for (int i = 0; i < topics.size(); i++)
			if (flag)
				vec[Main.intTopic.get(
						Main.stTopic.get(i))] = d
						.getUsedTopicsInArticle().get(i) == null ? 0.0 : d
						.getUsedTopicsInArticle().get(i);
			else
				vec[i] = d.getUsedTopicsInArticle().get(i) == null ? 0.0 : d
						.getUsedTopicsInArticle().get(i);
		double length = 0;
		for (int i = 0; i < vec.length; i++){
			if(vec[i] == null)vec[i]=0.0;
			length += vec[i] * vec[i];
		}
		length = Math.sqrt(length);
		for (int i = 0; i < vec.length; i++)
			vec[i] /= length;
		return vec;
	}

}
