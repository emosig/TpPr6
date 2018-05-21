package es.ucm.fdi.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import es.ucm.fdi.exceptions.SimulatorExc;
import es.ucm.gui.MainFrame;

/*
 * Componente de swing editor de texto
 */
public class MyTextEditor extends JPanel{
	
	private JTextArea textArea;
	private JFileChooser fc;
	private PrintWriter out;
	private String fileName;
	
	public MyTextEditor() {
		super(new BorderLayout());
		fileName = "";
		initTextEd();
	}
	
	private void initTextEd() {
		textArea = new JTextArea("");
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane area = new JScrollPane(textArea);
		area.setPreferredSize(new Dimension(500, 500));
		add(area);
		fc = new JFileChooser();
	}
	
	public String getName() {
		return fileName;
	}
	
	public void append(String s) {
		textArea.append(s);
	}
	
	/*
	 * Devuelve la ruta del fichero leído o null
	 */
	public String load() throws SimulatorExc {
		int r = fc.showOpenDialog(null);
		if (r == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if (!file.getName().endsWith(".ini")) {
				throw new SimulatorExc("Formato no soportado");
			}
			String s = readFile(file);
			textArea.setText(s);
			return file.getAbsolutePath();
		}
		else return null;
	}
	
	public void clear() {
		textArea.setText("");
	}
	
	public void save() throws FileNotFoundException {
		int r = fc.showSaveDialog(null);
		if(r == JFileChooser.APPROVE_OPTION) {
			writeOut();
		}	
	}
	
	public static String readFile(File file) {
		String s = "";
		try {
			s = new Scanner(file).useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			MainFrame.showFriendlyExc("Archivo no encontrado");
		}
		return s;
	}
	
	private void writeOut() throws FileNotFoundException {
		out = new PrintWriter(fc.getSelectedFile());
		out.print(textArea.getText());//devuelve vacío!
		out.close();
	}
}
