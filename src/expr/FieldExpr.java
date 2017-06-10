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
	public Type getValue(Scan s) throws Exception {
		if ("$".equals(tableName) && !"$".equals("colName")) {
			return s.getValue(colName);
		} else
		if (!"$".equals(tableName) && !"$".equals("colName")) {
			return s.getValue(tableName, colName);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return tableName + "." + colName;
	}
}
