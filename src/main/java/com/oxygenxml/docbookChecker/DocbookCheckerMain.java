package com.oxygenxml.docbookChecker;



import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.oxygenxml.docbookChecker.reporters.ProblemReporterImpl;
import com.oxygenxml.docbookChecker.reporters.StatusReporterImpl;
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
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, InterruptedException{

		//setLookAndFeel
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// create frame
		//CheckerFrame frame = new CheckerFrame(null, null, new ProblemReporterImpl(), new StatusReporterImpl(), new SwingFileChooserCreator(), new PlainParserCreator());

//		SwingUtilities.invokeLater(new Runnable() {
//	    public void run() {
//	    	List<String> list = new ArrayList<String>();
//	    	list.add("file:/D:/docbook-validate-check-completeness/test-samples/broken-external-link/testdb4.xml");
//	    	list.add("file:/D:/docbook-validate-check-completeness/test-samples/broken-external-link/testdb5.xml");
//	    	System.out.println("am creat lista");
//	    	Worker worker = new Worker(list , new PlainSettingImpl(), new PlainParserCreator(), new ProblemReporterImpl(), new StatusReporterImpl());
//	    	System.out.println("am dat execute");
//	    	
//	    	worker.execute();
//	    }
//	    
//	});
//		Thread.sleep(500000);
	}
}
