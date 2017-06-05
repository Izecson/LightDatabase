package parser;

import prototype.DatabaseException;
import prototype.DatabaseManager;

public class VisitorBuilder {
	static public Visitor build(DatabaseManager dm, int t) throws DatabaseException {
		if (t == LightdbLexer.CREATE_DATABASE ||
			t == LightdbLexer.DROP_DATABASE ||
			t == LightdbLexer.USE_DATABASE) {
			return new DatabaseVisitor(dm);
		} else
		if (t == LightdbLexer.CREATE_TABLE ||
			t == LightdbLexer.DROP_TABLE) {
			return new TableVisitor(dm);
		} else
		if (t == LightdbLexer.INSERT_VALUES ||
			t == LightdbLexer.INSERT_COLUMNS ||
			t == LightdbLexer.INSERT_SUBQUERY) {
				return new InsertVisitor(dm);
		} else
		if (t == LightdbLexer.SELECT) {
			return new SelectVisitor(dm);
		} else {
			throw new DatabaseException("Error: unsupported grammar.");
		}
	}
}
