package type;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class BasicType implements Type, Serializable {
	protected enum DataType {
		BOOLEAN, INT, DECIMAL, FLOAT, TIMESTAMP, CHAR, VARCHAR
	}

	protected DataType type;

	@Override
	public abstract Object getValue();
	
	@Override
	public abstract Type setValue(String s);

	@Override
	public abstract boolean lessThan(Type value);

	@Override
	public abstract boolean greaterThan(Type value);

	@Override
	public abstract boolean equals(Type value);

	@Override
	public abstract Type clone();

	@Override
	public abstract String toString();
	
	@Override
	public abstract int length();
	
	@Override
	public boolean equalsType(Type t) {
		if (t == null) return false;
		return t.isBOOLEAN() && this.isBOOLEAN() ||
				t.isCHAR() && this.isCHAR() ||
				t.isDECIMAL() && this.isDECIMAL() ||
				t.isFLOAT() && this.isFLOAT() ||
				t.isINT() && this.isINT() ||
				t.isTIMESTAMP() && this.isTIMESTAMP() ||
				t.isVARCHAR() && this.isVARCHAR();
	}
	
	@Override
	public boolean isBOOLEAN() {
		return type == DataType.BOOLEAN;
	}

	@Override
	public boolean isCHAR() {
		return type == DataType.CHAR;
	}

	@Override
	public boolean isDECIMAL() {
		return type == DataType.DECIMAL;
	}

	@Override
	public boolean isFLOAT() {
		return type == DataType.FLOAT;
	}

	@Override
	public boolean isINT() {
		return type == DataType.INT;
	}

	@Override
	public boolean isTIMESTAMP() {
		return type == DataType.TIMESTAMP;
	}

	@Override
	public boolean isVARCHAR() {
		return type == DataType.VARCHAR;
	}

	@Override
	public Type negate() {
		return null;
	}
	
	@Override
	public Type add(Type val) {
		return null;
	}

	@Override
	public Type multiply(Type val) {
		return null;
	}
	
	@Override
	public Type divide(Type val) {
		return null;
	}
	
	@Override
	public Type mod(int val) {
		return null;
	}

	@Override
	public Type divide(int count) {
		return null;
	}

	@Override
	public int compareTo(Type tb) {
		if (this.greaterThan(tb)) {
			return 1;
		} else
		if (this.lessThan(tb)) {
			return -1;
		} else {
			return 0;
		}
	}
}
