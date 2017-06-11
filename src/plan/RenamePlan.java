package plan;

import java.util.LinkedList;

import prototype.Schema;
import scan.RenameScan;
import scan.Scan;

public class RenamePlan implements Plan {
	Plan fatherPlan;
	Plan subPlan;
	String alias;
	
	public RenamePlan(Plan plan, String as) {
		subPlan = plan;
		alias = as;
		subPlan.setFather(this);
	}
	
	@Override
	public Scan start() throws Exception {
		return new RenameScan(subPlan.start(), alias);
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
	public Plan setFather(Plan fa) {
		fatherPlan = fa;
		return this;
	}

	@Override
	public String toString() {
		return "Rename ";
	}

	@Override
	public Plan getFather() {
		return fatherPlan;
	}

}
