


public class Rule {

	NonterminalType left;
	Object[] right;
	TokenType[] predict;

	public Rule(NonterminalType left, Object[] right, TokenType[] predict) {
		this.left = left;
		this.right = right;
		this.predict = predict;
	}



}