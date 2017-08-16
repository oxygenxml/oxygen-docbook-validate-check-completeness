package com.oxygenxml.ldocbookChecker.parser;


import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;
import com.oxygenxml.docbookChecker.translator.Translator;

import java.util.List;

import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.docbookChecker.WorkerReporter;

/**
 * Checker for links(external links, internal links and images).
 * @author intern4
 *
 */
public interface LinksChecker {

	/**
	 * Checks for broken links.
	 * @param parserCreator Creator for parser used in validation.
	 * @param urls	The URLs that should be validated.
	 * @param problemReporter	A ProblemReporter
	 * @param statusReporter A StatusReporter
	 * @param workerReporter A WorkerReporter
	 * @param translator A translator used for internationalization.
	 */
	public void check(ParserCreator parserCreator, List<String> urls, CheckerInteractor interactor, ProblemReporter problemReporter, 
			StatusReporter statusReporter, WorkerReporter workerReporter, Translator translator);
}
