package type;

public interface Type {	
	public boolean isBOOLEAN();

	public boolean isCHAR();

	public boolean isDECIMAL();

	public boolean isFLOAT();

	public boolean isINT();

	public boolean isTIMESTAMP();

	public boolean isVARCHAR();

	public Object getValue();

	public Type setValue(String s);

	public boolean lessThan(Type value);

	public boolean greaterThan(Type value);

	public boolean equals(Type value);

	public Type clone();

	public String toString();

	public Type add(Type val);
	
	public Type negate();

	public Type multiply(Type val);
	
	public Type divide(Type val);
	
	public Type mod(int val);
	
	public Type divide(int count);

	public int compareTo(Type tb);

	public int length();
}
