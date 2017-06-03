package type;

import database.DatabaseException;

@SuppressWarnings("serial")
public class VarcharType extends BasicType {
	private int capacity;
	private int length;
	private String value;
	
	public VarcharType(int cap) {
		type = DataType.VARCHAR;
		capacity = cap;
		length = 0;
	}
	
	public VarcharType(int cap, String s) throws DatabaseException {
		type = DataType.VARCHAR;
		capacity = cap;
		if (s == null || "null".equals(s)) {
			value = null;
			length = 0;
		} else {
			String sub = s.substring(1, s.length() - 1);
			if (sub.length() > capacity) {
				throw new DatabaseException("Default string is too long.");
			}
			length = sub.length();
			value = sub;
		}
	}
	
	@Override
	public Type setValue(String s) {
		if (s == null || "null".equals(s)) {
			value = null;
			length = 0;
		} else {
			String sub = s.substring(1, s.length() - 1);
			if (sub.length() <= capacity) {
				value = sub;
				length = sub.length();
			}
		}
		return this;
	}

	@Override
	public Object getValue() {
		return value;
	}
	
	public int getLength() {
		return length;
	}

	@Override
	public boolean lessThan(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.compareTo((String) value.getValue()) < 0;
		}
	}

	@Override
	public boolean greaterThan(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.compareTo((String) value.getValue()) > 0;
		}
	}

	@Override
	public boolean equals(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.compareTo((String) value.getValue()) == 0;
		}
	}

	@Override
	public Type clone() {
		try {
			return new CharType(capacity, "'" + value + "'");
		} catch (DatabaseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toString() {
		if (value == null) {
			return "null";
		}
		return "'" + value + "'";
	}
	
	@Override
	public int length() {
		return capacity + 8;
	}
}
