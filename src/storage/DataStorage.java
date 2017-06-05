package storage;

import java.util.LinkedList;

import prototype.DatabaseException;
import type.Type;

public interface DataStorage {
	public void open();
	
	public boolean next();
	
	public void close();
	
	public LinkedList<Type> getRow() throws DatabaseException;
	
	public void insertRow(LinkedList<Type> row);
}
