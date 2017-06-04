package plan;

import java.util.LinkedList;

import prototype.Schema;
import scan.Scan;

public interface Plan {
	public Scan start();
	
	public Schema getSchema();
	
	public LinkedList<Plan> getChildren();
	
	public Plan setChildren(LinkedList<Plan> plans);
	
	public Plan getFather();
	
	public Plan setFather(Plan fa);
	
	public String toString();
}
