package tests;

import com.oxygenxml.docbook.checker.ValidationWorkerReporter;

/**
 * Implement used in JUnits
 * @author intern4
 *
 */
public class PlainWorkerReporter implements ValidationWorkerReporter {

	
	@Override
	public void reportProgress(int progress, boolean isFinalCycle) {
		System.out.println("Progress: "+progress);
		
	}
	
	@Override
	public void reportInProcessElement(String link) {
		System.out.println(link);
		
	}

}
