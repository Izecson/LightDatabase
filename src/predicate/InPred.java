package predicate;

import expr.Expr;
import plan.Plan;
import scan.Scan;
import type.Type;

public class InPred implements Predicate {
	Expr expr;
	Plan plan;
	
	public InPred(Expr expr, Plan plan) {
		this.expr = expr;
		this.plan = plan;
	}

	@Override
	public boolean isTrue(Scan s) throws Exception {
		Type value = expr.getValue(s);
		Scan sub = plan.start();

		sub.open();
		boolean flag = false;
		while (sub.next()) {
			if (value.equals(sub.getValue(0))) {
				flag = true;
				break;
			}
		}
		sub.close();
		return flag;
	}
	
	@Override
	public String toString() {
		return expr.toString() + " IN(" + plan.toString() + ")";
	}
}
