


import java.util.ArrayList;
import java.util.Stack;
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
		rules = new Rule[34];
		rules[0] = new Rule(NonterminalType.Method, new Object[] {NonterminalType.FunctionStart, NonterminalType.BlockStmt},
			new TokenType[] {TokenType.FUNCTION});
		rules[1] = new Rule(NonterminalType.FunctionStart, new Object[] {TokenType.FUNCTION, NonterminalType.FnName, TokenType.LPAREN, NonterminalType.VarTail},
			new TokenType[] {TokenType.FUNCTION});
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
		rules[12] = new Rule(NonterminalType.IfStatement, new Object[] {TokenType.IF, TokenType.LPAREN, NonterminalType.Expr, TokenType.RPAREN, NonterminalType.BlockStmt},
			new TokenType[] {TokenType.IF});
		rules[13] = new Rule(NonterminalType.CallStmt, new Object[] {NonterminalType.FnName, TokenType.LPAREN, NonterminalType.VarTail, TokenType.SEMICOLON},
			new TokenType[] {TokenType.FUNCTION});
		rules[14] = new Rule(NonterminalType.Expr, new Object[] {NonterminalType.SumExpr, NonterminalType.ExprTail},
			new TokenType[] {TokenType.INTEGER, TokenType.LPAREN});
		rules[15] = new Rule(NonterminalType.ExprTail, new Object[] {},
			new TokenType[] {TokenType.SEMICOLON, TokenType.RPAREN});
		rules[16] = new Rule(NonterminalType.ExprTail, new Object[] {TokenType.LESSTHAN, NonterminalType.SumExpr},
			new TokenType[] {TokenType.LESSTHAN});
		rules[17] = new Rule(NonterminalType.ExprTail, new Object[] {TokenType.GREATERTHAN, NonterminalType.SumExpr},
			new TokenType[] {TokenType.GREATERTHAN});
		rules[18] = new Rule(NonterminalType.ExprTail, new Object[] {TokenType.EQEQ, NonterminalType.SumExpr},
			new TokenType[] {TokenType.EQEQ});
		rules[19] = new Rule(NonterminalType.SumExpr, new Object[] {NonterminalType.ProdExpr, NonterminalType.SumExprTail},
			new TokenType[] {TokenType.INTEGER, TokenType.LPAREN});
		rules[20] = new Rule(NonterminalType.SumExprTail, new Object[] {},
			new TokenType[] {TokenType.LESSTHAN, TokenType.GREATERTHAN, TokenType.EQEQ, TokenType.SEMICOLON, TokenType.RPAREN});
		rules[21] = new Rule(NonterminalType.SumExprTail, new Object[] {NonterminalType.SumExprList},
			new TokenType[] {TokenType.PLUS, TokenType.MINUS});
		rules[22] = new Rule(NonterminalType.SumExprList, new Object[] {TokenType.PLUS, NonterminalType.ProdExpr, NonterminalType.SumExprList},
			new TokenType[] {TokenType.PLUS});
		rules[23] = new Rule(NonterminalType.SumExprList, new Object[] {TokenType.MINUS, NonterminalType.ProdExpr, NonterminalType.SumExprList},
			new TokenType[] {TokenType.MINUS});
		rules[24] = new Rule(NonterminalType.ProdExpr, new Object[] {NonterminalType.Atom, NonterminalType.ProdExprTail},
			new TokenType[] {TokenType.INTEGER, TokenType.LPAREN});
		rules[25] = new Rule(NonterminalType.ProdExprTail, new Object[] {},
			new TokenType[] {TokenType.PLUS, TokenType.MINUS, TokenType.LESSTHAN, TokenType.GREATERTHAN, TokenType.EQEQ, TokenType.SEMICOLON, TokenType.RPAREN});
		rules[26] = new Rule(NonterminalType.ProdExprTail, new Object[] {NonterminalType.ProdExprList},
			new TokenType[] {TokenType.TIMES});
		rules[27] = new Rule(NonterminalType.ProdExprList, new Object[] {TokenType.TIMES, NonterminalType.ProdExprList},
			new TokenType[] {TokenType.TIMES});
		rules[28] = new Rule(NonterminalType.Atom, new Object[] {TokenType.INTEGER},
			new TokenType[] {TokenType.INTEGER});
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
		parsingStack.push(NonterminalType.Method);
		for (int i = 0; i < currentTokens.size(); i++) {
			Token t = currentTokens.get(i);
			Object last = parsingStack.pop();
			while (t.type != last) {
				// see if the token type is in the predict set of the current rule
				int indexOfRule = findIndexOfRule((NonterminalType) last, t);
				if (indexOfRule == -1) {
					return false;
				}
				// add things back to the stack
				for (int j = rules[indexOfRule].predict.length - 1; j >= 0; j--) {
					parsingStack.push(rules[indexOfRule].predict[j]);
				}
				last = parsingStack.pop();
			}
			parsingStack.pop();
		}
		return true;
	}

	public int findIndexOfRule(NonterminalType last, Token t) {
		for (int i = 0; i < rules.length; i++) {
			if (rules[i].left == last && member(rules[i].predict, t.type)) {
				return i;
			}
		}
		return -1;
	}

	public boolean member(Object[] list, Object elem) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].equals(elem)) {
				return true;
			}
		}
		return false;
	}







}
