package expr;

import scan.Scan;
import type.Type;

public class BinaryExpr implements Expr {
	Expr leftExpr;
	Expr rightExpr;
	String operator;
	
	public BinaryExpr(Expr left, Expr right, String op) {
		leftExpr = left;
		rightExpr = right;
		operator = op;
	}
	
	@Override
	public Type getValue(Scan s) {
		Type left = leftExpr.getValue(s);
		Type right = rightExpr.getValue(s);
		if ("+".equals(operator)) {
			return left.add(right);
		} else
		if ("-".equals(operator)) {
			return left.add(right.negate());
		} else
		if ("*".equals(operator)) {
			return left.multiply(right);
		} else
		if ("/".equals(operator)) {
			return left.divide(right);
		} else
		if ("%".equals(operator)) {
			if (left.isINT() && right.isINT()) {
				return left.mod(((Integer) right.getValue()).intValue());
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return leftExpr.toString() + operator + rightExpr.toString();
	}
}
