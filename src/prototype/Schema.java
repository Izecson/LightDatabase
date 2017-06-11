package prototype;

import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class Schema implements Serializable {
	LinkedList<String> tableNames;
	LinkedList<String> columnNames;
	LinkedList<Column> columns;

	public Schema() {
		tableNames = new LinkedList<String>();
		columnNames = new LinkedList<String>();
		columns = new LinkedList<Column>();
	}

	public LinkedList<String> getTableNames() {
		return tableNames;
	}
	
	public LinkedList<String> getColumnNames() {
		return columnNames;
	}
	
	public LinkedList<Column> getColumnList() {
		return columns;
	}
	
	public Schema setTableNames(LinkedList<String> tbls) {
		tableNames = tbls;
		return this;
	}
	
	public Schema setColumnNames(LinkedList<String> cols) {
		columnNames = cols;
		return this;
	}
	
	public Schema setColumnList(LinkedList<Column> cols) {
		columns = cols;
		return this;
	}
	
	public int length() {
		return columns.size();
	}
	
	public boolean containsColumn(String col) {
		return getColumnIndex(col) >= 0;
	}
	
	public boolean containsColumn(String tbl, String col) {
		return getColumnIndex(tbl, col) >= 0;
	}
	
	public String getTableName(int index) {
		return tableNames.get(index);
	}
	
	public String getColumnName(int index) {
		return columnNames.get(index);
	}
	
	public int getColumnIndex(String col) {
		for (int i = 0; i < columnNames.size(); ++i) {
			if (columnNames.get(i).equals(col)) {
				return i;
			}
		}
		return -1;
	}
	
	public int getColumnIndex(String tbl, String col) {
		for (int i = 0; i < columnNames.size(); ++i) {
			if (tableNames.get(i).equals(tbl) && columnNames.get(i).equals(col)) {
				return i;
			}
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	public Schema clone() {
		Schema ret = new Schema();
		return ret.setTableNames((LinkedList<String>) getTableNames().clone())
				.setColumnNames((LinkedList<String>) getColumnNames().clone())
				.setColumnList((LinkedList<Column>) getColumnList().clone());
	}
	
	public Schema product(Schema sch) {
		return clone().addAll(sch.getTableNames(), sch.getColumnNames(), sch.getColumnList());
	}
	
	public Schema add(String tbl, String name, Column col) {
		tableNames.add(tbl);
		columnNames.add(name);
		columns.add(col);
		return this;
	}
	
	private Schema addAll(LinkedList<String> tbls, LinkedList<String>names, LinkedList<Column> cols) {
		tableNames.addAll(tbls);
		columnNames.addAll(names);
		columns.addAll(cols);
		return this;
	}
}
