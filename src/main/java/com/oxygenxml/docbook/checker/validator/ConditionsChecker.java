package com.oxygenxml.docbook.checker.validator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import com.oxygenxml.docbook.checker.ValidationWorkerInteractor;
import com.oxygenxml.docbook.checker.parser.ConditionDetails;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.TabKeyGenerator;

/**
 * Checker for conditions.
 * 
 * @author intern4
 *
 */
public class ConditionsChecker {

	/**
	 * Problem reporter
	 */
	private ProblemReporter problemReporter;
	private ValidationWorkerInteractor workerInteractor;

	/**
	 * Constructor.
	 * 
	 * @param problemReporter
	 * @param workerInteractor
	 */
	public ConditionsChecker(ProblemReporter problemReporter, ValidationWorkerInteractor workerInteractor) {
		this.problemReporter = problemReporter;
		this.workerInteractor = workerInteractor;

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

			// check if thread was interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}

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
