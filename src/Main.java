import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

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

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(new File("input.txt"));
		String topics = sc.useDelimiter("\\Z").next();
		sc.close();
		String[] splitString = topics.split(articleDelimiter);
		TopicModel topicModel = new TopicModel(ITERATIONS, 2, 0.5);
		for (int i = 0; i < splitString.length; i++) {
			String article = splitString[i];
			ArrayList<Word> words = new ArrayList<Word>();
			StringTokenizer tk = new StringTokenizer(article,
					"., !?_:;)(\"\'\n\t");
			while (tk.hasMoreElements()) {
				String s = tk.nextToken();
				// System.out.println(s);
				words.add(new Word(s));
			}
			Document d = new Document(words);
			topicModel.addDocument(d);

		}

		Scanner sc1 = new Scanner(new File("holbrook-tagged-dev.dat"));
		String text = sc1.useDelimiter("\\Z").next();
		text = DataCollector.castToBetterString(text);
		sc1.close();
		PrintWriter wr = new PrintWriter(new FileWriter(
				"holbrook-tagged-dev.dat"));
		wr.append(text);
		wr.close();

		SpellCorrect spellCorrect = new SpellCorrect(
				"holbrook-tagged-train.dat", "holbrook-tagged-dev.dat");
		CheckerResult res = spellCorrect.eval();

		String article = res.getCorrectString();
		ArrayList<Word> words = new ArrayList<Word>();
		StringTokenizer tk = new StringTokenizer(article, "., !?_:;)(\"\'\n\t");
		while (tk.hasMoreElements()) {
			String s = tk.nextToken();

			if (!(s.equals("<s>") || s.equals("</s>"))) {
				words.add(new Word(s));
			}
		}
		Document d = new Document(words);
		topicModel.addDocument(d);

		for (int i = 0; i < TOPIC_COUNT; i++) {
			topicModel.addTopic(new Topic());
		}
		topicModel.doLDA();

		ArrayList<Topic> tops = topicModel.getTopics();

		HashMap<String, Integer> usedTopicsInArticle = new HashMap<String,Integer>();
		HashMap<Integer,String> indexedTopics = new HashMap<Integer,String>();
		
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
			System.out.println(topicIndex + " "+ top);
			indexedTopics.put(topicIndex, top);
			usedTopicsInArticle.put(top, 0);
			
		}

		for(Word w:d.getWords()){
//			System.out.println(w.getWord());
			String top = indexedTopics.get(w.getIndex());
			usedTopicsInArticle.put(top, usedTopicsInArticle.get(top)+1);
		}
		
		for (int topicIndex = 0; topicIndex < tops.size(); topicIndex++) {
			Topic t = tops.get(topicIndex);
			System.out.println("topic: " + topicIndex);
			for (String s : t.getWordMap().keySet()) {
				System.out.print(s + " " + t.getWordMap().get(s) + "  |  ");
			}
			System.out.println();
		}
		for(String top:usedTopicsInArticle.keySet()){
			System.out.println(top+ " "+usedTopicsInArticle.get(top));
		}

	}
}