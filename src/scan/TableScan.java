package scan;

import prototype.Table;
import type.Type;

public class TableScan implements Scan {
	Table table;
	DataStorage storage;
	
	public TableScan(Table tbl) {
		table = tbl;
	}

	@Override
	public boolean next() {
		return storage.next();
	}

	@Override
	public void open() {
		storage = DataStorageFactory.createStorage(table);
	}

	@Override
	public void close() {
		storage.close();
	}

	@Override
	public int length() {
		return table.getSchema().length();
	}

	@Override
	public Type getValue(int index) {
		return storage.getRecord().getValue(index);
	}

	@Override
	public Type getValue(String col) {
		return storage.getRecord().getValue(col);
	}

	@Override
	public Type getValue(String tbl, String col) {
		return storage.getRecord.getValue(tbl, col);
	}
}
