package com.oxygenxml.docbook.checker.validator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.TabKey;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
/**
 * Checker for conditions.
 * @author intern4
 *
 */
public class ConditionsChecker {

	private ProblemReporter problemReporter;

	public ConditionsChecker(ProblemReporter problemReporter) {
		this.problemReporter = problemReporter;

	}

	/**
	 * Check the given conditions and report if a undefined condition was found.
	 * @param url
	 * @param profilingInformation
	 * @param conditions
	 */
	public void validateAndReport(String url, ProfilingConditionsInformations profilingInformation  ,LinkedHashMap<String, LinkedHashSet<String>> conditions ) {

			LinkedHashMap<String, LinkedHashSet<String>> definedConditions = profilingInformation
					.getProfileConditions(ProfilingConditionsInformations.ALL_DOCBOOKS);
			
			
			Iterator<String> iterKey = conditions.keySet().iterator();
			while (iterKey.hasNext()) {
				String key = iterKey.next();
				
				if (definedConditions.containsKey(key)) {
					Iterator<String> iterValue = conditions.get(key).iterator();
					while (iterValue.hasNext()) {
						String value = iterValue.next();
						if (!definedConditions.get(key).contains(value)) {
							problemReporter.reportUndefinedConditions(key, value,
									 TabKey.generate(url, ""));
						}
					}
				}
			}
	}
}
