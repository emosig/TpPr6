package es.ucm.fdi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel{
	
	private String[] fieldNames;
	private List<? extends DescribableEntity> elements;
	
	public MyTableModel(List<? extends DescribableEntity> elements) {
		super();
		this.elements = elements;
		loadTable();
	}
	
	private void loadTable() {
		Map<String, String> data = new HashMap<>();
		elements.get(0).describe(data); //solo para sacar los nombres de las columnas
		fieldNames = data.keySet().toArray(new String[data.keySet().size()]);
	}

	@Override
	public String getColumnName(int columnIndex) {
		return fieldNames[columnIndex];
	}
	
	@Override
	public int getColumnCount() {
		return fieldNames.length;
	}

	@Override
	public int getRowCount() {
		return elements.size();
	}

	@Override
	public String getValueAt(int rowIndex, int columnIndex) {
		Map<String, String> data = new HashMap<>();
		elements.get(rowIndex).describe(data);
		return data.get(fieldNames[columnIndex]);
	}

}
