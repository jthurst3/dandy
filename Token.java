
import java.awt.Rectangle;

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
	Rectangle rect;
	boolean hit;

	public Token(String content, TokenType type, Rectangle rect) {
		this.content = content;
		this.type = type;
		this.rect = rect;
		this.hit = false;
	}

	public String toString() {
		return this.content;
	}


}