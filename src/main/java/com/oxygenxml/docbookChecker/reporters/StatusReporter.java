package com.oxygenxml.docbookChecker.reporters;
/**
 * Used by checker for report statuses. 
 * @author intern4
 *
 */
public interface StatusReporter {
	/**
	 * Progress status
	 */
	final String progress = "Validation in progress";
	/**
	 * Fail status
	 */
	final String fail = "Validation fail";
	/**
	 * Success status
	 */
	final String succes = "Validation successful";
	
	/**
	 * Report a status
	 * @param message the status
	 */
	public void reportStatus(String message);

}
