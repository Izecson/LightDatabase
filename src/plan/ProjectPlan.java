package plan;

import java.util.LinkedList;

import expr.Expr;
import prototype.Schema;
import scan.ProjectScan;
import scan.Scan;

public class ProjectPlan implements Plan {
	Plan fatherPlan;
	Plan subPlan;
	Schema schema;
	LinkedList<Expr> exprList;
	LinkedList<String> alias;
	
	public ProjectPlan(LinkedList<Expr> exprs, LinkedList<String> as, Plan p) {
		exprList = exprs;
		alias = as;
		subPlan = p;
		if (p != null) p.setFather(this);
		
		schema = new Schema();
		for (String name : alias) {
			if (name == null) {
				schema.add("*", "*", null);
			} else {
				schema.add("*", name, null);
			}
		}
	}
	
	@Override
	public Scan start() throws Exception {
		if (subPlan != null) {
			return new ProjectScan(subPlan.start(), schema, exprList);
		} else {
			return new ProjectScan(null, schema, exprList);
		}
	}

	@Override
	public Schema getSchema() {
		return schema;
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
		return "Project ";
	}
}
