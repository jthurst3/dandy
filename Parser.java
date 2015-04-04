


import java.util.ArrayList;

public class Parser {

	public static void main(String[] args) {
		Token t = new Token("+", TokenType.OPERATOR);
		System.out.println(t);
	}

	ArrayList<Token> currentTokens;


	public Parser() {
		currentTokens = new ArrayList<Token>();
	}

	public void addAndEvaluate(Token t) {
		add(t);
		evaluate(t);
	}

	public void add(Token t) {
		currentTokens.add(t);
	}
	public void evaluate(Token t) {
		
	}




}