package type;

import java.math.BigDecimal;

@SuppressWarnings("serial")
public class DecimalType extends BasicType {
	private int scale;
	private int decimal;
	private BigDecimal value;
	
	public DecimalType(int scl, int dec) {
		type = DataType.DECIMAL;
		scale = scl;
		decimal = dec;
	}
	
	public DecimalType(BigDecimal val) {
		type = DataType.DECIMAL;
		value = val;
		scale = value.scale();
		decimal = value.precision();
	}
	
	public DecimalType(Float val) {
		type = DataType.DECIMAL;
		value = BigDecimal.valueOf(val);
		scale = value.scale();
		decimal = value.precision();
	}
	
	public DecimalType(String s) {
		type = DataType.DECIMAL;
		if (s == null || "null".equals(s)) {
			value = null;
		} else {
			value = new BigDecimal(s);
			scale = value.scale();
			decimal = value.precision();
		}
	}
	
	@Override
	public Type setValue(String s) {
		if (s == null || "null".equals(s)) {
			value = null;
		} else {
			value = new BigDecimal(s);
		}
		return this;
	}

	@Override
	public Object getValue() {
		return value;
	}
	
	public int getIntegerScale() {
		return scale - decimal;
	}

	public int getDecimalScale() {
		return decimal;
	}

	@Override
	public boolean lessThan(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.compareTo((BigDecimal) value.getValue()) < 0;
		}
	}

	@Override
	public boolean greaterThan(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.compareTo((BigDecimal) value.getValue()) > 0;
		}
	}

	@Override
	public boolean equals(Type value) {
		if (this.value == null || value.getValue() == null) {
			return false;
		} else {
			return this.value.compareTo((BigDecimal) value.getValue()) == 0;
		}
	}

	@Override
	public Type clone() {
		return new DecimalType(this.value);
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
		return new DecimalType(this.value.add((BigDecimal) val.getValue()));
	}

	@Override
	public Type divide(int count) {
		if (this.value == null) {
			return null;
		}
		return new DecimalType(this.value.divide(new BigDecimal(count)));
	}
	
	@Override
	public int length() {
		return scale + 6;
	}
}
