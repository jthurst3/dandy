


import java.util.ArrayList;
import java.awt.Rectangle;

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

	ArrayList<Token> currentTokens;
	String currentProgramString;
	int numSuccesses;
	ArrayList<Token> currentFunctions;


	public Parser() {
		currentTokens = new ArrayList<Token>();
		currentFunctions = new ArrayList<Token>();
		currentProgramString = "";
		numSuccesses = 0;
	}

	/** returns true if adding the token to the list can still make for valid syntax, false otherwise */
	public boolean addAndEvaluate(Token t) {
		add(t);
		return evaluate();
	}

	public void add(Token t) {
		currentTokens.add(t);
		currentProgramString += " " + t.content;
		if (t.type == TokenType.FUNCTION) {
			currentFunctions.add(t);
		}
	}
	/** returns true if the syntax is valid */
	public boolean evaluate() {
		return ++numSuccesses <= 5; // stub
		// see if the syntax is still valid
	}


	/** returns a new random token */
	public Token getNewRandomToken(int WIDTH, int HEIGHT, int rw, int rh) {
		// get a new random token (that's not undefined)
		// NOTE: Requires UNDEFINED to be the last value in the enumeration!
		TokenType type = TokenType.values()[(int)(Math.random() * TokenType.values().length
			/*this is the part that requires undefined to be last thing in TokenType enumeration */ - 1)];
		String content = getRandomContentFromTokenType(type);
		return new Token(content, type, new Rectangle(WIDTH,(int)(Math.random()*(HEIGHT-rh)),rw,rh));
	}

	/** returns a new random string given a token type */
	public String getRandomContentFromTokenType(TokenType type) {
		String randomVars[] = {"x", "y", "z"};
		String operatorList[] = {"=", "==", "+", "-", "*", "/", "%"};
		String brackets[] = {"(", ")", "{", "}", ";"};
		String keywords[] = {"if"};
		switch (type) {
			case INTEGER:
			return "" + (int)(Math.random() * 20);
			case VARIABLE:
			return randomFromList(randomVars);
			case FUNCTION:
			if (currentFunctions.size() == 0) {
				// return a variable if the functions thing is null
				return randomFromList(randomVars);
			} else {
				return currentFunctions.get((int)(Math.random() * currentFunctions.size())).content;
			}
			case OPERATOR:
			return randomFromList(operatorList);
			case BRACKET:
			return randomFromList(brackets);
			case KEYWORD:
			return randomFromList(keywords);
			case UNDEFINED:
			return "undefined"; // should never do this
			default: return "undefined2"; // should never get here
		}
	}

	public String randomFromList(String[] list) {
		return list[(int)(Math.random() * list.length)];
	}




}