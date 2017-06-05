package scan;

import prototype.Table;
import type.Type;

public class TableScan implements Scan {
	Table table;

	public TableScan(Table tbl) {
		table = tbl;
	}

	@Override
	public void open() {
		table.open();
	}

	@Override
	public boolean next() {
		return table.next();
	}

	@Override
	public void close() {
		table.close();
	}

	@Override
	public int length() {
		return table.getSchema().length();
	}

	@Override
	public Type getValue(int index) {
		return table.getRecord().getValue(index);
	}

	@Override
	public Type getValue(String col) {
		return table.getRecord().getValue(col);
	}

	@Override
	public Type getValue(String tbl, String col) {
		return table.getRecord().getValue(col);
	}
}
