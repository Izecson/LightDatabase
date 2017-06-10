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
import predicate.*;
import prototype.DatabaseException;
import prototype.DatabaseManager;
import prototype.Table;

/*
 * Select
	->SELECT [DISTINCT] select-expr,бн
		[FROM tbl-ref[,tbl-ref]бн]
		[WHERE where-condition]
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
		plan = parseSelect(t);
	}
	
	public Plan getPlan() {
		return plan;
	}
	
	private Plan parseSelect(CommonTree t) {
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
				Predicate pred = null;
				for(CommonTree child : children) {
					if (child.getType() == LightdbLexer.FROM) {
						List<CommonTree> names = (List<CommonTree>) child.getChildren();
						for (CommonTree tbl : names) {
							Table table = manager.getDatabase().getTable(tbl.toString().toLowerCase());
							fromPlan = new ProductPlan(new TablePlan(table), fromPlan);
						}
					} else
					if (child.getType() == LightdbLexer.WHERE) {
						pred = parsePredicate((CommonTree) child.getChild(0));
					}
				}
				Plan plan = new ProjectPlan(exprList, new SelectPlan(fromPlan, pred));
				if (t.getType() == LightdbLexer.SELECT_DISTINCT) {
					plan = new DistinctPlan(plan);
				}
				return plan;
			} else {
				throw new DatabaseException("Builtin error, please have a check.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Predicate parsePredicate(CommonTree tree) throws DatabaseException {
		if (tree.getType() == LightdbLexer.OR) {
			CommonTree lftChild = (CommonTree) tree.getChild(0);
			CommonTree rgtChild = (CommonTree) tree.getChild(1);
			return new OrPred(parsePredicate(lftChild), parsePredicate(rgtChild));
		}
		if (tree.getType() == LightdbLexer.AND) {
			CommonTree lftChild = (CommonTree) tree.getChild(0);
			CommonTree rgtChild = (CommonTree) tree.getChild(1);
			return new AndPred(parsePredicate(lftChild), parsePredicate(rgtChild));
		}
		if (tree.getType() >= 114 && tree.getType() <= 119) {
			CommonTree lftChild = (CommonTree) tree.getChild(0);
			CommonTree rgtChild = (CommonTree) tree.getChild(1);
			return new ComparePred(getExpr(lftChild), getExpr(rgtChild), tree.getText());
		}
		if (tree.getType() == LightdbLexer.NOT_EXISTS) {
			CommonTree lftChild = (CommonTree) tree.getChild(0);
			return new ExistPred(parseSelect(lftChild), true);
		}
		if (tree.getType() == LightdbLexer.EXISTS) {
			CommonTree lftChild = (CommonTree) tree.getChild(0);
			return new ExistPred(parseSelect(lftChild), false);
		}
		if (tree.getType() == LightdbLexer.IN) {
			CommonTree lftChild = (CommonTree) tree.getChild(0);
			CommonTree rgtChild = (CommonTree) tree.getChild(1);
			return new InPred(getExpr(lftChild), parseSelect(rgtChild));
		}
		if (tree.getType() == LightdbLexer.ANY) {
			CommonTree lftChild = (CommonTree) tree.getChild(0);
			CommonTree rgtChild = (CommonTree) tree.getChild(1);
			CommonTree thirdChild = (CommonTree) tree.getChild(2);
			return new AnyPred(getExpr(lftChild), parseSelect(thirdChild), rgtChild.getText());
		}
		if (tree.getType() == LightdbLexer.ALL) {
			CommonTree lftChild = (CommonTree) tree.getChild(0);
			CommonTree rgtChild = (CommonTree) tree.getChild(1);
			CommonTree thirdChild = (CommonTree) tree.getChild(2);
			return new AllPred(getExpr(lftChild), parseSelect(thirdChild), rgtChild.getText());
		}
		return null;
	}
}
