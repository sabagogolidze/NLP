package checker;

public class CheckerResult {
	private int errors;
	private String correctString;
	
	public CheckerResult(int errors,String correctString) {
		this.errors = errors;
		this.correctString = correctString;
	}
	
	public int getErrors() {
		return errors;
	}
	
	public String getCorrectString() {
		return correctString;
	}
	
	

}
