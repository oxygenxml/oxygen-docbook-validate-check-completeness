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
	 * @param tabKey The associated tab.
	 */
	void reportBrokenLinks(Link brokenLink, String tabKey);
	
	/**
	 * Report the exception found.
	 * @param exception The exception.
	 * * @param tabKey The associated tab.
	 */
	void reportException(Exception ex, String tabKey);
	
	/**
	 * Clear the problems reported in given tabKey.
	 * 
	 */
	public void clearReportedProblems(String tabKey);


	
	
}


