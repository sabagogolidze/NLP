import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Document;
import model.Topic;
import model.TopicModel;
import model.Word;




public class Main {
	
	private final static int TOPIC_COUNT = 3; 
	private static String articleDelimiter = "\nთემა:\n";
	private final static int ITERATIONS = 26;
	
	public static void main(String[] args) throws IOException {
//		String content = new Scanner(new File("input.txt")).useDelimiter("\\Z").next();
//		content = content.replaceAll("[^ა-ჰ\\d -.!?_,:;\"']", " ");
//		content = content.replaceAll("\\(", " ");
//		content = content.replaceAll("\\)", " ");
//		
//		
//		content = content.replaceAll("\\.+", ".");
//		content = content.replaceAll("[.!?;]", ".");
//		content = content.replaceAll(" +", " ");
//		content = content.replaceAll("\\. +", ".");
//		content = content.replaceAll("\\.+", ".");		
//		content = content.replaceAll("\\.", ".\n");
//		
//		content = content.replaceAll("([^ა-ჰ])(-)", " ");
//		
//		content = content.replaceAll("\\. +", ".");
//		content = content.replaceAll("\\.", ".\n");		
//		content = content.replaceAll("\n+", "\n");
//		content = content.replaceAll("(-)([^ა-ჰ])", " ");
//		
//		PrintWriter wr  = new PrintWriter(new FileWriter("output.txt"));
//		wr.append(content+"\n");
//		wr.close();
		
		Scanner sc = new Scanner(new File("input.txt"));
		String topics = sc.useDelimiter("\\Z").next();
//		System.out.println(articleCount);
		String [] splitString = topics.split(articleDelimiter);
		TopicModel topicModel = new TopicModel(ITERATIONS);
	    for(int i = 0; i < splitString.length ; i ++){
	        String article = splitString[i];
	        ArrayList<Word> words = new ArrayList<Word>();
	        StringTokenizer tk = new StringTokenizer(article,"., !?_:;)(\"\'\n\t");
	        while(tk.hasMoreElements()){
	        	String s = tk.nextToken();
//	        	System.out.println(s);
	        	words.add(new Word(s));
	        }
	        Document d = new Document(words);
	        topicModel.addDocument(d);
	        
	    }
	    
	    for(int i=0;i<TOPIC_COUNT;i++){
	    	topicModel.addTopic(new Topic());
	    }	    
		
		
		
	}
}
