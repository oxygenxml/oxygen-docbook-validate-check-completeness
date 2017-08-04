package com.oxygenxml.profiling;

import java.util.Map;
import java.util.Set;
/**
 * Interface for report results of the background thread.
 * @author intern4
 *
 */
public interface ProfilingInformationWorkerReporter {

	/**
	 * Report the result of worker background thread in case ProfilingInformation.ATTRIBUTES .
	 * @param result the result
	 */
	void reportConditionsSetsWorkerFinish(Map<String, Map<String, Set<String>>> result);
	
	/**
	 * Report the result of worker background thread in case ProfilingInformation.CONDITIONS.
	 * @param result the result
	 */
	void reportConditionsWorkerFinish( Map<String, Set<String>> result);
	/**
	 * Report the result of worker background thread in case ProfilingInformation.ATTRIBUTES.
	 * @param result the result
	 */
	void reportConditionsAttributeNamesWorkerFinish(Set<String> result);

	/**
	 * Report the result of ProfileDocsToCheckWorker.
	 * @param result
	 */
	void reportConditionsProfileDocsToCheckWorkerFinish(Map<String, Set<String>> result);
}