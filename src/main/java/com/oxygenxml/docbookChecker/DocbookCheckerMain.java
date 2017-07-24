package com.oxygenxml.docbookChecker;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.oxygenxml.docbookChecker.view.CheckerFrame;

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
		CheckerFrame frame = new CheckerFrame(null);
	}
}
