package expr;

import java.util.LinkedList;

import plan.Plan;
import prototype.DatabaseException;
import scan.Scan;
import type.Type;

public class SubqueryExpr implements Expr {
	Plan subQuery;
	
	public SubqueryExpr(Plan sub) {
		subQuery = sub;
	}

	@Override
	public Type getValue(Scan s) throws Exception {
		Type ret = null;
		Scan scan = subQuery.start();
		scan.open();
		if (scan.next()) {
			LinkedList<Type> row = scan.getRow();
			if (row.size() != 1 || scan.next()) {
				throw new DatabaseException("Subquery must have only one element(or NULL)in the result table.");
			}
			ret = row.get(0);
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return "(" + subQuery.toString() + ")";
	}
}
