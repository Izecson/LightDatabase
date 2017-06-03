package expr;

import scan.Scan;
import type.Type;

public class ConstExpr implements Expr {
	Type value;
	
	public ConstExpr(Type v) {
		value = v;
	}
	
	@Override
	public Type getValue(Scan s) {
		return value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
