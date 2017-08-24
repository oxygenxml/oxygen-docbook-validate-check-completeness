package com.oxygenxml.docbook.checker;

/**
 * Reporter for validations worker.
 * @author intern4
 *
 */
public interface ValidationWorkerReporter {
	/**
	 * Report progress of process.
	 * @param progressCounter The current progress counter.
	 * @param isFinalCycle <code>true</code> if is the final cycle of progress bar
	 */
	public void reportProgress(int progressCounter, boolean isFinalCycle);

	/**
	 * Report the URL or the link in process.
	 * @param element the link or the URL.
	 */
	public void reportInProcessElement(String element);

}
