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
	 */
	public static void main(String[] args){
		
		//setLookAndFeel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
		}
		
		// create frame
		CheckerFrame frame = new CheckerFrame();
	}
}
