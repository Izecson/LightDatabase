package predicate;

import scan.Scan;

public class OrPred implements Predicate {
	Predicate leftPred;
	Predicate rightPred;

	public OrPred(Predicate left, Predicate right) {
		leftPred = left;
		rightPred = right;
	}

	@Override
	public boolean isTrue(Scan s) throws Exception {
		return leftPred.isTrue(s) || rightPred.isTrue(s);
	}
	
	@Override
	public String toString() {
		return leftPred.toString() + " OR " + rightPred.toString();
	}
}
