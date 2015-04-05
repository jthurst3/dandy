public enum NonterminalType {
	Method,
	FunctionStart,
	VarName,
	FnName,
	VarTail,
	VarList,
	Statement,
	IfStatement,
	CallStmt,
	Expr,
	ExprTail,
	SumExpr,
	SumExprList,
	ProdExpr,
	ProdExprList,
	Atom,
	AssignStatement,
	BlockStmt,
	StmtList,
	ElseBlock,
	FunctionCall;

	public TokenType[] first, follow;


	/*public NonterminalType(first, follow) {
		this.first = first;
		this.follow = follow;
	}*/
}