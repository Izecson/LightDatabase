package parser;

import org.antlr.runtime.tree.CommonTree;

public abstract class Visitor {
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
	
	public abstract void visit(CommonTree t);
}
