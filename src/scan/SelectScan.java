package scan;

import java.util.LinkedList;

import predicate.Predicate;
import type.Type;

public class SelectScan implements Scan {
	Scan sub;
	Predicate pred;
	
	public SelectScan(Scan scan, Predicate pr) {
		sub = scan;
		pred = pr;
	}

	@Override
	public void open() {
		sub.open();
	}

	@Override
	public boolean next() {
		try {
			while (sub.next()) {
				if (pred == null || pred.isTrue(sub)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
		return sub.getValue(tbl, col);
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
