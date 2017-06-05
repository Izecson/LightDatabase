package prototype;

import java.util.HashMap;

public class DatabaseManager {
	private Database db;
	private HashMap<String, Database> baseMap = new HashMap<String, Database>();
	
	public Database getDatabase() throws DatabaseException {
		if (db == null) {
			throw new DatabaseException("Database doesn't exists.");
		}
		return db;
	}
	
	public void createDatabase(String name) throws DatabaseException {
		if (baseMap.containsKey(name)) {
			throw new DatabaseException("Database '" + name + "' already exists.");
		} else {
			baseMap.put(name, new Database(name));
		}
	}
	
	public void useDatabase(String name) throws DatabaseException {
		if (baseMap.containsKey(name)) {
			db = baseMap.get(name);
		} else {
			throw new DatabaseException("Database '" + name + "' doesn't exist.");
		}
	}
	
	public void dropDatabase(String name) throws DatabaseException {
		if (baseMap.containsKey(name)) {
			baseMap.remove(name);
		} else {
			throw new DatabaseException("Database '" + name + "' doesn't exist.");
		}
	}
	
	public boolean contains(String name) {
		return baseMap.containsKey(name);
	}
}
