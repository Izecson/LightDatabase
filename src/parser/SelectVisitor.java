package parser;

import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import expr.Expr;
import plan.DistinctPlan;
import plan.Plan;
import plan.ProductPlan;
import plan.ProjectPlan;
import plan.SelectPlan;
import plan.TablePlan;
import prototype.DatabaseException;
import prototype.DatabaseManager;
import prototype.Table;

/*
 * Select
	->SELECT [DISTINCT] select-expr,бн
		[FROM tbl-ref[,tbl-ref]бн]
 */
@SuppressWarnings("unchecked")
public class SelectVisitor extends Visitor {
	private DatabaseManager manager;
	private Plan plan;
	
	public SelectVisitor(DatabaseManager dm) {
		manager = dm;
		plan = null;
	}
	
	@Override
	public void visit(CommonTree t) {
		try {
			if (t.getType() == LightdbLexer.SELECT || t.getType() == LightdbLexer.SELECT_DISTINCT) {
				List<CommonTree> children = (List<CommonTree>) t.getChildren();
				LinkedList<Expr> exprList = new LinkedList<Expr>();
				for (CommonTree expr : children) {
					if (expr.getType() == LightdbLexer.FROM) break;
					exprList.add(getExpr(expr));
				}
				// TODO: more powerful select.
				ProductPlan fromPlan = null;
				for(CommonTree fromCls : children) {
					if (fromCls.getType() == LightdbLexer.FROM) {
						List<CommonTree> names = (List<CommonTree>) fromCls.getChildren();
						for (CommonTree tbl : names) {
							Table table = manager.getDatabase().getTable(tbl.toString().toLowerCase());
							fromPlan = new ProductPlan(new TablePlan(table), fromPlan);
						}
						break;
					}
				}
				plan = new ProjectPlan(exprList, new SelectPlan(fromPlan));
				if (t.getType() == LightdbLexer.SELECT_DISTINCT) {
					plan = new DistinctPlan(plan);
				}
			} else {
				throw new DatabaseException("Builtin error, please have a check.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public Plan getPlan() {
		return plan;
	}
}
