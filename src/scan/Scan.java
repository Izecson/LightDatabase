package scan;

import type.Type;

public interface Scan {
	public void open();
	
	public boolean next();

	public void close();
	
	public int length();
	
	public Type getValue(int index);
	
	public Type getValue(String col);
	
	public Type getValue(String tbl, String col);
}
