package predicate;

import scan.Scan;

public interface Predicate {
	public boolean isTrue(Scan s) throws Exception;
	
	public String toString();
}
