package scan;

import type.Type;

public class SelectScan implements Scan {
	Scan sub;
	// more powerful select
	
	public SelectScan(Scan scan) {
		sub = scan;
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
	public Type getValue(int index) {
		return sub.getValue(index);
	}

	@Override
	public Type getValue(String col) {
		return sub.getValue(col);
	}
	
	@Override
	public Type getValue(String tbl, String col) {
		return sub.getValue(tbl, col);
	}

	@Override
	public int length() {
		return sub.length();
	}
}
