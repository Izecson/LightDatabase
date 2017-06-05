package prototype;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Database implements Serializable {
	String dbName;
	HashMap<String, Table> metadata;
	
	public Database(String name) {
		dbName = name;
		metadata = loadMetadata(name);
	}
	
	public Table createTable(String name, Schema sch) throws DatabaseException {
		if (!metadata.containsKey(name)) {
			Table table = new Table(name, sch);
			metadata.put(name, table);
			return table;
		} else {
			throw new DatabaseException("Table '" + name + "' already exists.");
		}
	}
	
	public void dropTable(String name) throws DatabaseException {
		if (metadata.containsKey(name)) {
			metadata.remove(name);
		} else {
			throw new DatabaseException("Table '" + name + "' doesn't exist.");
		}
	}
	
	public Table getTable(String name) throws DatabaseException {
		if (metadata.containsKey(name)) {
			return metadata.get(name);
		} else {
			throw new DatabaseException("Table '" + name + "' doesn't exist.");
		}
	}
	
	private HashMap<String, Table> loadMetadata(String name) {
		// TODO: load meta from files.
		return new HashMap<String, Table>();
	}
}
