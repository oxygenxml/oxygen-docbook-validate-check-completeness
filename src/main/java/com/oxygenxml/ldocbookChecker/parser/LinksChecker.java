package com.oxygenxml.ldocbookChecker.parser;


import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;

public interface LinksChecker {

	/**
	 * Checks for broken links.
	 * @param parserCreator parser creator for parse the given url.
	 * @param url	the url 
	 * @param settings settings for GUI
	 * @param problemReporter	problem reporter
	 */
	public void check(ParserCreator parserCreator, String url, CheckerInteractor interactor, ProblemReporter problemReporter, StatusReporter statusReporter);
}
