package predicate;

import expr.Expr;
import plan.Plan;
import scan.Scan;
import type.Type;

public class AllPred implements Predicate {
	Expr leftExpr;
	Plan plan;
	String op;
	
	public AllPred(Expr left, Plan plan, String op) {
		this.leftExpr = left;
		this.plan = plan;
		this.op = op;
	}

	@Override
	public boolean isTrue(Scan s) throws Exception {
		Type left = leftExpr.getValue(s);
		Scan sub = plan.start();
		
		sub.open();
		boolean flag = true;
		while (sub.next()) {
			if ("<".equals(op)) {
				flag &= left.lessThan(sub.getValue(0));
			} else
			if (">".equals(op)) {
				flag &= left.greaterThan(sub.getValue(0));
			} else
			if ("=".equals(op)) {
				flag &= left.equals(sub.getValue(0));
			} else
			if ("<=".equals(op)) {
				flag &= !left.greaterThan(sub.getValue(0));
			} else
			if (">=".equals(op)) {
				flag &= !left.lessThan(sub.getValue(0));
			} else
			if ("<>".equals(op)) {
				flag &= !left.equals(sub.getValue(0));
			}
			
			if (!flag) break;
		}
		sub.close();
		return flag;
	}

	@Override
	public String toString() {
		return leftExpr.toString() + op + "ALL(" + plan.toString() + ")";
	}
}
