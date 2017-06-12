package parser;

import java.util.LinkedList;

import org.antlr.runtime.tree.CommonTree;

import expr.Expr;
import plan.Plan;
import plan.UpdatePlan;
import predicate.Predicate;
import prototype.DatabaseException;
import prototype.DatabaseManager;
import prototype.Table;

/*
 * Update
	-> UPDATE tbl-name SET col-name1=value [,col-name2=value¡­][WHERE where-condition]
 */
public class UpdateVisitor extends Visitor {
	private Plan plan;

	public UpdateVisitor(DatabaseManager dm) {
		manager = dm;
		plan = null;
	}

	@Override
	public void visit(CommonTree t) throws Exception {
		try {
			if (t.getType() == LightdbLexer.UPDATE) {
				CommonTree tbl = (CommonTree) t.getChild(0);
				String tableName = tbl.toString().toLowerCase();
				Table table = manager.getDatabase().getTable(tableName);
				
				Predicate pred = null;
				LinkedList<String> cols = new LinkedList<String>();
				LinkedList<Expr> exprs = new LinkedList<Expr>();
				for (int i = 1; i < t.getChildCount(); ++i) {
					CommonTree child = (CommonTree) t.getChild(i);
					if (child.getType() == LightdbLexer.UPDATE_PAIR) {
						String name = child.getChild(0).toString().toLowerCase();
						Expr expr = parseExpr((CommonTree) child.getChild(1));
						cols.add(name);
						exprs.add(expr);
					} else
					if (child.getType() == LightdbLexer.WHERE) {
						pred = parsePredicate((CommonTree) child.getChild(0));
					}
				}
				plan = new UpdatePlan(table, pred, cols, exprs);
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
