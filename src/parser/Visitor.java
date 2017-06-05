package parser;

import org.antlr.runtime.tree.CommonTree;

import expr.*;
import plan.Plan;
import prototype.Column;
import prototype.DatabaseException;
import type.BooleanType;
import type.CharType;
import type.FloatType;
import type.IntType;
import type.Type;
/*
 * - Select
	-> SELECT [DISTINCT] select-expr,бн
		[FROM tbl-ref[,tbl-ref]бн]
 */
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
			return new FieldExpr("", t.toString().toLowerCase());
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
}
