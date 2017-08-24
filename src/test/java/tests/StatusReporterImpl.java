package tests;

import com.oxygenxml.docbook.checker.reporters.StatusReporter;

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
