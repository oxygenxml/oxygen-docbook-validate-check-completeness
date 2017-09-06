package com.oxygenxml.docbook.checker.reporters;

import com.oxygenxml.docbook.checker.parser.Link;

/**
 * Interface for report and clear broken links and exceptions.
 * @author intern4
 *
 */
public interface ProblemReporter {
	
	/**
	 * Report the broken link found.
	 * @param brokenLink The broken link.
	 * @param ex The exception found.
	 * @param tabKey The associated tab.
	 */
	void reportBrokenLinks(Link brokenLink, Exception ex, String tabKey);
	
	/**
	 * Report the exception found.
	 * @param ex The exception found.
	 * @param tabKey The associated tab.
	 * @param The document with problem.
	 */
	void reportException(Exception ex, String tabKey, String document);

	/**
	 * Report a undefined condition.
	 * @param attribute The attribute of condition.
	 * @param value 	The value of condition.
	 * @param tabKey The associated tab.
	 */
	void reportUndefinedConditions(String attribute, String value, String tabKey);
	
	/**
	 * Clear the problems reported in given tabKey.
	 * @param tabKey The tabKey.
	 */
	public void clearReportedProblems(String tabKey);

	
	
}


