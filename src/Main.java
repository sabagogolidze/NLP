import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

import model.Document;
import model.Topic;
import model.TopicModel;
import model.Word;

public class Main {

	private final static int TOPIC_COUNT = 2;
	private static String articleDelimiter = "+";
	private final static int ITERATIONS = 26;

	public static void main(String[] args) throws IOException {
		// String content = new Scanner(new
		// File("input.txt")).useDelimiter("\\Z").next();
		// content = content.replaceAll("[^ა-ჰ\\d -.!?_,:;\"']", " ");
		// content = content.replaceAll("\\(", " ");
		// content = content.replaceAll("\\)", " ");
		//
		//
		// content = content.replaceAll("\\.+", ".");
		// content = content.replaceAll("[.!?;]", ".");
		// content = content.replaceAll(" +", " ");
		// content = content.replaceAll("\\. +", ".");
		// content = content.replaceAll("\\.+", ".");
		// content = content.replaceAll("\\.", ".\n");
		//
		// content = content.replaceAll("([^ა-ჰ])(-)", " ");
		//
		// content = content.replaceAll("\\. +", ".");
		// content = content.replaceAll("\\.", ".\n");
		// content = content.replaceAll("\n+", "\n");
		// content = content.replaceAll("(-)([^ა-ჰ])", " ");
		//
		// PrintWriter wr = new PrintWriter(new FileWriter("output.txt"));
		// wr.append(content+"\n");
		// wr.close();

		Scanner sc = new Scanner(new File("input.txt"));
		String topics = sc.useDelimiter("\\Z").next(); // System.out.println(articleCount);
		StringTokenizer tk1 = new StringTokenizer(topics, articleDelimiter);
		TopicModel topicModel = new TopicModel(ITERATIONS);
		while (tk1.hasMoreElements()) {
			String article = tk1.nextToken();
			ArrayList<Word> words = new ArrayList<Word>();
			StringTokenizer tk = new StringTokenizer(article);
			while (tk.hasMoreElements()) {
				String s = tk.nextToken();
				System.out.println(s);
				words.add(new Word(s));
			}
			Document d = new Document(words);
			topicModel.addDocument(d);

		}

		for (int i = 0; i < TOPIC_COUNT; i++) {
			topicModel.addTopic(new Topic());
		}
		topicModel.doLDA();
		ArrayList<Topic> arr = topicModel.getTopics();
		for (Topic t : arr) {
			System.out.println("topic:");
			for (String s : t.getWordMap().keySet()) {
				System.out.print(s + " " + t.getWordMap().get(s) + "  |  ");
			}
			System.out.println();
		}

	}
}
