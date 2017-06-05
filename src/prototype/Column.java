package prototype;

import java.io.Serializable;

import type.Type;

@SuppressWarnings("serial")
public class Column implements Serializable {
	private String name;
	private Type type;
	private int nextInt;
	private boolean isNullable;
	private boolean hasDefault;
	private boolean autoIncrement;

	public Column(String s) {
		name = s;
		type = null;
		nextInt = 0;
		isNullable = true;
		hasDefault = false;
		autoIncrement = false;
	}

	public Column(String s, Type t) {
		name = s;
		type = t;
		nextInt = 0;
		isNullable = true;
		hasDefault = false;
		autoIncrement = false;
	}
	
	public Column setType(Type t) {
		type = t;
		return this;
	}
	
	public Column setDefault(String s) {
		hasDefault = true;
		type.setValue(s);
		return this;
	}

	public Column setNullable(boolean x) {
		isNullable = x;
		return this;
	}
	
	public Column setAutoIncrement(boolean x) {
		autoIncrement = x;
		return this;
	}
	
	public int getNextInt() {
		return nextInt++;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isNullable() {
		return isNullable;
	}
	
	public boolean hasDefault() {
		return hasDefault;
	}
	
	public boolean autoIncrement() {
		return autoIncrement;
	}
	
	public void typeCheck() throws DatabaseException {
		if (autoIncrement && type.isINT() == false) {
			throw new DatabaseException("Auto-incrementation cannot be applied on " + type.toString() + " type.");
		}
	}
}
