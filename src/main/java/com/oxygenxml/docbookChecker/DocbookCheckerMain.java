package com.oxygenxml.docbookChecker;



import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.oxygenxml.docbookChecker.reporters.ProblemReporterImpl;
import com.oxygenxml.docbookChecker.view.CheckerFrame;
import com.oxygenxml.docbookChecker.view.OxygenFileChooserCreator;
import com.oxygenxml.docbookChecker.view.SwingFileChooserCreator;
import com.oxygenxml.ldocbookChecker.parser.PlainParserCreator;

/**
 * Main class.
 * @author intern4
 *
 */
public class DocbookCheckerMain {

	/**
	 * main
	 * @param args
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{

		//setLookAndFeel
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// create frame
		CheckerFrame frame = new CheckerFrame(null, null, new ProblemReporterImpl(), new OxygenFileChooserCreator(), new PlainParserCreator());
	}
}
