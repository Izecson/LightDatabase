package predicate;

import expr.Expr;
import scan.Scan;
import type.Type;

public class ComparePred implements Predicate {
	Expr leftExpr;
	Expr rightExpr;
	String op;
	
	public ComparePred(Expr left, Expr right, String op) {
		this.leftExpr = left;
		this.rightExpr = right;
		this.op = op;
	}
	
	@Override
	public boolean isTrue(Scan s) throws Exception {
		Type left = leftExpr.getValue(s);
		Type right = rightExpr.getValue(s);
		if ("<".equals(op)) {
			return left.lessThan(right);
		} else
		if (">".equals(op)) {
			return left.greaterThan(right);
		} else
		if ("=".equals(op)) {
			return left.equals(right);
		} else
		if ("<=".equals(op)) {
			return !left.greaterThan(right);
		} else
		if (">=".equals(op)) {
			return !left.lessThan(right);
		} else
		if ("<>".equals(op)) {
			return !left.equals(right);
		} else{
			return false;
		}
	}
	
	@Override
	public String toString() {
		return leftExpr.toString() + op + rightExpr.toString();
	}
}
