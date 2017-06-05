package type;

import java.math.BigDecimal;

@SuppressWarnings("serial")
public class FloatType extends BasicType {
	private BigDecimal value;

	public FloatType() {
		type = DataType.FLOAT;
		value = null;
	}

	public FloatType(BigDecimal val) {
		type = DataType.FLOAT;
		value = val;
	}
	
	public FloatType(Float val) {
		type = DataType.FLOAT;
		value = BigDecimal.valueOf(val);
	}
	
	public FloatType(String s) {
		type = DataType.FLOAT;
		if (s == null || "null".equals(s)) {
			value = null;
		} else {
			value = new BigDecimal(s);
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
		if (value == null) {
			return null;
		}
		return value.floatValue();
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
		return new FloatType(this.value);
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
		return new FloatType(this.value.negate());
	}

	@Override
	public Type add(Type val) {
		if (this.value == null || val.getValue() == null) {
			if (val.isINT() || val.isFLOAT()) {
				return new FloatType();
			} else
			if (val.isDECIMAL()) {
				return val.clone();
			}
		} else
		if (val.isINT()) {
			return new FloatType(this.value.add(BigDecimal.valueOf((Integer) val.getValue())));
		} else
		if (val.isFLOAT()) {
			return new FloatType(this.value.add(BigDecimal.valueOf((Float) val.getValue())));
		} else
		if (val.isDECIMAL()) {
			return new DecimalType(this.value.add((BigDecimal) val.getValue()));
		}
		return null;
	}
	
	@Override
	public Type multiply(Type val) {
		if (this.value == null || val.getValue() == null) {
			if (val.isINT() || val.isFLOAT()) {
				return new FloatType();
			} else
			if (val.isDECIMAL()) {
				return val.clone();
			}
		} else
		if (val.isINT()) {
			return new FloatType(this.value.multiply(BigDecimal.valueOf((Integer) val.getValue())));
		} else
		if (val.isFLOAT()) {
			return new FloatType(this.value.multiply(BigDecimal.valueOf((Float) val.getValue())));
		} else
		if (val.isDECIMAL()) {
			return new DecimalType(this.value.multiply((BigDecimal) val.getValue()));
		}
		return null;
	}
	
	@Override
	public Type divide(Type val) {
		if (this.value == null || val.getValue() == null) {
			if (val.isINT() || val.isFLOAT()) {
				return new FloatType();
			} else
			if (val.isDECIMAL()) {
				return val.clone();
			}
		} else
		if (val.isINT()) {
			return new FloatType(this.value.divide(BigDecimal.valueOf((Integer) val.getValue())));
		} else
		if (val.isFLOAT()) {
			return new FloatType(this.value.divide(BigDecimal.valueOf((Float) val.getValue())));
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
		return new FloatType(this.value.divide(new BigDecimal(count)));
	}
	
	@Override
	public int length() {
		return 6;
	}
}
