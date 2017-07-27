package com.oxygenxml.docbookChecker.reporters;

public interface StatusReporter {
	
	final String progress = "Validation in progress";
	final String fail = "Validation fail";
	final String succes = "Validation successful";
	/**
	 * Report finish
	 */
	public void reportStatus(String message);

}
