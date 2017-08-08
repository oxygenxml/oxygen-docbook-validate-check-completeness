package com.oxygenxml.docbookChecker.view;

/**
 * Reporter for ProgressMonitor
 * @author intern4
 *
 */
public interface ProgressMonitorReporter {
	
	/**
	 * Report a note at progress monitor. 
	 * @param note the note.
	 */
	public void reportNote(String note);
	
	/**
	 * Close the progress monitor.
	 */
	public void close();
}
