package plan;

import java.util.LinkedList;

import database.Schema;
import scan.Scan;

public class SelectPlan implements Plan {
	Plan subPlan;
	Plan fatherPlan;
	
	public SelectPlan(Plan p) {
		subPlan = p;
		subPlan.setFather(this);
	}
	
	@Override
	public Scan start() {
		return subPlan.start();
	}

	@Override
	public Schema schema() {
		return subPlan.schema();
	}

	@Override
	public LinkedList<Plan> getChildren() {
		LinkedList<Plan> ret = new LinkedList<Plan>();
		ret.add(subPlan);
		return ret;
	}

	@Override
	public Plan setChildren(LinkedList<Plan> plans) {
		subPlan = plans.get(0);
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
