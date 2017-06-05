package scan;

import java.util.LinkedList;

import expr.Expr;
import type.Type;

public class ProjectScan implements Scan {
	Scan sub;
	LinkedList<Expr> exprList;
	
	public ProjectScan(Scan scan, LinkedList<Expr> exprs) {
		sub = scan;
		exprList = exprs;
	}

	@Override
	public void open() {
		sub.open();
	}

	@Override
	public boolean next() {
		return sub.next();
	}

	@Override
	public void close() {
		sub.close();
	}

	@Override
	public Type getValue(int index) throws Exception {
		return exprList.get(index).getValue(sub);
	}

	@Override
	public Type getValue(String col) throws Exception {
		/*
		if (schema.containsColumn(col)) {
			return sub.getValue(col);
		}
		*/
		return null;
	}

	@Override
	public Type getValue(String tbl, String col) throws Exception {
		/*
		if (schema.containsColumn(tbl, col)) {
			return sub.getValue(tbl, col);
		}
		*/
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
