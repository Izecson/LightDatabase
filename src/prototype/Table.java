package prototype;

import java.io.Serializable;
import java.util.LinkedList;

import storage.DataStorage;
import storage.DataStorageInMemory;
import type.Type;

@SuppressWarnings("serial")
public class Table implements Serializable {
	private DataStorage storage;
	private String name;
	private Schema schema;
	private int primaryKey;
	
	public Table(String tbl, Schema sch) {
		name = tbl;
		schema = sch;
		primaryKey = -1;
		storage = createDataStorage(name);
	}
	
	public String getName() {
		return name;
	}
	
	public Schema getSchema() {
		return schema;
	}
	
	public Table setPrimaryKey(String key) {
		primaryKey = schema.getColumnIndex(key);
		return this;
	}

	public void open() {
		storage.open();
	}
	
	public void close() {
		storage.close();
	}

	public int size() {
		return storage.size();
	}

	public Record getRecord(int index) throws DatabaseException {
		return new Record(storage.getRow(index), schema);
	}
	
	public void insertRow(LinkedList<Type> value) throws DatabaseException {
		// Size check.
		if (schema.length() != value.size()) {
			throw new DatabaseException("Insert Error: the insert record must be of the same format as defined.");
		}
		// Type check.
		LinkedList<Column> cols = schema.getColumnList();
		for (int i = 0; i < cols.size(); ++i) {
			Column col = cols.get(i);
			Type val = value.get(i);
			if (!val.equalsType(col.getType())) {
				throw new DatabaseException("Insert Error: the insert record must be of the same format as defined.");
			}
		}
		// TODO: primary key check.
		
		storage.insertRow(value);
		// Auto-increment
		for (int i = 0; i < cols.size(); ++i) {
			Column col = cols.get(i);
			if (col.autoIncrement()) {
				col.updateNextInt(value.get(i));
			}
		}
	}
	
	public void deleteRows(LinkedList<Integer> indices) throws DatabaseException {
		for (int i = 0; i < indices.size(); ++i) {
			storage.deleteRow(indices.get(i).intValue() - i);
		}
	}
	
	public void updateRow(int index, LinkedList<Type> value) throws DatabaseException {
		storage.deleteRow(index);
		insertRow(value);
	}
	
	private DataStorage createDataStorage(String tbl) {
		return new DataStorageInMemory(tbl);
	}
}
