package parser;

import org.antlr.runtime.tree.CommonTree;

import plan.Plan;
import prototype.DatabaseManager;

/*
 * Select
	->SELECT [DISTINCT] select-expr,бн
		[FROM tbl-ref[,tbl-ref]бн]
		[WHERE where-condition]
 */
public class SelectVisitor extends Visitor {
	private Plan plan;
	
	public SelectVisitor(DatabaseManager dm) {
		manager = dm;
		plan = null;
	}
	
	@Override
	public void visit(CommonTree t) {
		plan = parseSelect(t);
	}
	
	@Override
	public Plan getPlan() {
		return plan;
	}
}
