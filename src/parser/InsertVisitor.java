package parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import database.*;
import expr.*;
import plan.PlanExecutor;
import plan.RecordPlan;
import type.*;

/*
 * Insert
	-> INSERT INTO tbl-name VALUES (value, бн)
	-> INSERT INTO tbl-name(col-name,бн)VALUES(value,бн)
	-> INSERT INTO tbl-name(subquery)
 */
@SuppressWarnings("unchecked")
public class InsertVisitor extends Visitor {
	PlanExecutor executor = new PlanExecutor();

	@Override
	public void visit(CommonTree t) {
		try {
			// statement -> insert_statement -> INSERT_VALUES tbl_name values_clause
			if (t.getType() == LightdbLexer.INSERT_VALUES) {
				CommonTree tbl = (CommonTree) t.getChild(0);
				String tableName = tbl.toString().toLowerCase();
				Table table = Database.getDatabase().getTable(tableName);
				
				CommonTree val_clause = (CommonTree) t.getChild(1);
				executor.createInsertPlan(table, new RecordPlan(
						parseValues(val_clause, table.getSchema().getColumnList(tableName))));
			} else
			// // statement -> insert_statement -> INSERT_COLUMNS tbl_name col_name* values_clause
			if (t.getType() == LightdbLexer.INSERT_COLUMNS) {
				CommonTree tbl = (CommonTree) t.getChild(0);
				String tableName = tbl.toString().toLowerCase();
				Table table = Database.getDatabase().getTable(tableName);
				
				List<CommonTree> children = (List<CommonTree>) t.getChildren();
				LinkedList<String> cols = new LinkedList<String>();
				for (int i = 1; i < children.size(); ++i) {
					CommonTree child = children.get(i);
					if (child.getType() == LightdbLexer.ID) {
						cols.add(child.toString().toLowerCase());
					} else
					if (child.getType() == LightdbLexer.VALUES) {
						executor.createInsertPlan(table, new RecordPlan(
								parseValuesWithColumns(child, table.getSchema().getColumnList(tableName), cols)));
						break;
					}
				}
			} else
			// statement -> insert_statement -> INSERT_SUBQUERY tbl_name subquery
			if (t.getType() == LightdbLexer.INSERT_SUBQUERY) {
				CommonTree tbl = (CommonTree) t.getChild(0);
				String tableName = tbl.toString().toLowerCase();
				Table table = Database.getDatabase().getTable(tableName);
				
				CommonTree subq = (CommonTree) t.getChild(1);
				SelectVisitor.visit(subq);
				executor.createInsertPlan(table, SelectVisitor.getPlan());
			} else {
				throw new DatabaseException("Builtin error, please have a check.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	private Record parseValues(CommonTree t, List<Column> columnList) {
		try {
			if (t.getType() == LightdbLexer.VALUES) {
				Iterator<CommonTree> treeIter = (Iterator<CommonTree>) t.getChildren().iterator();
				Iterator<Column> colIter = columnList.iterator();
				LinkedList<Type> values = new LinkedList<Type>();
				while(colIter.hasNext() && treeIter.hasNext()) {
					values.add(getValue(treeIter.next(), colIter.next()));
				}
				return null;
			} else {
				throw new DatabaseException("Builtin error, please have a check.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Record parseValuesWithColumns(CommonTree t, List<Column> columnList, List<String> namedCols) {
		try {
			if (t.getType() == LightdbLexer.VALUES) {
				List<CommonTree> children = (List<CommonTree>) t.getChildren();
				LinkedList<Type> values = new LinkedList<Type>();
				for (Column col : columnList) {
					String colName = col.getName();
					CommonTree tree = null;
					for (int i = 0; i < namedCols.size(); ++i) {
						if (colName.equals(namedCols.get(i))) {
							tree = children.get(i);
							break;
						}
					}
					values.add(getValue(tree, col));
				}
				return null;
			} else {
				throw new DatabaseException("Builtin error, please have a check.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Expr getExpr(CommonTree t) throws DatabaseException {
		if (t.getType() == 105 || t.getType() == 108 || t.getType() == 109 ||
			(t.getType() == 111 && t.getChildCount() == 2) || t.getType() == 113) {
			return new BinaryExpr(
					getExpr((CommonTree) t.getChild(0)),
					getExpr((CommonTree) t.getChild(1)),
					t.toString());
		}
		if (t.getType() == 111 && t.getChildCount() == 1) {
			return new UnaryExpr(
					getExpr((CommonTree) t.getChild(0)),
					"-");
		}
		if (t.getType() == LightdbLexer.INTEGER_LITERAL) {
			return new ConstExpr(new IntType(t.toString()));
		}
		if (t.getType() == LightdbLexer.FLOAT_LITERAL) {
			return new ConstExpr(new FloatType(t.toString()));
		}
		if (t.getType() == LightdbLexer.STRING_LITERAL) {
			return new ConstExpr(new CharType(t.toString().length() - 2, t.toString()));
		}
		if (t.getType() == LightdbLexer.TRUE) {
			return new ConstExpr(new BooleanType(true));
		}
		if (t.getType() == LightdbLexer.FALSE) {
			return new ConstExpr(new BooleanType(false));
		}
		return null;
	}
	
	private Type getValue(CommonTree t, Column col) throws DatabaseException {
		if (t == null || t.getType() == LightdbLexer.NULL) {
			if (col.autoIncrement() && col.getType().isINT()) {
				return new IntType(col.getNextInt());
			} else
			if (col.hasDefault()) {
				return col.getType().clone();
			} else {
				return null;
			}
		} else
		if (t.getType() == LightdbLexer.DEFAULT) {
			return col.getType().clone();
		} else {
			Expr e = getExpr(t);
			return e.getValue(null);
		}
	}
}
