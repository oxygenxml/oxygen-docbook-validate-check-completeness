package com.oxygenxml.ldocbookChecker.parser;


import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;

import java.util.List;

import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.docbookChecker.WorkerReporter;

public interface LinksChecker {

	/**
	 * Checks for broken links.
	 * @param parserCreator parser creator for parse the given url.
	 * @param url	the url 
	 * @param settings settings for GUI
	 * @param problemReporter	problem reporter
	 */
	public void check(ParserCreator parserCreator, List<String> url, CheckerInteractor interactor, ProblemReporter problemReporter, StatusReporter statusReporter, WorkerReporter workerReporter);
}
