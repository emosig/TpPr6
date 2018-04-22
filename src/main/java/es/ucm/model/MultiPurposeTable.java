package es.ucm.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;

import es.ucm.fdi.util.AbstractTableModel;

public class MultiPurposeTable extends AbstractTableModel<String>{
	
	public MultiPurposeTable() {
		
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColumnCount() {
		return fieldNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return fieldNames[columnIndex];
	}

	@Override
	public int getRowCount() {
		return elements.size();
	}

	@Override  // ineficiente: ¿puedes mejorarlo?
	public Object getValueAt(int rowIndex, int columnIndex) {
		/*return elements.get(rowIndex)
				.describe(new HashMap<String, String>())
				.get(fieldNames[columnIndex]);*/
		return null; //no entiendo lo de arriba. Qué coño tiene que implementar describable?
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		if(isCellEditable(rowIndex, columnIndex)) {
			
		}
	}

	@Override
	public void describe(Map<String, String> out) {
		// TODO Auto-generated method stub
		
	}

}
