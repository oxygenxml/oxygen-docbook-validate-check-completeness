package com.oxygenxml.docbook.checker.reporters;
/**
 * Used by checker for report statuses. 
 * @author Cosmin Duna
 */

public interface StatusReporter {
	
	/**
	 * Report a status
	 * @param message the status
	 */
	public void reportStatus(String message);

}
