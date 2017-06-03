package plan;

import java.util.LinkedList;

import prototype.Schema;
import scan.Scan;

public class ProductPlan implements Plan {
	Plan fatherPlan;
	LinkedList<Plan> plans;
	
	public ProductPlan(LinkedList<Plan> plans) {
		this.plans = plans;
		for (Plan p : plans) {
			p.setFather(this);
		}
	}
	
	@Override
	public Scan start() {
		// TODO: product plan start
		return null;
	}

	@Override
	public Schema schema() {
		// TODO: product plan schema
		return null;
	}

	@Override
	public LinkedList<Plan> getChildren() {
		return plans;
	}

	@Override
	public Plan setChildren(LinkedList<Plan> plans) {
		this.plans = plans;
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
