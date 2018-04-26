package es.ucm.fdi.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/*
 * Componente de swing para mostrar tablas con filas de DescribableEntity
 */
public class MyTable extends JPanel{
	/*
	 * Clase interna que hace las veces de TableModel
	 */
	private class MyTableModel extends AbstractTableModel{
		
		private String[] fieldNames;
		private List<? extends DescribableEntity> elements;
		
		public MyTableModel(List<? extends DescribableEntity> elements) {
			super();
			this.elements = elements;
			loadTable();
		}
		/*
		 * Método de primera carga de datos en la tabla
		 */
		private void loadTable() {
			Map<String, String> data = new HashMap<>();
			//solo para sacar los nombres de las columnas
			elements.get(0).describe(data); 
			fieldNames = data.keySet().toArray(
					new String[data.keySet().size()]);
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
	
	private static JTable tab;
	
	public MyTable(List<? extends DescribableEntity> data) {
		super();
		tab = new JTable(new MyTableModel(data));
		tab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane sp = new JScrollPane(tab);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(sp);
	}
	
	public void resizeColumnWidth() {
	    final TableColumnModel columnModel = tab.getColumnModel();
	    for (int column = 0; column < tab.getColumnCount(); column++) {
	        int width = 40; // Min width
	        for (int row = 0; row < tab.getRowCount(); row++) {
	            TableCellRenderer renderer = tab.getCellRenderer(row, column);
	            Component comp = tab.prepareRenderer(renderer, row, column);
	            width = Math.max(comp.getPreferredSize().width +1 , width);
	        }
	        if(width > 300)
	            width=300;
	        columnModel.getColumn(column).setPreferredWidth(width);
	    }
	}
}
