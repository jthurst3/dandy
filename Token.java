
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
	
	boolean hit;
	
	double x,y;
	double vel=0.0;

	public Token(String content, TokenType type) {
		this.content = content;
		this.type = type;
		this.hit = false;
	}

	public String toString() {
		return this.content;
	}
}