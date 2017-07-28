package com.oxygenxml.docbookChecker;



import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


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
