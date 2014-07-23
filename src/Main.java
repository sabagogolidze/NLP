import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import checker.CheckerResult;
import checker.SpellCorrect;
import model.Document;
import model.Topic;
import model.TopicModel;
import model.Word;

public class Main {

	public final static int TOPIC_COUNT = 4;
	// private static String articleDelimiter = "+";
	private final static int ITERATIONS = 60;
	private static TopicDetector topicDetector = new TopicDetector();
	private static String articleDelimiter = "\nთემა:\n";
	private static TopicModel topicModel = new TopicModel(ITERATIONS, 2, 0.5);
	public static HashMap<String, Integer> intTopic = new HashMap<>();
	public static HashMap<Integer, String> stTopic = new HashMap<>();
	// private static
	private static TopicModel ungradedTopicModel = new TopicModel(ITERATIONS,
			2, 0.5);

	private static void analyseGradedTopics() throws IOException {

		Scanner sc = new Scanner(new File("input.txt"));
		String topics = sc.useDelimiter("\\Z").next();
		sc.close();
		String[] splitString = topics.split(articleDelimiter);
		System.out.println(splitString.length+"saba");
		for (int i = 0; i < splitString.length; i++) {
			String article = splitString[i];
			Pattern pattern = Pattern.compile("(<grade>)(\\d+)(</grade>)");
			Matcher matcher = pattern.matcher(article);
			matcher.find();

			double grade = Integer.parseInt(matcher.group(2));

			article = article.replaceAll("<grade>\\d+</grade>", "");
			List<String> filtered = topicDetector.filterStopWords(article);
			ArrayList<Word> words = new ArrayList<Word>();
			
			for(String s:filtered){
//				String s = tk.nextToken();
				// System.out.println(s);
				words.add(new Word(s));
			}
			Document d = new Document(words);
			d.setGrade(grade);
			topicModel.addDocument(d);

		}
		for (int i = 0; i < TOPIC_COUNT; i++) {
			topicModel.addTopic(new Topic());
		}
		topicModel.doLDA();

		ArrayList<Topic> tops = topicModel.getTopics();

		// HashMap<String, Integer> usedTopicsInArticle = new HashMap<String,
		// Integer>();
		HashMap<Integer, String> indexedTopics = new HashMap<Integer, String>();

		for (int topicIndex = 0; topicIndex < tops.size(); topicIndex++) {

			Topic t = tops.get(topicIndex);
			ArrayList<String> wordGroup = new ArrayList<String>();
			HashMap<String, Integer> wordMap = t.getWordMap();
			for (String s : wordMap.keySet()) {
				int count = wordMap.get(s);
				for (int i = 0; i < count; i++) {
					wordGroup.add(s);
				}
			}
			String top = topicDetector.getTopic(wordGroup);
			System.out.println(topicIndex + " " + top);
			indexedTopics.put(topicIndex, top);
			intTopic.put(top, topicIndex);
		}
		
		for (Topic t : tops) {
		System.out.println("topic:");
		ArrayList<String> arr1 = new ArrayList<String>();
		for (String s : t.getWordMap().keySet()) {
			System.out.print(s + " " + t.getWordMap().get(s) + "  |  ");
			arr1.add(s);
		}
		System.out.println();
	}

		ArrayList<Document> documents = topicModel.getDocuments();
		for (Document d : documents) {
			HashMap<String, Integer> usedTopicsInArticle = new HashMap<String, Integer>();
			for (Word w : d.getWords()) {
				// System.out.println(w.getWord());
				String top = indexedTopics.get(w.getIndex());
				if (usedTopicsInArticle.containsKey(top)) {
					usedTopicsInArticle.put(top,
							usedTopicsInArticle.get(top) + 1);
				} else {
					usedTopicsInArticle.put(top, 1);
				}

			}
			d.setUsedTopicsInArticle(usedTopicsInArticle);
		}

	}

	private static void analyseUngradedTopics() throws IOException {

		Scanner sc = new Scanner(new File("ungradedTopics.txt"));
		String topics = sc.useDelimiter("\\Z").next();
		sc.close();
		String[] splitString = topics.split(articleDelimiter);

		for (int i = 0; i < splitString.length; i++) {
			String article = splitString[i];

			article = DataCollector.castToBetterString(article);
			PrintWriter wr = new PrintWriter(new FileWriter(
					"holbrook-tagged-dev.dat"));
			wr.append(article);
			wr.close();

			SpellCorrect spellCorrect = new SpellCorrect(
					"holbrook-tagged-train.dat", "holbrook-tagged-dev.dat");
			CheckerResult res = spellCorrect.eval();

			ArrayList<Word> words = new ArrayList<Word>();
			List<String> filtered = topicDetector.filterStopWords(article);
			
			for(String s:filtered){
//				String s = tk.nextToken();
				// System.out.println(s);
				words.add(new Word(s));
			}
			Document d = new Document(words);
			d.setCheckerResult(res);
			ungradedTopicModel.addDocument(d);
			
		}
		for (int i = 0; i < TOPIC_COUNT; i++) {
			ungradedTopicModel.addTopic(new Topic());
		}
		ungradedTopicModel.doLDA();

		ArrayList<Topic> tops = ungradedTopicModel.getTopics();

		// HashMap<String, Integer> usedTopicsInArticle = new HashMap<String,
		// Integer>();
		HashMap<Integer, String> indexedTopics = new HashMap<Integer, String>();

		for (int topicIndex = 0; topicIndex < tops.size(); topicIndex++) {

			Topic t = tops.get(topicIndex);
			ArrayList<String> wordGroup = new ArrayList<String>();
			HashMap<String, Integer> wordMap = t.getWordMap();
			for (String s : wordMap.keySet()) {
				int count = wordMap.get(s);
				for (int i = 0; i < count; i++) {
					wordGroup.add(s);
				}
			}
			String top = topicDetector.getTopic(wordGroup);
			System.out.println(topicIndex  + top);
			indexedTopics.put(topicIndex, top);
			stTopic.put(topicIndex, top);
		}

		ArrayList<Document> documents = ungradedTopicModel.getDocuments();
		for (Document d : documents) {
			HashMap<String, Integer> usedTopicsInArticle = new HashMap<String, Integer>();
			for (Word w : d.getWords()) {
				// System.out.println(w.getWord());
				String top = indexedTopics.get(w.getIndex());
				if (usedTopicsInArticle.containsKey(top)) {
					usedTopicsInArticle.put(top,
							usedTopicsInArticle.get(top) + 1);
				} else {
					usedTopicsInArticle.put(top, 1);
				}

			}
			d.setUsedTopicsInArticle(usedTopicsInArticle);
		}
		
		for (Topic t : tops) {
			System.out.println("topic:");
			ArrayList<String> arr1 = new ArrayList<String>();
			for (String s : t.getWordMap().keySet()) {
				System.out.print(s + " " + t.getWordMap().get(s) + "  |  ");
				arr1.add(s);
			}
			System.out.println();
		}
		
	}

	public static void main(String[] args) throws IOException {
		analyseGradedTopics();
		analyseUngradedTopics();
		ArrayList<Document> graded = topicModel.getDocuments();
		ArrayList<Document> unGraded = ungradedTopicModel.getDocuments();
		Evaluator ev = new Evaluator();
		System.out.println(graded.size());
		for(Document d : unGraded){
			System.out.println(ev.evaluate(d, graded));
		}
	}
}