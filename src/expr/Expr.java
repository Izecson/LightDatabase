package expr;

import scan.Scan;
import type.Type;

public interface Expr {
	public Type getValue(Scan s) throws Exception;
	
	public String toString();
}
