package checker;
import java.util.HashMap;
import java.util.List;


public class LaplaceUnigramLanguageModel implements LanguageModel {
	int N; // size of text 
	HashMap<String, Integer> V; // V <W, number of W s in text> ;

	double SMOOSE_COEF = 0.001;
	
	/** Initialize your data structures in the constructor. */
	public LaplaceUnigramLanguageModel(HolbrookCorpus corpus) {
		V = new HashMap<String, Integer>();
		N=0;
		train(corpus);
		
	}

	/**
	 * Takes a corpus and trains your language model. Compute any counts or
	 * other corpus statistics in this function.
	 */
	public void train(HolbrookCorpus corpus) {
		for (Sentence sentence : corpus.getData()) { // iterate over sentences
			for (Datum datum : sentence) { // iterate over words
				String word = datum.getWord();
				if(word.equals("<s>")||word.equals("</s>")){
					continue;
				}
				if (V.containsKey(word)) {
					int c = V.get(word);
					V.put(word, c + 1);
				} else {
					V.put(word, 1);
				}
				N++;
			}
		}
//		System.out.println();
//		System.out.println(V.size() + " " + N);
	}

	/**
	 * Takes a list of strings as argument and returns the log-probability of
	 * the sentence using your language model. Use whatever data you computed in
	 * train() here.
	 */
	public double score(List<String> sentence) {
		double score = 0.0;
		for (String word : sentence) {
			int wordCount = 0;
			if (V.containsKey(word)) {
				wordCount = V.get(word);
			}
			
			double probability = Math.log(((double) (wordCount + SMOOSE_COEF))
					/ ((double) (N + V.size())));
			
			score += probability;
		}
//		System.out.println();
//		System.out.println();
//		System.out.println(score);
//		System.out.println();
//		System.out.println();
		return score;
	}
}
