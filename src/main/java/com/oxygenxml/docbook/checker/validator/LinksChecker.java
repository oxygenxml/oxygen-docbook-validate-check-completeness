package com.oxygenxml.docbook.checker.validator;


import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.docbook.checker.WorkerReporter;
import com.oxygenxml.docbook.checker.parser.ParserCreator;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.StatusReporter;
import com.oxygenxml.docbook.checker.translator.Translator;

import java.util.List;

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
