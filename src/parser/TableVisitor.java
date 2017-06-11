package parser;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import plan.Plan;
import prototype.Column;
import prototype.DatabaseException;
import prototype.DatabaseManager;
import prototype.Schema;
import prototype.Table;
import type.BooleanType;
import type.CharType;
import type.DecimalType;
import type.FloatType;
import type.IntType;
import type.TimestampType;
import type.Type;
import type.VarcharType;

/*
 * Create or Drop table
 *	->CREATE TABLE tbl-name (create-definition, бн)
 *	->DROP TABLE tbl-name [,tbl-name]
 */
public class TableVisitor extends Visitor {
	private String tableName;
	private Schema schema;
	
	public TableVisitor(DatabaseManager dm) {
		manager = dm;
		tableName = null;
		schema = new Schema();
	}
	
	@Override
	public Plan getPlan() {
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void visit(CommonTree t) {
		try {
			if (t.getType() == LightdbLexer.CREATE_TABLE) {
				List<CommonTree> children = (List<CommonTree>) t.getChildren();
				String primaryKey = null;
				for (CommonTree child : children) {
					// tbl_name -> name -> ID
					if (child.getType() == LightdbLexer.ID) {
						tableName = child.toString().toLowerCase();
					} else
					// create_definition -> CREATE_DEFINITION col_name data_type suffix*
					if (child.getType() == LightdbLexer.CREATE_DEFINITION) {
						if (tableName != null) {
							parseColumns(child);
						} else {
							throw new DatabaseException("Table name is missing.");
						}
					} else
					// create_definition -> PRIMARY_KEY col_name
					if (child.getType() == LightdbLexer.PRIMARY_KEY) {
						CommonTree col = (CommonTree) child.getChild(0);
						if (col.getType() == LightdbLexer.ID) {
							primaryKey = col.toString().toLowerCase();
						}
					}
				}
				Table table = manager.getDatabase().createTable(tableName, schema);
				table.setPrimaryKey(primaryKey);
			} else
			if (t.getType() == LightdbLexer.DROP_TABLE) {
				List<CommonTree> children = (List<CommonTree>) t.getChildren();
				for (CommonTree child : children) {
					if (child.getType() == LightdbLexer.ID) {
						manager.getDatabase().dropTable(child.toString().toLowerCase());
					}
				}
			} else {
				throw new DatabaseException("Builtin error.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	private void parseColumns(CommonTree t) {
		try {
			if (t.getType() == LightdbLexer.CREATE_DEFINITION) {
				List<CommonTree> children = (List<CommonTree>) t.getChildren();
				String colName = null;
				Column col = null;
				for (CommonTree child : children) {
					if (child.getType() == LightdbLexer.ID) {
						colName = child.toString().toLowerCase();
						col = new Column(colName);
					} else
					if (isDataType(child.getType())) {
						col.setType(getType(child));
					} else
					if (child.getType() == LightdbLexer.NULL) {
						if (col != null) {
							col.setNullable(child.getChildCount() == 0);
						}
					} else 
					if (child.getType() == LightdbLexer.AUTO_INCREMENT) {
						if (col != null) {
							col.setAutoIncrement(true);
						}
					} else
					if (child.getType() == LightdbLexer.DEFAULT) {
						if (col != null) {
							CommonTree deflt = (CommonTree) child.getChild(0);
							if (deflt.getType() == LightdbLexer.NULL) {
								col.setDefault("null");
							} else {
								col.setDefault(deflt.getText());
							}
						}
					}
				}
				schema.add(tableName, colName, col);
			} else {
				throw new DatabaseException("Builtin error, please have a check.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	// data_type -> real_type
	@SuppressWarnings("unchecked")
	private Type getType(CommonTree t) {
		// data_type -> INT
		if (t.getType() == LightdbLexer.INT) {
			return new IntType();
		}
		// data_type -> FLOAT
		if (t.getType() == LightdbLexer.FLOAT) {
			return new FloatType();
		}
		// data_type -> CHAR INTEGER_LITERAL
		if (t.getType() == LightdbLexer.CHAR) {
			CommonTree child2 = (CommonTree) t.getChild(0);
			String cap = child2.getText();
			return new CharType(Integer.parseInt(cap));
		}
		// data_type -> DATETIME
		if (t.getType() == LightdbLexer.DATETIME) {
			return new TimestampType();
		}
		// data_type -> BOOLEAN
		if (t.getType() == LightdbLexer.BOOLEAN) {
			return new BooleanType();
		}
		// data_type -> DECIMAL INTEGER_LITERAL INTEGER_LITERAL
		if (t.getType() == LightdbLexer.DECIMAL) {
			List<CommonTree> childts2 = (List<CommonTree>) t.getChildren();
			if (childts2.size() == 2) {
				CommonTree child2 = childts2.get(0);
				String cap = child2.getText();
				child2 = childts2.get(1);
				String dec = child2.getText();
				return new DecimalType(Integer.parseInt(cap), Integer.parseInt(dec));
			} else {
				CommonTree child2 = childts2.get(0);
				String cap = child2.getText();
				return new DecimalType(Integer.parseInt(cap), 0);
			}
		}
		// data_type -> TIMESTAMP
		if (t.getType() == LightdbLexer.TIMESTAMP) {
			return new TimestampType();
		}
		// data_type -> VARCHAR INTEGER_LITERAL
		if (t.getType() == LightdbLexer.VARCHAR) {
			CommonTree child2 = (CommonTree) t.getChild(0);
			String cap = child2.getText();
			return new VarcharType(Integer.parseInt(cap));
		}
		return null;
	}
	
	private boolean isDataType(int type) {
		if (type == LightdbLexer.BOOLEAN ||
			type == LightdbLexer.CHAR ||
			type == LightdbLexer.DATETIME ||
			type == LightdbLexer.DECIMAL ||
			type == LightdbLexer.FLOAT ||
			type == LightdbLexer.INT ||
			type == LightdbLexer.TIMESTAMP ||
			type == LightdbLexer.VARCHAR) {
			return true;
		} else {
			return false;
		}
	}
}
