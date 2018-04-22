package es.ucm.fdi.util;

import java.util.List;
import java.util.Map;

import javax.swing.table.TableModel;

public abstract class AbstractTableModel<E> implements TableModel, Describable{
	
	protected String[] fieldNames;
	protected List<E> elements;
	
	/*@Override
	public void describe(Map<String, String> out) {
		// TODO Auto-generated method stub
		
	}*/
}
