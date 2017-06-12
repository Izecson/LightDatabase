package scan;

import java.util.LinkedList;

import expr.Expr;
import prototype.Schema;
import type.Type;

public class ProjectScan implements Scan {
	Scan sub;
	Schema schema;
	LinkedList<Expr> exprList;
	boolean start;
	
	public ProjectScan(Scan scan, Schema sch, LinkedList<Expr> exprs) {
		sub = scan;
		schema = sch;
		exprList = exprs;
	}

	@Override
	public void open() {
		if (sub != null) sub.open();
		else start = false;
	}

	@Override
	public boolean next() {
		if (sub != null) return sub.next();
		if (!start) return start = true;
		return false;
	}

	@Override
	public void close() {
		if (sub != null) sub.close();
		else start = false;
	}

	@Override
	public Type getValue(int index) throws Exception {
		return exprList.get(index).getValue(sub);
	}

	@Override
	public Type getValue(String col) throws Exception {
		int ind = schema.getColumnIndex(col);
		if (ind >= 0) {
			return getValue(ind);
		}
		return null;
	}

	@Override
	public Type getValue(String tbl, String col) throws Exception {
		int ind = schema.getColumnIndex(tbl, col);
		if (ind >= 0) {
			return getValue(ind);
		}
		return null;
	}

	@Override
	public int length() {
		return exprList.size();
	}

	@Override
	public LinkedList<Type> getRow() {
		int length = length();
		LinkedList<Type> ret = new LinkedList<Type>();
		for (int i = 0; i < length; ++i) {
			try {
				ret.add(getValue(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
}
