package checker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StupidBackoffLanguageModel implements LanguageModel {

	HashMap<String, HashMap<String, Integer>> map; // map<W(i) , map of W(i-1) s> 
	HashMap<String, Integer> V; // V <W, number of W s in text> ;
	int N; // size of text 
	static final double STUPID_BACK = 0.4;
	/** Initialize your data structures in the constructor. */
	public StupidBackoffLanguageModel(HolbrookCorpus corpus) {
		map = new HashMap<String, HashMap<String, Integer>>();
		V = new HashMap<String, Integer>();
		N = 0;
		train(corpus);
	}

	/**
	 * Takes a corpus and trains your language model. Compute any counts or
	 * other corpus statistics in this function.
	 */
	public void train(HolbrookCorpus corpus) {
		for (Sentence sentence : corpus.getData()) { // iterate over sentences
			ArrayList<String> sen = new ArrayList<String>();
			for (Datum datum : sentence) { // iterate over words
				N++;
				String word = datum.getWord();
				sen.add(word);
				if (V.containsKey(word)) {
					int c = V.get(word);
					V.put(word, c + 1);
				} else {
					V.put(word, 1);
				}

			}
			for (int i = 1; i < sen.size(); i++) { // iterate over words
				String w = sen.get(i);
				String w_1 = sen.get(i - 1);
				if (map.containsKey(w)) {
					if (map.get(w).containsKey(w_1)) {
						int c = map.get(w).get(w_1);
						map.get(w).put(w_1, c + 1);
					} else {
						map.get(w).put(w_1, 1);
					}
				} else {
					HashMap<String, Integer> m = new HashMap<String, Integer>();
					m.put(w_1, 1);
					map.put(w, m);

				}

			}

		}
	}

	/**
	 * Takes a list of strings as argument and returns the log-probability of
	 * the sentence using your language model. Use whatever data you computed in
	 * train() here.
	 */
	public double score(List<String> sentence) {
		double score = 0.0;
		ArrayList<String> sen = new ArrayList<String>();
		for (String word : sentence) {
			sen.add(word);
		}
		for (int i = 1; i < sen.size(); i++) {
			String w = sen.get(i);
			String w_1 = sen.get(i - 1);
			if (isPair(w, w_1)) {
				double a = map.get(w).get(w_1);
				double b = V.get(w_1);
				double probability = Math.log(a / b);
				score += probability;
			} else {
				double wordCount = 0.4;
				if (V.containsKey(w)) {
					wordCount = V.get(w);
				}

				double probability = Math.log((STUPID_BACK * wordCount)
						/ ((double) N));

				score += probability;
			}
		}

		return score;

	}

	private boolean isPair(String w, String w_1) {
		if (!V.containsKey(w) || !V.containsKey(w_1)) {
			return false;
		}

		if (!map.get(w).containsKey(w_1)) {
			return false;
		}

		return true;
	}
}
