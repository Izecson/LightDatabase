package plan;

import prototype.Table;

public class PlanExecutor {
	Plan rootPlan = null;
	
	public void setPlan(Plan plan) {
		rootPlan = plan;
	}
	
	public void createInsertPlan(Table tbl, Plan p) {
		rootPlan = new InsertPlan(tbl, p);
	}
}
