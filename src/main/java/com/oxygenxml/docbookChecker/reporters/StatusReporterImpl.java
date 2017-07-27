package com.oxygenxml.docbookChecker.reporters;

public class StatusReporterImpl implements StatusReporter {
	
	@Override
	public void reportStatus(String message) {
		System.out.println("******"+message+"*******");
		
	}

}
