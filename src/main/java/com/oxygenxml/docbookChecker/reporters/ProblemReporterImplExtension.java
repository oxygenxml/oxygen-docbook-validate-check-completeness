package com.oxygenxml.docbookChecker.reporters;

import com.oxygenxml.ldocbookChecker.parser.Link;

public class ProblemReporterImplExtension implements ProblemReporter{

	@Override
	public void reportBrokenLinks(Link brokenLink) {
		System.out.println(brokenLink.toString() +"\n");
		
	}

	@Override
	public void reportException(Exception ex) {
		System.out.println(ex.toString());
	}

}
