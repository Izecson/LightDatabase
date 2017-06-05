package storage;

import java.util.LinkedList;

import prototype.DatabaseException;
import type.Type;

public class DataStorageInMemory implements DataStorage {
	int cursor;
	int rowNumber;
	String tableName;
	LinkedList<LinkedList<Type>> values;
	
	public DataStorageInMemory(String name) {
		cursor = -1;
		tableName = name;
		values = new LinkedList<LinkedList<Type>>();
	}

	@Override
	public void open() {
		cursor = -1;
		rowNumber = values.size();
	}

	@Override
	public boolean next() {
		cursor++;
		return cursor < rowNumber;
	}

	@Override
	public void close() {
		cursor = -1;
		rowNumber = values.size();
	}

	@Override
	public LinkedList<Type> getRow() throws DatabaseException {
		if (cursor >= 0 && cursor < values.size()) {
			return values.get(cursor);
		} else {
			throw new DatabaseException("No more records.");
		}
	}

	@Override
	public void insertRow(LinkedList<Type> row) {
		values.add(row);
	}
}
