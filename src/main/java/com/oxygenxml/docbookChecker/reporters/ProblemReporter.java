package com.oxygenxml.docbookChecker.reporters;

import com.oxygenxml.ldocbookChecker.parser.Link;

/**
 * Interface for report and clear broken links and exceptions.
 * @author intern4
 *
 */
public interface ProblemReporter {
	
	/**
	 * Report the broken link found.
	 * @param brokenLink 
	 */
	public void reportBrokenLinks(Link brokenLink, String conditionSetName);
	
	/**
	 * Report the exception found.
	 * @param exception
	 */
	public void reportException(Exception exception);
	
	/**
	 * Clear the problems reported.
	 */
	public void clearReportedProblems();
	
	
}


