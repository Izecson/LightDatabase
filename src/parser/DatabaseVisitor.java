package parser;

import org.antlr.runtime.tree.CommonTree;

import prototype.Database;
import prototype.DatabaseException;

/*
 * Create or Drop Database
	-> CREATE DATABASE db-name
	-> USE db-name
	-> DROP DATABASE db-name
 */
public class DatabaseVisitor extends Visitor {
	@Override
	public void visit(CommonTree t) {
		try {
			if (t.getType() == LightdbLexer.CREATE_DATABASE ||
				t.getType() == LightdbLexer.DROP_DATABASE ||
				t.getType() == LightdbLexer.USE_DATABASE) {
				CommonTree child = (CommonTree) t.getChild(0);
				String db_name = child.toString().toLowerCase();
				if (t.getType() == LightdbLexer.CREATE_DATABASE) {
					Database.createDatabase(db_name);
				} else
				if (t.getType() == LightdbLexer.DROP_DATABASE) {
					Database.dropDatabase(db_name);
				} else 
				if (t.getType() == LightdbLexer.USE_DATABASE) {
					Database.useDatabase(db_name);
				}
			} else {
				throw new DatabaseException("Builtin error, please have a check.");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
}
