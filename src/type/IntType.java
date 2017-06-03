package type;

import java.math.BigDecimal;

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
	public Type negate() {
		if (this.value == null) {
			return null;
		}
		return new IntType(Integer.valueOf(-this.value.intValue()));
	}
	
	@Override
	public Type add(Type val) {
		if (this.value == null || val.getValue() == null) {
			return null;
		} else
		if (val.isINT()) {
			return new IntType(Integer.valueOf(this.value.intValue() + (Integer) val.getValue()));
		} else
		if (val.isFLOAT()) {
			return new FloatType(BigDecimal.valueOf(this.value).add(BigDecimal.valueOf((Float) val.getValue())));
		} else
		if (val.isDECIMAL()) {
			return new DecimalType(BigDecimal.valueOf(this.value).add((BigDecimal) val.getValue()));
		}
		return null;
	}
	
	@Override
	public Type multiply(Type val) {
		if (this.value == null || val.getValue() == null) {
			return null;
		} else
		if (val.isINT()) {
			return new IntType(Integer.valueOf(this.value.intValue() * (Integer) val.getValue()));
		} else
		if (val.isFLOAT()) {
			return new FloatType(BigDecimal.valueOf(this.value).multiply(BigDecimal.valueOf((Float) val.getValue())));
		} else
		if (val.isDECIMAL()) {
			return new DecimalType(BigDecimal.valueOf(this.value).multiply((BigDecimal) val.getValue()));
		}
		return null;
	}
	
	@Override
	public Type divide(Type val) {
		if (this.value == null || val.getValue() == null) {
			return null;
		} else
		if (val.isINT()) {
			return new FloatType(BigDecimal.valueOf(this.value).divide(BigDecimal.valueOf((Integer) val.getValue())));
		} else
		if (val.isFLOAT()) {
			return new FloatType(BigDecimal.valueOf(this.value).divide(BigDecimal.valueOf((Float) val.getValue())));
		} else
		if (val.isDECIMAL()) {
			return new DecimalType(BigDecimal.valueOf(this.value).divide((BigDecimal) val.getValue()));
		}
		return null;
	}
	
	@Override
	public Type mod (int val) {
		if (this.value == null) {
			return null;
		}
		return new IntType(Integer.valueOf(this.value.intValue() % val));
	}

	@Override
	public Type divide(int count) {
		if (this.value == null) {
			return null;
		}
		return new FloatType(Float.valueOf((float) this.value.intValue() / (float) count));
	}
	
	@Override
	public int length() {
		return 6;
	}
}
