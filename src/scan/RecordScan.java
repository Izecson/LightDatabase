package scan;

import java.util.LinkedList;

import prototype.Record;
import type.Type;

public class RecordScan implements Scan {
	Record record;
	boolean start;
	
	public RecordScan(Record r) {
		record = r;
		start = false;
	}

	@Override
	public void open() {
		start = false;
	}

	@Override
	public boolean next() {
		if (start) {
			return false;
		} else {
			return start = true;
		}
	}

	@Override
	public void close() {}

	@Override
	public Type getValue(int index) throws Exception {
		if (start) {
			return record.getValue(index);
		}
		return null;
	}

	@Override
	public Type getValue(String col) throws Exception {
		if (start) {
			return record.getValue(col);
		}
		return null;
	}
	
	@Override
	public Type getValue(String tbl, String col) throws Exception {
		if (start) {
			return record.getValue(col);
		}
		return null;
	}

	@Override
	public int length() {
		return record.length();
	}

	@Override
	public LinkedList<Type> getRow() {
		return record.getValueList();
	}
}
