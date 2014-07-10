import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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

	private final static int TOPIC_COUNT = 2;
	// private static String articleDelimiter = "+";
	private final static int ITERATIONS = 26;
	private static String articleDelimiter = "\nთემა:\n";
	private static TopicModel topicModel = new TopicModel(ITERATIONS, 2, 0.5);
	// private static
	private static TopicModel ungradedTopicModel = new TopicModel(ITERATIONS,
			2, 0.5);

	private static void analyseGradedTopics() throws IOException {

		Scanner sc = new Scanner(new File("input.txt"));
		String topics = sc.useDelimiter("\\Z").next();
		sc.close();
		String[] splitString = topics.split(articleDelimiter);

		for (int i = 0; i < splitString.length; i++) {
			String article = splitString[i];
			Pattern pattern = Pattern.compile("(<grade>)(\\d+)(</grade>)");
			Matcher matcher = pattern.matcher(article);
			matcher.find();

			double grade = Integer.parseInt(matcher.group(2));

			article = article.replaceAll("<grade>\\d+</grade>", "");

			ArrayList<Word> words = new ArrayList<Word>();
			StringTokenizer tk = new StringTokenizer(article,
					"., !?_:;)(\"\'\n\t");
			while (tk.hasMoreElements()) {
				String s = tk.nextToken();
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
			TopicDetector topicDetector = new TopicDetector();
			String top = topicDetector.getTopic(wordGroup);
			System.out.println(topicIndex + " " + top);
			indexedTopics.put(topicIndex, top);

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
			StringTokenizer tk = new StringTokenizer(article,
					"., !?_:;)(\"\'\n\t");
			while (tk.hasMoreElements()) {
				String s = tk.nextToken();
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
			TopicDetector topicDetector = new TopicDetector();
			String top = topicDetector.getTopic(wordGroup);
			System.out.println(topicIndex + " " + top);
			indexedTopics.put(topicIndex, top);

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

	}

	public static void main(String[] args) throws IOException {

		analyseGradedTopics();
		analyseUngradedTopics();
		

	}
}