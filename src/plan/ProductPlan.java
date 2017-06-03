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
		plan1.setFather(this);
		plan2.setFather(this);
	}
	
	@Override
	public Scan start() {
		return new ProductScan(plan1.start(), plan2.start());
	}

	@Override
	public Schema schema() {
		// TODO: product plan schema
		return null;
	}

	@Override
	public LinkedList<Plan> getChildren() {
		LinkedList<Plan> plans = new LinkedList<Plan>();
		plans.add(plan1);
		plans.add(plan2);
		return plans;
	}

	@Override
	public Plan setChildren(LinkedList<Plan> plans) {
		plan1 = plans.get(0);
		plan2 = plans.get(1);
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
