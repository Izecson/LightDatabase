package prototype;

import java.io.Serializable;
import java.util.LinkedList;

import type.Type;

@SuppressWarnings("serial")
public class Record implements Serializable {
	private Schema schema;
	private LinkedList<Type> values;
	
	public Record(LinkedList<Type> values, Schema sch) {
		this.values = values;
		schema = sch;
	}
	
	public Schema getSchema() {
		return schema;
	}
	
	public Type getValue(int index) {
		return values.get(index);
	}
	
	public Type getValue(String col) {
		LinkedList<String> names = schema.getColumnNames();
		for (int i = 0; i < names.size(); ++i) {
			if (names.get(i).equals(col)) {
				return getValue(i);
			}
		}
		return null;
	}
	
	public int length() {
		return values.size();
	}
}
