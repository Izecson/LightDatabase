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
		value = null;
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
			scale = 0;
			decimal = 0;
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
	public Type negate() {
		if (this.value == null) {
			return this.clone();
		}
		return new DecimalType(this.value.negate());
	}

	@Override
	public Type add(Type val) {
		if (this.value == null || val.getValue() == null) {
			if (val.isINT() || val.isFLOAT() || val.isDECIMAL()) {
				return this.clone();
			}
		} else
		if (val.isINT()) {
			return new DecimalType(this.value.add(BigDecimal.valueOf((Integer) val.getValue())));
		} else
		if (val.isFLOAT()) {
			return new DecimalType(this.value.add(BigDecimal.valueOf((Float) val.getValue())));
		} else
		if (val.isDECIMAL()) {
			return new DecimalType(this.value.add((BigDecimal) val.getValue()));
		}
		return null;
	}

	@Override
	public Type multiply(Type val) {
		if (this.value == null || val.getValue() == null) {
			if (val.isINT() || val.isFLOAT() || val.isDECIMAL()) {
				return this.clone();
			}
		} else
		if (val.isINT()) {
			return new DecimalType(this.value.multiply(BigDecimal.valueOf((Integer) val.getValue())));
		} else
		if (val.isFLOAT()) {
			return new DecimalType(this.value.multiply(BigDecimal.valueOf((Float) val.getValue())));
		} else
		if (val.isDECIMAL()) {
			return new DecimalType(this.value.multiply((BigDecimal) val.getValue()));
		}
		return null;
	}
	
	@Override
	public Type divide(Type val) {
		if (this.value == null || val.getValue() == null) {
			if (val.isINT() || val.isFLOAT() || val.isDECIMAL()) {
				return this.clone();
			}
		} else
		if (val.isINT()) {
			return new DecimalType(this.value.divide(BigDecimal.valueOf((Integer) val.getValue())));
		} else
		if (val.isFLOAT()) {
			return new DecimalType(this.value.divide(BigDecimal.valueOf((Float) val.getValue())));
		} else
		if (val.isDECIMAL()) {
			return new DecimalType(this.value.divide((BigDecimal) val.getValue()));
		}
		return null;
	}
	
	@Override
	public Type divide(int count) {
		if (this.value == null) {
			return this.clone();
		}
		return new DecimalType(this.value.divide(new BigDecimal(count)));
	}
	
	@Override
	public int length() {
		return scale + 6;
	}
}
