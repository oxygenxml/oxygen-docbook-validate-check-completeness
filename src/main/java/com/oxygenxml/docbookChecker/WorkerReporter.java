package com.oxygenxml.docbookChecker;


/**
 * Reporter 
 * @author intern4
 *
 */
public interface WorkerReporter {
	/**
	 * Report progress of process.
	 * @param progress the progress
	 */
	public void reporteProgress(int progress);

	/**
	 * Report the URL or the link in process.
	 * @param element the link or the URL.
	 */
	public void reportInProcessElement(String element);
}
