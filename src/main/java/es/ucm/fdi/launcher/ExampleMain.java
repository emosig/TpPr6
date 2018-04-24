package es.ucm.fdi.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import es.ucm.fdi.exceptions.NegativeArgExc;
import es.ucm.fdi.exceptions.SimulatorExc;
import es.ucm.fdi.ini.Ini;
import es.ucm.gui.MainFrame;

public class ExampleMain {

	private final static Integer _timeLimitDefaultValue = 10;
	private static Integer _timeLimit = null;
	private static String _inFile = null;
	private static String _outFile = null;

	private static boolean parseArgs(String[] args) {

		// define the valid command line options
		//returns true for GUI mode, false for batch mode
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		boolean mode = false;
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseStepsOption(line);
			mode = parseModeOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			// new Piece(...) might throw GameError exception
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}
		return mode;
	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc(
				"’batch’ for batch mode and ’gui’ for GUI mode (default value is ’batch’)").build());
		
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg()
				.desc("Ticks to execute the simulator's main loop (default value is " + _timeLimitDefaultValue + ").")
				.build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(ExampleMain.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}

	private static void parseStepsOption(CommandLine line) throws ParseException {
		String t = line.getOptionValue("t", _timeLimitDefaultValue.toString());
		try {
			_timeLimit = Integer.parseInt(t);
			assert (_timeLimit < 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time limit: " + t);
		}
	}
	
	private static boolean parseModeOption(CommandLine line) throws ParseException {
		//returns true for GUI, false for batch
		String t = line.getOptionValue("m");
		if("gui".equals(t)) return true;
		else if("batch".equals(t)) return false;
		else throw new ParseException("Invalid mode");
	}

	/**
	 * This method run the simulator on all files that ends with .ini if the given
	 * path, and compares that output to the expected output. It assumes that for
	 * example "example.ini" the expected output is stored in "example.ini.eout".
	 * The simulator's output will be stored in "example.ini.out"
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 * @throws InvocationTargetException 
	 */
	public static void test(String path) throws IOException, InvocationTargetException, InterruptedException {

		File dir = new File(path);

		if ( !dir.exists() ) {
			throw new FileNotFoundException(path);
		}
		File[] files = dir.listFiles(
				/*
				 * Me hubiese gustado que fuese de otra forma, pero 
				 * lo que tuvimos FilenameFilter y yo nunca funcionó
				 * 
				new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".ini");
			}
		}*/);

		for (File file : files) {
			//mi código
			if(file.getName().endsWith(".ini")) //filtro aquí los archivos
				test(file.getAbsolutePath(), file.getAbsolutePath() + ".out", file.getAbsolutePath() + ".eout", 10);
		}

	}

	private static void test(String inFile, String outFile, String expectedOutFile, int timeLimit) throws IOException, InvocationTargetException, InterruptedException {
		_outFile = outFile;
		_inFile = inFile;
		_timeLimit = timeLimit;
		
		startGUIMode();
		
		/*startBatchMode();
		boolean equalOutput = (new Ini(_outFile)).equals(new Ini(expectedOutFile));
		System.out.println("Result for: '" + _inFile + "' : "
				+ (equalOutput ? "OK!" : ("not equal to expected output +'" + expectedOutFile + "'")));
				*/
	}

	/**
	 * Run the simulator in batch mode
	 * 
	 * @throws IOException
	 */
	private static void startBatchMode() throws IOException {
		// TODO
		// Add your code here. Note that the input argument where parsed and stored into
		// corresponding fields.
		try {
			InputStream i = new FileInputStream(_inFile);
			OutputStream o = new FileOutputStream(_outFile);
			Controller c = new Controller(i, o, _timeLimit);
			c.run();
		} catch(FileNotFoundException f){
			System.out.println("El sistema no puede encontrar la ruta especificada");
		}	
	}
	
	/**
	 * Run the simulator in GUI mode
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 * @throws InvocationTargetException 
	 */
	private static void startGUIMode() throws IOException, InvocationTargetException, InterruptedException {
		InputStream i = null;
		try {
			if(_inFile != null) {
				i = new FileInputStream(_inFile);
			}
			Controller c = new Controller(i, _timeLimit); //nueva constructora?
			SwingUtilities.invokeAndWait(new Runnable() { 
				public void run() { 
					try {
						new MainFrame(c, null);
					} catch (SimulatorExc e) {
						//e.printStackTrace();
					} 
					} 
				});
		} catch(FileNotFoundException f){
			System.out.println("El sistema no puede encontrar la ruta especificada");
		} catch (NegativeArgExc e) {
			//e.printStackTrace();
		}	
	}

	private static void start(String[] args) throws IOException, InvocationTargetException, InterruptedException {
		if(parseArgs(args)) startGUIMode();
		else startBatchMode();
	}

	public static void main(String[] args) throws IOException, InvocationTargetException, InterruptedException {

		// my example command lines:
		//
		// -i src/main/resources/examples/events/basic/ex1.ini
		// -i src/main/resources/examples/events/basic/ex1.ini -o ex1.out
		// -i src/main/resources/examples/events/basic/ex1.ini -t 20
		// -i src/main/resources/examples/events/basic/ex1.ini -o ex1.out -t 20
		// --help
		//

		// Call test in order to test the simulator on all examples in a directory.
		test("src/main/resources/examples/basic");
		// Call start to start the simulator from command line, etc.
		//start(args);
	}

}
