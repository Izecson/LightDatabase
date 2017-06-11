package parser;

import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import expr.*;
import plan.DistinctPlan;
import plan.Plan;
import plan.ProductPlan;
import plan.ProjectPlan;
import plan.RenamePlan;
import plan.SelectPlan;
import plan.TablePlan;
import predicate.AllPred;
import predicate.AndPred;
import predicate.AnyPred;
import predicate.ComparePred;
import predicate.ExistPred;
import predicate.InPred;
import predicate.OrPred;
import predicate.Predicate;
import prototype.Column;
import prototype.DatabaseException;
import prototype.DatabaseManager;
import prototype.Schema;
import prototype.Table;
import type.BooleanType;
import type.CharType;
import type.FloatType;
import type.IntType;
import type.Type;

public abstract class Visitor {
	DatabaseManager manager;
	
	public void print(CommonTree t, int depth) {
		if (t != null) {
			StringBuffer sb = new StringBuffer(depth);
			System.out.println(sb.toString() + t.getText().toString());
			for (int i = 0; i < depth; i++) {
				sb = sb.append("   ");
			}
			for (int i = 0; i < t.getChildCount(); i++) {
				System.out.println(sb.toString() + t.getChild(i).getText());
				print((CommonTree) t.getChild(i), depth + 1);
			}
		}
	}
	
	abstract public void visit(CommonTree t) throws Exception;
	
	abstract public Plan getPlan();
	
	protected Expr getExpr(CommonTree t) throws DatabaseException {
		if (t.getType() == 105 || t.getType() == 108 || t.getType() == 109 ||
			(t.getType() == 111 && t.getChildCount() == 2) || t.getType() == 113) {
			return new BinaryExpr(
					getExpr((CommonTree) t.getChild(0)),
					getExpr((CommonTree) t.getChild(1)),
					t.toString());
		} else
		if (t.getType() == 111 && t.getChildCount() == 1) {
			return new UnaryExpr(
					getExpr((CommonTree) t.getChild(0)),
					"-");
		} else
		if (t.getType() == 112) {
			return new FieldExpr(t.getChild(0).toString().toLowerCase(), t.getChild(1).toString().toLowerCase());
		}
		if (t.getType() == LightdbLexer.ID) {
			return new FieldExpr("*", t.toString().toLowerCase());
		} else
		if (t.getType() == LightdbLexer.INTEGER_LITERAL) {
			return new ConstExpr(new IntType(t.toString()));
		} else
		if (t.getType() == LightdbLexer.FLOAT_LITERAL) {
			return new ConstExpr(new FloatType(t.toString()));
		} else
		if (t.getType() == LightdbLexer.STRING_LITERAL) {
			return new ConstExpr(new CharType(t.toString().length() - 2, t.toString()));
		} else
		if (t.getType() == LightdbLexer.TRUE) {
			return new ConstExpr(new BooleanType(true));
		} else
		if (t.getType() == LightdbLexer.FALSE) {
			return new ConstExpr(new BooleanType(false));
		}
		return null;
	}
	
	protected Type getValue(CommonTree t, Column col) throws Exception {
		Type tp = col.getType();
		if (t == null || t.getType() == LightdbLexer.NULL) {
			if (col.autoIncrement() && tp.isINT()) {
				return new IntType(col.getNextInt());
			} else
			if (col.hasDefault()) {
				return tp.clone();
			} else {
				if (!col.isNullable()) {
					throw new DatabaseException("Insert Error: Invalid null value.");
				}
				return tp.clone().setValue("null");
			}
		} else
		if (t.getType() == LightdbLexer.DEFAULT) {
			return tp.clone();
		} else {
			Expr e = getExpr(t);
			Type ret = e.getValue(null);
			if (ret == null) {
				throw new DatabaseException("Syntax error.");
			}
			return tp.clone().setValue(ret.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Plan parseSelect(CommonTree t) {
		try {
			if (t.getType() == LightdbLexer.SELECT || t.getType() == LightdbLexer.SELECT_DISTINCT) {
				List<CommonTree> children = (List<CommonTree>) t.getChildren();
				// TODO: more powerful select.
				Plan fromPlan = null;
				Predicate pred = null;
				for(CommonTree child : children) {
					if (child.getType() == LightdbLexer.FROM) {
						List<CommonTree> refs = (List<CommonTree>) child.getChildren();
						for (CommonTree fromcls : refs) {
							if (fromcls.getType() == LightdbLexer.AS) {
								Plan subPlan = null;
								CommonTree sub = (CommonTree) fromcls.getChild(0);
								if (sub.getType() == LightdbLexer.SELECT || sub.getType() == LightdbLexer.SELECT_DISTINCT) {
									subPlan = parseSelect(sub);
								} else {
									Table table = manager.getDatabase().getTable(sub.toString().toLowerCase());
									subPlan = new TablePlan(table);
								}
								CommonTree alias = (CommonTree) fromcls.getChild(1);
								fromPlan = new ProductPlan(new RenamePlan(subPlan, alias.toString().toLowerCase()), fromPlan);
							} else
							if (fromcls.getType() == LightdbLexer.ID) {
								Table table = manager.getDatabase().getTable(fromcls.toString().toLowerCase());
								fromPlan = new ProductPlan(new TablePlan(table), fromPlan);
							}
						}
					} else
					if (child.getType() == LightdbLexer.WHERE) {
						pred = parsePredicate((CommonTree) child.getChild(0));
					}
				}
				
				LinkedList<Expr> exprList = new LinkedList<Expr>();
				LinkedList<String> asNames = new LinkedList<String>();
				for (CommonTree expr : children) {
					if (expr.getType() == LightdbLexer.FROM) break;
					if (expr.getType() == 108 && expr.getChildCount() == 0) {
						Schema schema = fromPlan.getSchema();
						for (int i = 0; i < schema.length(); ++i) {
							exprList.add(new FieldExpr(schema.getTableName(i), schema.getColumnName(i)));
							asNames.add(null);
						}
						continue;
					} else
					if (expr.getType() == LightdbLexer.AS) {
						Expr e = getExpr((CommonTree) expr.getChild(0));
						exprList.add(e);
						asNames.add(expr.getChild(1).toString().toLowerCase());
					} else {
						Expr e = getExpr((CommonTree) expr);
						exprList.add(e);
						asNames.add(null);
					}
				}

				Plan plan = new ProjectPlan(exprList, asNames, new SelectPlan(fromPlan, pred));
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
	
	protected Predicate parsePredicate(CommonTree tree) throws DatabaseException {
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
