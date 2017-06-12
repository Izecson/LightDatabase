package parser;

import org.antlr.runtime.tree.CommonTree;

import plan.DeletePlan;
import plan.Plan;
import prototype.DatabaseException;
import prototype.DatabaseManager;
import prototype.Table;

/*
 * Delete
	-> DELETE FROM tbl-name [WHERE where-condition]
 */
public class DeleteVisitor extends Visitor {
	private Plan plan;

	public DeleteVisitor(DatabaseManager dm) {
		manager = dm;
		plan = null;
	}

	@Override
	public void visit(CommonTree t) throws Exception {
		try {
			if (t.getType() == LightdbLexer.DELETE) {
				CommonTree tbl = (CommonTree) t.getChild(0);
				String tableName = tbl.toString().toLowerCase();
				Table table = manager.getDatabase().getTable(tableName);
				
				CommonTree whcls = (CommonTree) t.getChild(1);
				plan = new DeletePlan(table, parsePredicate((CommonTree) whcls.getChild(0)));
			} else {
				throw new DatabaseException("Builtin error, please have a check.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Plan getPlan() {
		return plan;
	}
	
}
