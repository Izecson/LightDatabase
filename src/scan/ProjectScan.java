package scan;

import prototype.Schema;
import type.Type;

public class ProjectScan implements Scan {
	Scan sub;
	Schema schema;
	
	public ProjectScan(Scan scan, Schema sch) {
		sub = scan;
		schema = sch;
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
		return sub.getValue(
				schema.getTableNames().get(index),
				schema.getColumnNames().get(index));
	}

	@Override
	public Type getValue(String col) throws Exception {
		if (schema.containsColumn(col)) {
			return sub.getValue(col);
		}
		return null;
	}

	@Override
	public Type getValue(String tbl, String col) throws Exception {
		if (schema.containsColumn(tbl, col)) {
			return sub.getValue(tbl, col);
		}
		return null;
	}

	@Override
	public int length() {
		return schema.length();
	}
}
