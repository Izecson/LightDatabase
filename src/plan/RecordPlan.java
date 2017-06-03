package plan;

import java.util.LinkedList;

import database.Record;
import database.Schema;
import scan.RecordScan;
import scan.Scan;

public class RecordPlan implements Plan {
	private Record record;
	private Plan fatherPlan;

	public RecordPlan(Record r) {
		record = r;
	}

	@Override
	public Scan start() {
		return new RecordScan(record);
	}

	@Override
	public Schema schema() {
		return null;
	}

	@Override
	public LinkedList<Plan> getChildren() {
		LinkedList<Plan> ret = new LinkedList<Plan>();
		return ret;
	}

	@Override
	public void setChildren(LinkedList<Plan> plans) {}

	@Override
	public Plan getFather() {
		return fatherPlan;
	}

	@Override
	public void setFather(Plan fa) {
		fatherPlan = fa;
	}

	@Override
	public String toString() {
		return "Record ";
	}
}