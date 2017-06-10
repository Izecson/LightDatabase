package predicate;

import plan.Plan;
import scan.Scan;

public class ExistPred implements Predicate {
	Plan plan;
	boolean reverse;
	
	public ExistPred(Plan plan, boolean rev) {
		this.plan = plan;
		this.reverse = rev;
	}

	@Override
	public boolean isTrue(Scan s) throws Exception {
		Scan sub = plan.start();
		sub.open();
		boolean flag = sub.next();
		sub.close();
		return reverse ^ flag;
	}
	
	@Override
	public String toString() {
		if (reverse) {
			return "NOT EXISTS (" + plan.toString() + ")";
		} else {
			return "EXISTS (" + plan.toString() + ")";
		}
	}
}
