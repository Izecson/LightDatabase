package scan;

import java.util.LinkedList;

import type.Type;

public class ProductScan implements Scan {
	Scan sub1;
	Scan sub2;
	
	public ProductScan(Scan scan1, Scan scan2) {
		sub1 = scan1;
		sub2 = scan2;
	}

	@Override
	public void open() {
		sub1.open();
		sub2.open();
	}

	@Override
	public boolean next() {
		if (!sub2.next()) {
			if (sub1.next()) {
				sub2.open();
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public void close() {
		sub1.close();
		sub2.close();
	}

	@Override
	public Type getValue(int index) throws Exception {
		if (index < sub1.length()) {
			return sub1.getValue(index);
		} else {
			return sub2.getValue(index - sub1.length());
		}
	}

	@Override
	public Type getValue(String col) throws Exception {
		Type ret = sub1.getValue(col);
		if (ret == null) {
			ret = sub2.getValue(col);
		}
		return null;
	}

	@Override
	public Type getValue(String tbl, String col) throws Exception {
		Type ret = sub1.getValue(tbl, col);
		if (ret == null) {
			ret = sub2.getValue(tbl, col);
		}
		return null;
	}

	@Override
	public int length() {
		return sub1.length() + sub2.length();
	}

	@Override
	public LinkedList<Type> getRow() throws Exception {
		LinkedList<Type> ret = new LinkedList<Type>();
		ret.addAll(sub1.getRow());
		ret.addAll(sub2.getRow());
		return ret;
	}
}
