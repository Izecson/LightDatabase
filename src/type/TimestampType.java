package type;

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class TimestampType extends BasicType {
	private Timestamp value;
	
	public TimestampType() {
		type = DataType.TIMESTAMP;
	}
	
	public TimestampType(Timestamp ts) {
		type = DataType.TIMESTAMP;
		value = ts;
	}
	
	public TimestampType(String s) {
		type = DataType.TIMESTAMP;
		if (s == null || "null".equals(s)) {
			value = null;
		} else {
			String sub = s.substring(1, s.length() - 1);
			value = Timestamp.valueOf(sub);
		}
	}
	
	@Override
	public Type setValue(String s) {
		if (s == null || "null".equals(s)) {
			value = null;
		} else {
			String sub = s.substring(1, s.length() - 1);
			value = Timestamp.valueOf(sub);
		}
		return this;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public boolean lessThan(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.before((Timestamp) value.getValue());
		}
	}

	@Override
	public boolean greaterThan(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.after((Timestamp) value.getValue());
		}
	}

	@Override
	public boolean equals(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.equals((Timestamp) value.getValue());
		}
	}

	@Override
	public Type clone() {
		return new TimestampType(value);
	}

	@Override
	public String toString() {
		if (value == null) {
			return "null";
		}
		return "'" + value.toString().substring(0, 19) + "'";
	}
	
	@Override
	public int length() {
		return 27;
	}
}
