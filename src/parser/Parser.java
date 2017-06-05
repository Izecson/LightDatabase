package parser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;

import parser.LightdbParser.statement_return;
import plan.Plan;
import prototype.DatabaseManager;
import scan.Scan;

public class Parser {
	DatabaseManager manager;
	
	public Parser(DatabaseManager dm) {
		manager = dm;
	}
	
	public Scan parse(String str) {
		try {
			ANTLRStringStream antlrStringStream = new ANTLRStringStream(str);
			LightdbLexer eLexer = new LightdbLexer(antlrStringStream);
			TokenRewriteStream tokens = new TokenRewriteStream(eLexer);
			LightdbParser grammar = new LightdbParser(tokens);
			grammar.setTreeAdaptor(new CommonTreeAdaptor());
			statement_return ret = grammar.statement();
			CommonTree tree = (CommonTree) ret.getTree();
			Visitor visitor = VisitorBuilder.build(manager, tree.getType());
			visitor.visit(tree);
			Plan plan = visitor.getPlan();
			if (plan != null) {
				return plan.start();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
