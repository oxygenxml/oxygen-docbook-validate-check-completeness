package com.oxygenxml.docbook.checker;

/**
 * Interactor with the validations worker.
 * @author Cosmin Duna
 *
 */
public interface ValidationWorkerInteractor {

	/**
	 * Report note at progress monitor.
	 * @param note The note in String format.
	 */
	 void reportNote(String note);

	/**
	 * Get the state of isCancelled flag(flag set true if this task was cancelled before it completed normally).
	 *@return <code>true</code> if worker is cancelled, <code>false</code>otherwise.
	 */
	 boolean isCancelled();
}
