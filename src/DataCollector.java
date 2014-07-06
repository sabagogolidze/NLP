import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class DataCollector {
	
	
	public static void main(String[] args) throws IOException {
		String content = new Scanner(new File("rawData.txt")).useDelimiter("\\Z").next();
		content = content.replaceAll("-\r\n", "");
		content = content.replaceAll("\n", "NO");
		content = content.replaceAll("[^ა-ჰ\\d -.!?_,:;\"']", " ");
		content = content.replaceAll("\\(", " ");
		content = content.replaceAll("\\)", " ");
		
		content = content.replaceAll("\\.+", ".");
		content = content.replaceAll("[.!?;]", ".");
		content = content.replaceAll("_", "");
		content = content.replaceAll(" +", " ");
		content = content.replaceAll("\\. +", ".");
		content = content.replaceAll("\\.+", ".");
		content = content.replaceAll("\\.", ".\n");

		content = content.replaceAll("([^ა-ჰ])(-)", " ");

		content = content.replaceAll("\\. +", ".");
		content = content.replaceAll("\\.", ".\n");
		content = content.replaceAll("\n+", "\n");
		content = content.replaceAll("(-)([^ა-ჰ])", " ");
		

		PrintWriter wr = new PrintWriter(new FileWriter("output.txt"));
		wr.append(content + "\n");
		wr.close();
	}
}
