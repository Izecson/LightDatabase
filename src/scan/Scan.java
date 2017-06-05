package scan;

import type.Type;

public interface Scan {
	public void open();
	
	public boolean next();

	public void close();
	
	public int length();
	
	public Type getValue(int index) throws Exception;
	
	public Type getValue(String col) throws Exception;
	
	public Type getValue(String tbl, String col) throws Exception;
}
