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
	SumExprTail,
	SumExprList,
	ProdExpr,
	ProdExprTail,
	ProdExprList,
	Atom,
	AssignStatement,
	BlockStmt,
	StmtList;

	public TokenType[] first, follow;


	/*public NonterminalType(first, follow) {
		this.first = first;
		this.follow = follow;
	}*/
}