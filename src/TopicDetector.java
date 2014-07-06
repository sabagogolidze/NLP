import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;


public class TopicDetector {
	
	private final String trainDataDir = "topics";
	private boolean firstRun = true;
	private Map<String, Map<String, Integer>> topics;
	private Set<String> stopWords;
	
	public TopicDetector(){
		topics = new HashMap<String, Map<String, Integer>>();
		stopWords = new HashSet<String>();
	}
	
	public String getTopic(List<String> wordGroup){
		if(firstRun){
			try {
				train();
			} catch (Exception e) {
				e.printStackTrace();
			}
			firstRun = false;
		}
		// TODO
		return null;
	}


	private void train() throws Exception {
		readStopWords();
		File dir = new File(trainDataDir);
		for(File f: dir.listFiles()){
			if(f.isFile()){
				String topic = f.getName();
				System.out.println(topic);
				BufferedReader br = new BufferedReader(new FileReader(f));
				Map<String, Integer> map = new HashMap<String, Integer>();
				String line = null;
				while((line = br.readLine()) != null){
					List<String> wordList = filterStopWords(line);
					System.out.println(wordList);
					for(String word: wordList){
						if(map.containsKey(word)){
							map.put(word, map.get(word));
						}else{
							map.put(word, 1);
						}
					}
				}
				topics.put(topic, map);
				br.close();
			}
		}
	}

	private List<String> filterStopWords(String line) {
		line = filterLine(line);
		List<String> res = new ArrayList<String>();
		StringTokenizer tk = new StringTokenizer(line);
		while(tk.hasMoreTokens()){
			String word = tk.nextToken();
			if(stopWords.contains(word))
				continue;
			res.add(word);
		}
		return res;
	}

	private String filterLine(String line) {
		String res = "";
		for(char ch: line.toCharArray()){
			if((ch >= 'ა' && ch <= 'ჰ') ||ch == ' ' || ch == '-')
				res += ch;
		}
		return res;
	}

	private void readStopWords() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("geo_stopwords.txt"));
		String word = null;
		while((word = br.readLine()) != null){
			stopWords.add(word);
		}
		br.close();
	}
	
//	public static void main(String[] args) {
//		TopicDetector det = new TopicDetector();
//		det.getTopic(null);
//	}

}
