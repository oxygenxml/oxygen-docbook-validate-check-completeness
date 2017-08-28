package com.oxygenxml.profiling;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
/**
 * Interface for report results of the background thread.
 * @author intern4
 *
 */
public interface ProfileConditionsFromDocsWorkerReporter {
	/**
	 * Report the result of ProfileConditionsFromDocs.
	 * @param result The results.
	 */
	void reportProfileConditionsFromDocsWorkerFinish(LinkedHashMap<String, LinkedHashSet<String>> result);
	
}