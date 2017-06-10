package storage;

import java.util.LinkedList;

import prototype.DatabaseException;
import type.Type;

public class DataStorageInMemory implements DataStorage {
	String tableName;
	LinkedList<LinkedList<Type>> values;
	
	public DataStorageInMemory(String name) {
		tableName = name;
		values = new LinkedList<LinkedList<Type>>();
	}

	@Override
	public void open() {}
	
	@Override
	public void close() {}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public LinkedList<Type> getRow(int index) throws DatabaseException {
		if (index >= 0 && index < values.size()) {
			return values.get(index);
		} else {
			throw new DatabaseException("Row index out of range.");
		}
	}

	@Override
	public void insertRow(LinkedList<Type> row) {
		values.add(row);
	}

	@Override
	public void deleteRow(int index) throws DatabaseException {
		if (index >= 0 && index < values.size()) {
			values.remove(index);
		} else {
			throw new DatabaseException("Row index out of range.");
		}
	}
}
