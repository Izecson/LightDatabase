package scan;

import java.util.LinkedList;

import type.Type;

public class RenameScan implements Scan {
	Scan sub;
	String alias;
	
	public RenameScan(Scan scan, String as) {
		sub = scan;
		alias = as;
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
		return sub.getValue(index);
	}

	@Override
	public Type getValue(String col) throws Exception {
		return sub.getValue(col);
	}
	
	@Override
	public Type getValue(String tbl, String col) throws Exception {
		if (alias.equals(tbl)) {
			return sub.getValue(col);
		} else {
			return sub.getValue(tbl, col);
		}
	}

	@Override
	public int length() {
		return sub.length();
	}

	@Override
	public LinkedList<Type> getRow() throws Exception {
		return sub.getRow();
	}
}
