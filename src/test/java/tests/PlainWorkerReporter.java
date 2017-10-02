package tests;

import com.oxygenxml.docbook.checker.ValidationWorkerInteractor;

/**
 * Implement used in JUnits
 * @author intern4
 *
 */
public class PlainWorkerReporter implements ValidationWorkerInteractor {

	
	@Override
	public void reportNote(String link) {
		System.out.println(link);
		
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

}
