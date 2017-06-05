package prototype;

import java.io.Serializable;

import storage.DataStorage;

@SuppressWarnings("serial")
public class Table implements Serializable {
	private DataStorage storage;
	private String name;
	private Schema schema;
	private String primaryKey;
	
	public Table(String tbl, Schema sch) {
		name = tbl;
		schema = sch;
		storage = createDataStorage(name);
	}
	
	public Schema getSchema() {
		return schema;
	}
	
	public Table setPrimaryKey(String key) {
		primaryKey = key;
		return this;
	}
	
	public void open() {
		storage.open();
	}
	
	public boolean next() {
		return storage.next();
	}
	
	public void close() {
		storage.close();
	}
	
	public Record getRecord() {
		return new Record(storage.getValueList(), schema);
	}
	
	private DataStorage createDataStorage(String tbl) {
		return new DataStorageInMemory(tbl);
	}
}
