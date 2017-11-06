package com.oxygenxml.profiling;

import java.util.LinkedHashSet;
import java.util.Map;
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
	void reportProfileConditionsFromDocsWorkerFinish(Map<String, LinkedHashSet<String>> result);
	
}