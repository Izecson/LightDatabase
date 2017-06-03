package plan;

import java.util.LinkedList;

import prototype.Record;
import prototype.Schema;
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
	public Plan setChildren(LinkedList<Plan> plans) {
		return this;
	}

	@Override
	public Plan getFather() {
		return fatherPlan;
	}

	@Override
	public Plan setFather(Plan fa) {
		fatherPlan = fa;
		return this;
	}

	@Override
	public String toString() {
		return "Record ";
	}
}