package com.oxygenxml.docbookChecker;

/**
 * Implement used in JUnits
 * @author intern4
 *
 */
public class PlainWorkerReporter implements WorkerReporter {

	
	@Override
	public void reporteProgress(int progress) {
		System.out.println("Progress: "+progress);
		
	}
	
	@Override
	public void reportInProcessElement(String link) {
		System.out.println(link);
		
	}
}
