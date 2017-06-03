package plan;

import java.util.LinkedList;

import database.Schema;
import database.Table;
import scan.Scan;

public class InsertPlan implements Plan {
	// Table to be inserted.
	private Table table;
	// Plan to insert.
	private Plan plan;

	public InsertPlan(Table tbl, Plan p) {
		table = tbl;
		plan = p;
		plan.setFather(this);
	}

	@Override
	public Scan start() {
		return plan.start();
	}

	@Override
	public Schema schema() {
		return table.getSchema();
	}

	@Override
	public LinkedList<Plan> getChildren() {
		LinkedList<Plan> ret = new LinkedList<Plan>();
		ret.add(plan);
		return ret;
	}

	@Override
	public void setChildren(LinkedList<Plan> plans) {
		plan = plans.get(0);
	}

	@Override
	public Plan getFather() {
		return null;
	}

	@Override
	public void setFather(Plan fa) {}

	@Override
	public String toString() {
		return "Insert ";
	}
}