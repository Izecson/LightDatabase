package plan;

import java.util.LinkedList;

import expr.Expr;
import predicate.Predicate;
import prototype.Schema;
import prototype.Table;
import scan.Scan;
import scan.TableScan;
import type.Type;

public class UpdatePlan implements Plan {
	Table table;
	Predicate pred;
	Schema schema;
	LinkedList<String> columnList;
	LinkedList<Expr> exprList;
	
	public UpdatePlan(Table tbl, Predicate pr, LinkedList<String> cols, LinkedList<Expr> vals) {
		table = tbl;
		pred = pr;
		columnList = cols;
		exprList = vals;
		schema = table.getSchema();
	}
	
	@Override
	public Scan start() throws Exception {
		Scan scan = new TableScan(table);
		
		int rowID = 0;
		scan.open();
		while (scan.next()) {
			if (pred.isTrue(scan)) {
				LinkedList<Type> row = scan.getRow();
				for (int i = 0; i < columnList.size(); ++i) {
					int index = schema.getColumnIndex(columnList.get(i));
					if (index >= 0) {
						Type val = exprList.get(i).getValue(scan);
						row.set(index, val);
					}
				}
				table.updateRow(rowID, row);
			}
			rowID++;
		}
		
		scan.close();
		return null;
	}

	@Override
	public Schema getSchema() {
		return schema;
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
}
