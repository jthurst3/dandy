


/** types of tokens:
 * Integers
 * Variables and functions
 * Operators
 	=, ==, +, -, *, /, %
 * (, ), {, }, ;
 * if, elif, else
*/
public enum TokenType {
	'Integer', 'Variable', 'Function', 'Operator', 'Semicolon', 'Brace', 'Keyword', 'Undefined'
}
public class Token {
	String content;
	TokenType type;

	public Token(String content, TokenType type) {
		this.content = content;
		this.type = type;
	}


}