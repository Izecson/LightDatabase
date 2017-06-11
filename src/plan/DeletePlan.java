package plan;

import java.util.LinkedList;

import predicate.Predicate;
import prototype.Schema;
import prototype.Table;
import scan.Scan;
import scan.TableScan;

public class DeletePlan implements Plan {
	Table table;
	Predicate pred;
	
	public DeletePlan(Table tbl, Predicate pr) {
		table = tbl;
		pred = pr;
	}

	@Override
	public Scan start() throws Exception {
		Scan scan = new TableScan(table);
		LinkedList<Integer> indices = new LinkedList<Integer>();
		
		int index = 0;
		scan.open();
		while (scan.next()) {
			if (pred.isTrue(scan)) {
				indices.add(index);
			}
			index++;
		}
		table.deleteRows(indices);
		scan.close();
		return null;
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
		return null;
	}

	@Override
	public Plan setFather(Plan fa) {
		return this;
	}

	@Override
	public String toString() {
		return "Delete ";
	}
}
