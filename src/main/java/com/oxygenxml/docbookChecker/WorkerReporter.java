package com.oxygenxml.docbookChecker;


/**
 * Reporter 
 * @author intern4
 *
 */
public interface WorkerReporter {
	/**
	 * Report progress of process.
	 * @param progressCounter the current progress counter.
	 * @param isFinalCycle <code>true</code> if is the final cycle of progress bar
	 */
	public void reportProgress(int progressCounter, boolean isFinalCycle);

	/**
	 * Report the URL or the link in process.
	 * @param element the link or the URL.
	 */
	public void reportInProcessElement(String element);

}
