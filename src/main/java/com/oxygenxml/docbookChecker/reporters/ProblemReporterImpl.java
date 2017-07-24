package com.oxygenxml.docbookChecker.reporters;

import java.util.ArrayList;
import java.util.List;

import com.oxygenxml.ldocbookChecker.parser.Link;

/**
 * Save broken links and exceptions in lists
 * @author intern4
 *
 */
public class ProblemReporterImpl implements ProblemReporter{

	private List<Link> brokenLinks = new ArrayList<Link>();
	
	private List<Exception> exceptions = new ArrayList<Exception>();
	
 	@Override
	public void reportBrokenLinks(Link brokenLink) {
		this.brokenLinks.add( brokenLink);
	}

	@Override
	public void reportException(Exception ex) {
		exceptions.add(ex);
	}

	public List<Link> getBrokenLinks() {
		return brokenLinks;
	}

	public List<Exception> getExceptions() {
		return exceptions;
	}

}
