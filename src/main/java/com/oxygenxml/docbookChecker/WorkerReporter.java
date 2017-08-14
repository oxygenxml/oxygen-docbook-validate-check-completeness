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
	 * @param isFinalCycle <code>true</code> if is the final cycle of progress bar
	 */
	public void reportProgress(int progress, boolean isFinalCycle);

	/**
	 * Report the URL or the link in process.
	 * @param element the link or the URL.
	 */
	public void reportInProcessElement(String element);

}
