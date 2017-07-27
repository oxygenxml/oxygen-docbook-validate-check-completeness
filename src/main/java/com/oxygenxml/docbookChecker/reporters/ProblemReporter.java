package com.oxygenxml.docbookChecker.reporters;

import java.util.List;

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
	public void reportBrokenLinks(Link brokenLink);
	
	/**
	 * Report the exception found.
	 * @param ex
	 */
	public void reportException(Exception ex);
	
	/**
	 * Clear the problems reported.
	 */
	public void clearReportedProblems();
	
	
}


