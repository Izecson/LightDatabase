package type;

@SuppressWarnings("serial")
public class BooleanType extends BasicType {
	private Boolean value;

	public BooleanType() {
		type = DataType.BOOLEAN;
		value = null;
	}

	public BooleanType(Boolean val) {
		type = DataType.BOOLEAN;
		value = val;
	}

	public BooleanType(String s) {
		type = DataType.BOOLEAN;
		if (s == null || "null".equals(s)) {
			value = null;
		} else {
			value = Boolean.valueOf(s);
		}
	}

	@Override
	public Type setValue(String s) {
		if (s == null || "null".equals(s)) {
			value = null;
		} else {
			value = Boolean.valueOf(s);
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
			return this.value.compareTo((Boolean) value.getValue()) < 0;
		}
	}

	@Override
	public boolean greaterThan(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.compareTo((Boolean) value.getValue()) > 0;
		}
	}

	@Override
	public boolean equals(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.compareTo((Boolean) value.getValue()) == 0;
		}
	}

	@Override
	public Type clone() {
		return new BooleanType(this.value);
	}

	@Override
	public String toString() {
		if (value == null) {
			return "null";
		}
		return value.toString();
	}
	
	@Override
	public int length() {
		return 7;
	}
}
