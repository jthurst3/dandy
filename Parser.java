


import java.util.ArrayList;
import java.util.Stack;
import java.awt.Rectangle;

public class Parser {

	public static void main(String[] args) {
		// tests the parser on example inputs
		Parser p = new Parser();
		ArrayList<Token> rList = new ArrayList<Token>();
		rList.add(new Token("function", TokenType.FUNCTIONSTART));
		rList.add(new Token("level" + 1, TokenType.FUNCTION));
		rList.add(new Token("(", TokenType.LPAREN));
		rList.add(new Token(")", TokenType.RPAREN));
		rList.add(new Token("{", TokenType.LBRACE));
		rList.add(new Token("answer", TokenType.VARIABLE));
		rList.add(new Token("+", TokenType.PLUS));
		rList.add(new Token("answer", TokenType.VARIABLE));
		rList.add(new Token("=", TokenType.EQ));
		rList.add(new Token("1", TokenType.INTEGER));
		rList.add(new Token("1", TokenType.INTEGER));
		rList.add(new Token("+", TokenType.PLUS));
		rList.add(new Token("1", TokenType.INTEGER));
		rList.add(new Token(";", TokenType.SEMICOLON));


		for (int i = 0; i < rList.size(); i++) {
			p.addAndEvaluate(rList.get(i));
		}
	}


	Rule[] rules;

	ArrayList<Token> currentTokens;

	String currentProgramString;
	int numSuccesses;
	ArrayList<Token> currentFunctions;


	public Parser() {
		currentTokens = new ArrayList<Token>();
		currentFunctions = new ArrayList<Token>();
		initializeRules();
		currentProgramString = "";
		numSuccesses = 0;
	}

	/** returns true if adding the token to the list can still make for valid syntax, false otherwise */
	public boolean addAndEvaluate(Token t) {
		add(t);
		return checkForSyntaxError();
	}

	public void add(Token t) {
		currentTokens.add(t);
		currentProgramString += " " + t.content;
		if (t.type == TokenType.FUNCTION) {
			currentFunctions.add(t);
		}
	}

	public void initializeRules() {
		rules = new Rule[37];
		rules[0] = new Rule(NonterminalType.Method, new Object[] {NonterminalType.FunctionStart, NonterminalType.BlockStmt},
			new TokenType[] {TokenType.FUNCTIONSTART});
		rules[1] = new Rule(NonterminalType.FunctionStart, new Object[] {TokenType.FUNCTIONSTART, NonterminalType.FnName, TokenType.LPAREN, NonterminalType.VarTail},
			new TokenType[] {TokenType.FUNCTIONSTART});
		rules[2] = new Rule(NonterminalType.VarName, new Object[] {TokenType.VARIABLE},
			new TokenType[] {TokenType.VARIABLE});
		rules[3] = new Rule(NonterminalType.FnName, new Object[] {TokenType.FUNCTION},
			new TokenType[] {TokenType.FUNCTION});
		rules[4] = new Rule(NonterminalType.VarTail, new Object[] {TokenType.RPAREN},
			new TokenType[] {TokenType.RPAREN});
		rules[5] = new Rule(NonterminalType.VarTail, new Object[] {NonterminalType.VarName, NonterminalType.VarList, TokenType.RPAREN},
			new TokenType[] {TokenType.VARIABLE});
		rules[6] = new Rule(NonterminalType.VarList, new Object[] {},
			new TokenType[] {TokenType.RPAREN});
		rules[7] = new Rule(NonterminalType.VarList, new Object[] {TokenType.COMMA, NonterminalType.VarName, NonterminalType.VarList},
			new TokenType[] {TokenType.COMMA});
		rules[8] = new Rule(NonterminalType.Statement, new Object[] {NonterminalType.IfStatement},
			new TokenType[] {TokenType.IF});
		rules[9] = new Rule(NonterminalType.Statement, new Object[] {NonterminalType.AssignStatement},
			new TokenType[] {TokenType.VARIABLE});
		rules[10] = new Rule(NonterminalType.Statement, new Object[] {NonterminalType.BlockStmt},
			new TokenType[] {TokenType.LBRACE});
		rules[11] = new Rule(NonterminalType.Statement, new Object[] {NonterminalType.CallStmt},
			new TokenType[] {TokenType.FUNCTION});
		rules[12] = new Rule(NonterminalType.IfStatement, new Object[] {TokenType.IF, TokenType.LPAREN, NonterminalType.Expr, TokenType.RPAREN, NonterminalType.BlockStmt, NonterminalType.ElseBlock},
			new TokenType[] {TokenType.IF});
		rules[13] = new Rule(NonterminalType.CallStmt, new Object[] {NonterminalType.FunctionCall, TokenType.SEMICOLON},
			new TokenType[] {TokenType.FUNCTION});
		rules[14] = new Rule(NonterminalType.Expr, new Object[] {NonterminalType.SumExpr, NonterminalType.ExprTail},
			new TokenType[] {TokenType.INTEGER, TokenType.LPAREN, TokenType.VARIABLE, TokenType.FUNCTION});
		rules[15] = new Rule(NonterminalType.ExprTail, new Object[] {},
			new TokenType[] {TokenType.SEMICOLON, TokenType.RPAREN});
		rules[16] = new Rule(NonterminalType.ExprTail, new Object[] {TokenType.LESSTHAN, NonterminalType.SumExpr},
			new TokenType[] {TokenType.LESSTHAN});
		rules[17] = new Rule(NonterminalType.ExprTail, new Object[] {TokenType.GREATERTHAN, NonterminalType.SumExpr},
			new TokenType[] {TokenType.GREATERTHAN});
		rules[18] = new Rule(NonterminalType.ExprTail, new Object[] {TokenType.EQEQ, NonterminalType.SumExpr},
			new TokenType[] {TokenType.EQEQ});
		rules[19] = new Rule(NonterminalType.SumExpr, new Object[] {NonterminalType.ProdExpr, NonterminalType.SumExprList},
			new TokenType[] {TokenType.INTEGER, TokenType.LPAREN, TokenType.VARIABLE, TokenType.FUNCTION});
		rules[20] = new Rule(NonterminalType.SumExprList, new Object[] {},
			new TokenType[] {TokenType.LESSTHAN, TokenType.GREATERTHAN, TokenType.EQEQ, TokenType.SEMICOLON, TokenType.RPAREN});
		rules[21] = new Rule(NonterminalType.SumExprList, new Object[] {TokenType.PLUS, NonterminalType.ProdExpr, NonterminalType.SumExprList},
			new TokenType[] {TokenType.PLUS});
		rules[22] = new Rule(NonterminalType.SumExprList, new Object[] {TokenType.MINUS, NonterminalType.ProdExpr, NonterminalType.SumExprList},
			new TokenType[] {TokenType.MINUS});
		rules[23] = new Rule(NonterminalType.ProdExpr, new Object[] {NonterminalType.Atom, NonterminalType.ProdExprList},
			new TokenType[] {TokenType.INTEGER, TokenType.LPAREN, TokenType.VARIABLE, TokenType.FUNCTION});
		rules[24] = new Rule(NonterminalType.ProdExprList, new Object[] {},
			new TokenType[] {TokenType.PLUS, TokenType.MINUS, TokenType.LESSTHAN, TokenType.GREATERTHAN, TokenType.EQEQ, TokenType.SEMICOLON, TokenType.RPAREN});
		rules[25] = new Rule(NonterminalType.ProdExprList, new Object[] {TokenType.TIMES, NonterminalType.ProdExprList},
			new TokenType[] {TokenType.TIMES});
		rules[26] = new Rule(NonterminalType.Atom, new Object[] {TokenType.INTEGER},
			new TokenType[] {TokenType.INTEGER});
		rules[27] = new Rule(NonterminalType.Atom, new Object[] {NonterminalType.FunctionCall},
			new TokenType[] {TokenType.FUNCTION});
		rules[28] = new Rule(NonterminalType.Atom, new Object[] {NonterminalType.VarName},
			new TokenType[] {TokenType.VARIABLE});
		rules[29] = new Rule(NonterminalType.Atom, new Object[] {TokenType.LPAREN, NonterminalType.Expr, TokenType.RPAREN},
			new TokenType[] {TokenType.LPAREN});
		rules[30] = new Rule(NonterminalType.AssignStatement, new Object[] {NonterminalType.VarName, TokenType.EQ, NonterminalType.Expr, TokenType.SEMICOLON},
			new TokenType[] {TokenType.VARIABLE});
		rules[31] = new Rule(NonterminalType.BlockStmt, new Object[] {TokenType.LBRACE, NonterminalType.StmtList, TokenType.RBRACE},
			new TokenType[] {TokenType.LBRACE});
		rules[32] = new Rule(NonterminalType.StmtList, new Object[] {},
			new TokenType[] {TokenType.RBRACE});
		rules[33] = new Rule(NonterminalType.StmtList, new Object[] {NonterminalType.Statement, NonterminalType.StmtList},
			new TokenType[] {TokenType.IF, TokenType.LBRACE, TokenType.VARIABLE, TokenType.FUNCTION});
		rules[34] = new Rule(NonterminalType.FunctionCall, new Object[] {NonterminalType.FnName, TokenType.LPAREN, NonterminalType.VarTail},
			new TokenType[] {TokenType.FUNCTION});
		rules[35] = new Rule(NonterminalType.ElseBlock, new Object[] {},
			new TokenType[] {TokenType.IF, TokenType.LBRACE, TokenType.VARIABLE, TokenType.FUNCTION, TokenType.RBRACE});
		rules[36] = new Rule(NonterminalType.ElseBlock, new Object[] {TokenType.ELSE, NonterminalType.Statement},
			new TokenType[] {TokenType.ELSE});

	}


	// /** returns a new random token */
	// public Token getNewRandomToken(int WIDTH, int HEIGHT, int rw, int rh) {
		// // get a new random token (that's not undefined)
		// // NOTE: Requires UNDEFINED to be the last value in the enumeration!
		// TokenType type = TokenType.values()[(int)(Math.random() * TokenType.values().length
			// /*this is the part that requires undefined to be last thing in TokenType enumeration */ - 1)];
		// String content = getRandomContentFromTokenType(type);
		// return new Token(content, type, new Rectangle(WIDTH,(int)(Math.random()*(HEIGHT-rh)),rw,rh));
	// }

	// /** returns a new random string given a token type */
	// public String getRandomContentFromTokenType(TokenType type) {
		// String randomVars[] = {"x", "y", "z"};
		// String operatorList[] = {"=", "==", "+", "-", "*", "/", "%"};
		// String brackets[] = {"(", ")", "{", "}", ";"};
		// String keywords[] = {"if"};
		// switch (type) {
		// 	case INTEGER:
		// 	return "" + (int)(Math.random() * 20);
		// 	case VARIABLE:
		// 	return randomFromList(randomVars);
		// 	case FUNCTION:
		// 	if (currentFunctions.size() == 0) {
		// 		// return a variable if the functions thing is null
		// 		return randomFromList(randomVars);
		// 	} else {
		// 		return currentFunctions.get((int)(Math.random() * currentFunctions.size())).content;
		// 	}
		// 	case OPERATOR:
		// 	return randomFromList(operatorList);
		// 	case BRACKET:
		// 	return randomFromList(brackets);
		// 	case KEYWORD:
		// 	return randomFromList(keywords);
		// 	case UNDEFINED:
		// 	return "undefined"; // should never do this
		// 	default: return "undefined2"; // should never get here
		// }
	// }

	// public String randomFromList(String[] list) {
		// return list[(int)(Math.random() * list.length)];
	// }



	/** Parsing logic */
	/** returns true if the syntax is still valid after adding a new token */
	public boolean checkForSyntaxError() {
		Stack<Object> parsingStack = new Stack<Object>();
		ArrayList<Token> interimTokens = new ArrayList<Token>();
		parsingStack.push(NonterminalType.Method);
		// System.out.println("stack is: " + parsingStack);
		for (int i = 0; i < currentTokens.size(); i++) {
			Token t = currentTokens.get(i);
			if (parsingStack.isEmpty() && interimTokens.size() == currentTokens.size()) {
				return true;
			}
			Object last = parsingStack.pop();
			// System.out.println("parsing token " + t + " with top of stack = " + last);
			// System.out.println("stack is starting: " + parsingStack);
			while (!(t.type.getClass().equals(last.getClass()) && t.type.name().equals(((TokenType)last).name()))) {
				// System.out.println("not a match");
				if(t.type.getClass().equals(last.getClass()) && !t.type.name().equals(((TokenType)last).name())) {
					// System.out.println("name does not match");
					return false;
				}
				// see if the token type is in the predict set of the current rule
				int indexOfRule = findIndexOfRule((NonterminalType) last, t);
				// System.out.println(indexOfRule);
				if (indexOfRule == -1) {
					return false;
				}
				// add things back to the stack
				for (int j = rules[indexOfRule].right.length - 1; j >= 0; j--) {
					parsingStack.push(rules[indexOfRule].right[j]);
				}
				// System.out.println("stack is: " + parsingStack);
				if (parsingStack.isEmpty() && interimTokens.size() == currentTokens.size()) {
					return true;
				}
				last = parsingStack.pop();
				// System.out.println("stack is: " + parsingStack);
			}
			// System.out.println("stack is done: " + parsingStack);
			interimTokens.add(currentTokens.get(i));
			// System.out.println("interim tokens is: " + interimTokens);
		}
		return true;
	}

	public int findIndexOfRule(NonterminalType last, Token t) {
		for (int i = 0; i < rules.length; i++) {
			// System.out.println("comparing nonterminals: " + rules[i].left+" and "+last + " with result " + rules[i].left.equals(last));
			if (rules[i].left.equals(last)) {
				// System.out.println("got to " + rules[i].left);
				if(member(rules[i].predict, t.type)) {
					return i;
				}
			}
		}
		return -1;
	}

	public boolean member(Object[] list, TokenType elem) {
		for (int i = 0; i < list.length; i++) {
			// System.out.println("comparing " + list[i] + " with " + elem + " with result = "  + (list[i].equals(elem)));
			if (list[i].equals(elem)) {
				return true;
			}
		}
		return false;
	}







}
