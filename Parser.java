


import java.util.ArrayList;

public class Parser {

	public static void main(String[] args) {
		// tests the parser on example inputs
		// for (int i = 0; i < 1; i++) {
		// 	Parser p = new Parser();
		// 	Token[] tokens = new Token[] {new Token("1", TokenType.INTEGER), new Token("+", TokenType.OPERATOR), new Token("1", TokenType.INTEGER)};
		// 	for (int j = 0; j < tokens.length; j++) {
		// 		boolean status = p.addAndEvaluate(tokens[j]);
		// 		if (!status) {
		// 			System.out.println("Test failed after adding token " + tokens[j] + " on input " + p.currentTokens);
		// 		} else {
		// 			System.out.println("successfully added token " + tokens[j] + " on inut " + p.currentTokens);
		// 		}
		// 	}
		// }
		// Token t = new Token("+", TokenType.OPERATOR);
		// System.out.println(t);
	}

	ArrayList<Token> rList;


	public Parser(ArrayList<Token> rList) {
		this.rList = rList;
	}

	/** returns true if adding the token to the list can still make for valid syntax, false otherwise */
	public boolean addAndEvaluate(Token t) {
		add(t);
		return evaluate();
	}

	public void add(Token t) {
		rList.add(t);
	}
	/** returns true if the syntax is valid */
	public boolean evaluate() {
		// see if the syntax is still valid
		return true; // stub
	}




}