package storage;

import java.util.LinkedList;

import prototype.DatabaseException;
import type.Type;

public class DataStorageInMemory implements DataStorage {
	int cursor;
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
	}

	@Override
	public boolean next() {
		cursor++;
		return cursor < values.size();
	}

	@Override
	public void close() {
		cursor = -1;
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
