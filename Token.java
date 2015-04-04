


/** types of tokens:
 * Integers
 * Variables and functions
 * Operators
 	=, ==, +, -, *, /, %
 * (, ), {, }, ;
 * if, elif, else
*/
public class Token {
	String content;
	TokenType type;

	public Token(String content, TokenType type) {
		this.content = content;
		this.type = type;
	}

	public String toString() {
		return this.content;
	}


}