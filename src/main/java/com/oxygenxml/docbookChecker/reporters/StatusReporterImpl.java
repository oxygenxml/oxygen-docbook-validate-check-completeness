package com.oxygenxml.docbookChecker.reporters;
/**
 * Report status in the console. 
 * Used in JUnits 
 */

public class StatusReporterImpl implements StatusReporter {
	/**
	 * Report status in the console.
	 */
	@Override
	public void reportStatus(String message) {
		System.out.println("******"+message+"*******");
		
	}

}
