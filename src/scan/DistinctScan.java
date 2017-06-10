package scan;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;

import type.Type;

public class DistinctScan implements Scan {
	Scan sub;
	TreeSet<LinkedList<Type>> values;
	
	@SuppressWarnings("rawtypes")
	class Sorter implements Comparator {
		@SuppressWarnings("unchecked")
		public int compare(Object obj1, Object obj2) {
			LinkedList<Type> list1 = (LinkedList<Type>) obj1;
			LinkedList<Type> list2 = (LinkedList<Type>) obj2;
			for (int i = 0; i < sub.length(); ++i) {
				if (list1.get(i).greaterThan(list2.get(i))) return 1;
				else if (list1.get(i).lessThan(list2.get(i))) return -1;
			}
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public DistinctScan(Scan scan) {
		sub = scan;
		values = new TreeSet<LinkedList<Type>>(new Sorter());
	}

	@Override
	public void open() {
		sub.open();
		values.clear();
	}

	@Override
	public boolean next() {
		try {
			while(sub.next()) {
				if (!values.contains(sub.getRow())) {
					values.add(sub.getRow());
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void close() {
		values.clear();
		sub.close();
	}

	@Override
	public int length() {
		return sub.length();
	}

	@Override
	public LinkedList<Type> getRow() throws Exception {
		return sub.getRow();
	}

	@Override
	public Type getValue(int index) throws Exception {
		return sub.getValue(index);
	}

	@Override
	public Type getValue(String col) throws Exception {
		return sub.getValue(col);
	}

	@Override
	public Type getValue(String tbl, String col) throws Exception {
		return sub.getValue(tbl, col);
	}

}
