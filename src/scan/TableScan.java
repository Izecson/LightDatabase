package scan;

import java.util.LinkedList;

import prototype.Table;
import type.Type;

public class TableScan implements Scan {
	int cursor;
	int size;
	Table table;

	public TableScan(Table tbl) {
		table = tbl;
	}

	@Override
	public void open() {
		cursor = -1;
		size = table.size();
		table.open();
	}

	@Override
	public boolean next() {
		++cursor;
		return cursor < size;
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
	public Type getValue(int index) throws Exception {
		return table.getRecord(cursor).getValue(index);
	}

	@Override
	public Type getValue(String col) throws Exception {
		return table.getRecord(cursor).getValue(col);
	}

	@Override
	public Type getValue(String tbl, String col) throws Exception {
		if (table.getName().equals(tbl)) {
			return table.getRecord(cursor).getValue(col);
		} else {
			return null;
		}
	}

	@Override
	public LinkedList<Type> getRow() throws Exception {
		return table.getRecord(cursor).getValueList();
	}
}
