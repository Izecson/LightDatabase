package plan;

import java.util.LinkedList;

import database.Schema;
import scan.Scan;

public interface Plan {
	public Scan start();
	
	public Schema schema();
	
	public LinkedList<Plan> getChildren();
	
	public void setChildren(LinkedList<Plan> plans);
	
	public Plan getFather();
	
	public void setFather(Plan fa);
	
	public String toString();
}
