package type;

@SuppressWarnings("serial")
public class IntType extends BasicType {
	private Integer value;
	
	public IntType() {
		type = DataType.INT;
	}
	
	public IntType(Integer val) {
		type = DataType.INT;
		value = val;
	}
	
	public IntType(String s) {
		type = DataType.INT;
		if (s == null || "null".equals(s)) {
			value = null;
		} else {
			value = Integer.valueOf(s);
		}
	}
	
	@Override
	public Type setValue(String s) {
		if (s == null || "null".equals(s)) {
			value = null;
		} else {
			value = Integer.valueOf(s);
		}
		return this;
	}
	
	public Type setValue(int v) {
		value = Integer.valueOf(v);
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
			return this.value.compareTo((Integer) value.getValue()) < 0;
		}
	}

	@Override
	public boolean greaterThan(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.compareTo((Integer) value.getValue()) > 0;
		}
	}

	@Override
	public boolean equals(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.compareTo((Integer) value.getValue()) == 0;
		}
	}

	@Override
	public Type clone() {
		return new IntType(this.value);
	}

	@Override
	public String toString() {
		if (value == null) {
			return "null";
		}
		return value.toString();
	}

	@Override
	public Type add(Type val) {
		if (this.value == null || val.getValue() == null) {
			return null;
		}
		return new IntType(Integer.valueOf(this.value.intValue() + (Integer) val.getValue()));
	}

	@Override
	public Type divide(int count) {
		if (this.value == null) {
			return null;
		}
		return new DecimalType(Float.valueOf((float) this.value.intValue() / (float) count));
	}
	
	@Override
	public int length() {
		return 6;
	}
}
