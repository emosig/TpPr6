package es.ucm.fdi.util;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/*
 * Componente de swing para mostrar tablas con filas de DescribableEntity
 */
public class MyTable extends JPanel {
	/*
	 * Clase interna que hace las veces de TableModel
	 */
	private class MyTableModel extends AbstractTableModel{
		
		private String[] fieldNames;
		private List<? extends DescribableEntity> elements;
		//pongo data como atributo para ahorrarme news
		//cada vez que accedo a los datos
		private Map<String, String> data; 
		
		public MyTableModel(List<? extends DescribableEntity> elements) {
			super();
			data = new HashMap<>();
			this.elements = elements;
			loadTable();
		}
		/*
		 * Método de primera carga de datos en la tabla
		 */
		private void loadTable() {
			if(!elements.isEmpty()) {
				elements.get(0).describe(data); 
				fieldNames = data.keySet().toArray(
						new String[data.keySet().size()]);
			}
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
			elements.get(rowIndex).describe(data);
			return data.get(fieldNames[columnIndex]);
		}
	}
	
	private MyTableModel model;
	private static JTable tab;
	
	/*
	 * Empty table
	 */
	public MyTable() {
		super(new BorderLayout());
	}
	
	public MyTable(List<? extends DescribableEntity> data) {
		super(new BorderLayout());
		model = new MyTableModel(data);
		initTable(data);
		
	}
	
	public void initTable(List<? extends DescribableEntity> data) {
		tab = new JTable(model);
		add(new JScrollPane(tab), BorderLayout.CENTER);
	}
}
