package com.oxygenxml.docbook.checker.validator;


import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.docbook.checker.ValidationWorkerInteractor;
import com.oxygenxml.docbook.checker.parser.ParserCreator;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.StatusReporter;
import com.oxygenxml.docbook.checker.translator.Translator;
import com.oxygenxml.profiling.ProfilingConditionsInformations;

import java.util.List;

/**
 * Checker for links(external links, internal links and images).
 * @author intern4
 *
 */
public interface LinksChecker {

	/**
	 * Method for check links and conditions from the given URLs.
	 * @param parserCreator
	 * @param profilingInformation
	 * @param urls
	 * @param interactor
	 * @param problemReporter
	 * @param statusReporter
	 * @param workerReporter
	 * @param translator
	 */
	public void check(ParserCreator parserCreator, ProfilingConditionsInformations profilingInformation , List<String> urls, CheckerInteractor interactor, ProblemReporter problemReporter, 
			StatusReporter statusReporter, ValidationWorkerInteractor workerReporter, Translator translator);
}
