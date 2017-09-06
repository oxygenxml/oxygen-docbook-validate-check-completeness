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
public interface DocumentChecker {

/**
 *  Method for check links and conditions from the given URLs.
 *  
 * @param parserCreator Parser creator.
 * @param profilingInformation Profiling information.
 * @param urls 	List with URLs.
 * @param interactor CheckerInteractor
 * @param problemReporter Problem reporter
 * @param statusReporter Status reporter.
 * @param workerReporter Validation worker reporter
 * @param translator Translator
 */
	public void check(ParserCreator parserCreator, ProfilingConditionsInformations profilingInformation , List<String> urls, CheckerInteractor interactor, ProblemReporter problemReporter, 
			StatusReporter statusReporter, ValidationWorkerInteractor workerReporter, Translator translator);
}
