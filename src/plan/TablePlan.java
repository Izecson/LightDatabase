package plan;

import java.util.LinkedList;

import prototype.Schema;
import prototype.Table;
import scan.Scan;
import scan.TableScan;

public class TablePlan implements Plan {
	Plan fatherPlan;
	Table table;
	
	public TablePlan(Table tbl) {
		table = tbl;
	}
	
	@Override
	public Scan start() {
		return new TableScan(table);
	}

	@Override
	public Schema getSchema() {
		return table.getSchema();
	}

	@Override
	public LinkedList<Plan> getChildren() {
		LinkedList<Plan> ret = new LinkedList<Plan>();
		return ret;
	}

	@Override
	public Plan setChildren(LinkedList<Plan> plans) {
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
		return "Table ";
	}
}
