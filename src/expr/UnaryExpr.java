package expr;

import scan.Scan;
import type.Type;

public class UnaryExpr implements Expr {
	Expr expr;
	String operator;
	
	public UnaryExpr(Expr e, String op) {
		expr = e;
		operator = op;
	}
	
	@Override
	public Type getValue(Scan s) {
		Type v = expr.getValue(s);
		if ("-".equals(operator)) {
			return v.negate();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return operator + expr.toString();
	}
}
