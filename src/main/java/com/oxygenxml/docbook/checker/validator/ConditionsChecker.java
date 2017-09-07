package com.oxygenxml.docbook.checker.validator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import com.oxygenxml.docbook.checker.parser.ConditionDetails;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.TabKeyGenerator;
/**
 * Checker for conditions.
 * @author intern4
 *
 */
public class ConditionsChecker {

	/**
	 * Problem reporter
	 */
	private ProblemReporter problemReporter;

	/**
	 * Constructor.
	 * @param problemReporter
	 */
	public ConditionsChecker(ProblemReporter problemReporter) {
		this.problemReporter = problemReporter;

	}

	/**
	 * Check the given conditions and report if a undefined condition was found
	 * according to defindeConditions.
	 * 
	 * @param url
	 *          The url.
	 * @param definedConditions
	 *          The defined conditions.
	 * @param foundConditions
	 *          The conditions found.
	 */
	public void validateAndReport(String url, LinkedHashMap<String, LinkedHashSet<String>> definedConditions,
			LinkedHashSet<ConditionDetails> foundConditions) {

		// iterate over conditions found.
		Iterator<ConditionDetails> iterKey = foundConditions.iterator();
		while (iterKey.hasNext()) {
			ConditionDetails conditionFound = iterKey.next();

			if (definedConditions.containsKey(conditionFound.getAttribute())) {

				// if this value is not defined preferences
				if (!definedConditions.get(conditionFound.getAttribute()).contains(conditionFound.getValue())) {
					// report undefined condition
					problemReporter.reportUndefinedConditions(conditionFound, TabKeyGenerator.generate(url, ""));
				}
			}
		}
	}
}
