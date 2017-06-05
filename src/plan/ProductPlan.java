package plan;

import java.util.LinkedList;

import prototype.Schema;
import scan.ProductScan;
import scan.Scan;

public class ProductPlan implements Plan {
	Plan fatherPlan;
	Plan plan1;
	Plan plan2;
	
	public ProductPlan(Plan p1, Plan p2) {
		plan1 = p1;
		plan2 = p2;
		if (plan1 != null) plan1.setFather(this);
		if (plan2 != null) plan2.setFather(this);
	}
	
	@Override
	public Scan start() throws Exception {
		if (plan1 == null && plan2 != null) {
			return plan2.start();
		} else
		if (plan1 != null && plan2 == null) {
			return plan1.start();
		} else
		if (plan1 != null && plan2 != null) {
			return new ProductScan(plan1.start(), plan2.start());
		} else {
			return null;
		}
	}

	@Override
	public Schema getSchema() {
		if (plan1 == null && plan2 != null) {
			return plan2.getSchema();
		} else
		if (plan1 != null && plan2 == null) {
			return plan1.getSchema();
		} else
		if (plan1 != null && plan2 != null) {
			return plan1.getSchema().product(plan2.getSchema());
		} else {
			return null;
		}
	}

	@Override
	public LinkedList<Plan> getChildren() {
		LinkedList<Plan> plans = new LinkedList<Plan>();
		if (plan1 != null) plans.add(plan1);
		if (plan2 != null) plans.add(plan2);
		return plans;
	}

	@Override
	public Plan setChildren(LinkedList<Plan> plans) {
		if (plans.size() >= 2) {
			plan1 = plans.get(0);
			plan2 = plans.get(1);
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
		return "Product ";
	}
}
