package expr;

import scan.Scan;
import type.Type;

public class FieldExpr implements Expr {
	String tableName;
	String colName;
	
	public FieldExpr(String tbl, String col) {
		tableName = tbl;
		colName = col;
	}
	
	@Override
	public Type getValue(Scan s) {
		// TODO: get scan result.
		return null;
	}
	
	@Override
	public String toString() {
		return tableName + "." + colName;
	}
}
