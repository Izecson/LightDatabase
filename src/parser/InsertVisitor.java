package parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import expr.Expr;
import plan.InsertPlan;
import plan.Plan;
import plan.RecordPlan;
import prototype.*;
import type.*;

/*
 * Insert
	-> INSERT INTO tbl-name VALUES (value, бн)
	-> INSERT INTO tbl-name(col-name,бн)VALUES(value,бн)
	-> INSERT INTO tbl-name(subquery)
 */
@SuppressWarnings("unchecked")
public class InsertVisitor extends Visitor {
	private Plan plan;

	public InsertVisitor(DatabaseManager dm) {
		manager = dm;
		plan = null;
	}
	
	@Override
	public Plan getPlan() {
		return plan;
	}

	@Override
	public void visit(CommonTree t) throws Exception {
		try {
			// statement -> insert_statement -> INSERT_VALUES tbl_name values_clause
			if (t.getType() == LightdbLexer.INSERT_VALUES) {
				CommonTree tbl = (CommonTree) t.getChild(0);
				String tableName = tbl.toString().toLowerCase();
				Table table = manager.getDatabase().getTable(tableName);
				
				CommonTree val_clause = (CommonTree) t.getChild(1);
				plan = new InsertPlan(table, new RecordPlan(
						parseValues(val_clause, table.getSchema())));
			} else
			// // statement -> insert_statement -> INSERT_COLUMNS tbl_name col_name* values_clause
			if (t.getType() == LightdbLexer.INSERT_COLUMNS) {
				CommonTree tbl = (CommonTree) t.getChild(0);
				String tableName = tbl.toString().toLowerCase();
				Table table = manager.getDatabase().getTable(tableName);
				
				List<CommonTree> children = (List<CommonTree>) t.getChildren();
				LinkedList<String> cols = new LinkedList<String>();
				for (int i = 1; i < children.size(); ++i) {
					CommonTree child = children.get(i);
					if (child.getType() == LightdbLexer.ID) {
						cols.add(child.toString().toLowerCase());
					} else
					if (child.getType() == LightdbLexer.VALUES) {
						plan = new InsertPlan(table, new RecordPlan(
								parseValuesWithColumns(child, table.getSchema(), cols)));
						break;
					}
				}
			} else
			// statement -> insert_statement -> INSERT_SUBQUERY tbl_name subquery
			if (t.getType() == LightdbLexer.INSERT_SUBQUERY) {
				CommonTree tbl = (CommonTree) t.getChild(0);
				String tableName = tbl.toString().toLowerCase();
				Table table = manager.getDatabase().getTable(tableName);
				
				CommonTree subq = (CommonTree) t.getChild(1);
				plan = new InsertPlan(table, parseSelect(subq));
			} else {
				throw new DatabaseException("Builtin error, please have a check.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	private Record parseValues(CommonTree t, Schema schema) throws Exception {
		try {
			if (t.getType() == LightdbLexer.VALUES) {
				Iterator<CommonTree> treeIter = (Iterator<CommonTree>) t.getChildren().iterator();
				Iterator<Column> colIter = schema.getColumnList().iterator();
				LinkedList<Type> values = new LinkedList<Type>();
				while(colIter.hasNext() && treeIter.hasNext()) {
					values.add(getValue(treeIter.next(), colIter.next()));
				}
				return new Record(values, schema);
			} else {
				throw new DatabaseException("Builtin error, please have a check.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Record parseValuesWithColumns(CommonTree t, Schema schema, List<String> namedCols) throws Exception {
		try {
			if (t.getType() == LightdbLexer.VALUES) {
				List<CommonTree> children = (List<CommonTree>) t.getChildren();
				LinkedList<Column> columnList = schema.getColumnList();
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
				return new Record(values, schema);
			} else {
				throw new DatabaseException("Builtin error, please have a check.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Type getValue(CommonTree t, Column col) throws Exception {
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
			Expr e = parseExpr(t);
			Type ret = e.getValue(null);
			if (ret == null) {
				throw new DatabaseException("Syntax error.");
			}
			return tp.clone().setValue(ret.toString());
		}
	}
}
