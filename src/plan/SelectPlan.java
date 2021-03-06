package plan;

import java.util.LinkedList;

import predicate.Predicate;
import prototype.Schema;
import scan.Scan;
import scan.SelectScan;

public class SelectPlan implements Plan {
	Plan subPlan;
	Plan fatherPlan;
	Predicate pred;
	
	public SelectPlan(Plan p, Predicate pr) {
		pred = pr;
		subPlan = p;
		subPlan.setFather(this);
	}
	
	@Override
	public Scan start() throws Exception {
		return new SelectScan(subPlan.start(), pred);
	}

	@Override
	public Schema getSchema() {
		return subPlan.getSchema();
	}

	@Override
	public LinkedList<Plan> getChildren() {
		LinkedList<Plan> ret = new LinkedList<Plan>();
		ret.add(subPlan);
		return ret;
	}

	@Override
	public Plan setChildren(LinkedList<Plan> plans) {
		if (plans.size() >= 1) {
			subPlan = plans.get(0);
		}
		return this;
	}

	@Override
	public Plan getFather() {
		return fatherPlan;
	}

	@Override
	public Plan setFather(Plan fa) {
		fatherPlan = fa;
		return this;
	}

	@Override
	public String toString() {
		return "Select ";
	}
}
