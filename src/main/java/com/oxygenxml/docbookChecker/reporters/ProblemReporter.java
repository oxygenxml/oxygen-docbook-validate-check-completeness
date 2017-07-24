package com.oxygenxml.docbookChecker.reporters;

import java.util.List;

import com.oxygenxml.ldocbookChecker.parser.Link;

/**
 * Interface for report broken links and exceptions.
 * @author intern4
 *
 */
public interface ProblemReporter {
	
	public void reportBrokenLinks(Link brokenLink);
	
	public void reportException(Exception ex);
	
}
