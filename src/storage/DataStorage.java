package storage;

import java.util.LinkedList;

import prototype.DatabaseException;
import type.Type;

public interface DataStorage {
	public void open();
	
	public void close();
	
	public int size();
	
	public LinkedList<Type> getRow(int index) throws DatabaseException;
	
	public void insertRow(LinkedList<Type> row);
	
	public void deleteRow(int index) throws DatabaseException;
}
