package com.oxygenxml.docbook.checker.validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.docbook.checker.ValidationWorkerInteractor;
import com.oxygenxml.docbook.checker.parser.DocumentDetails;
import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.parser.LinksFinder;
import com.oxygenxml.docbook.checker.parser.LinksFinderImpl;
import com.oxygenxml.docbook.checker.parser.ParserCreator;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.StatusReporter;
import com.oxygenxml.docbook.checker.reporters.TabKeyGenerator;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

/**
 * Checker for links.
 * 
 * @author intern4
 *
 */
public class DocumentCheckerImp implements DocumentChecker {

	/**
	 * The progress
	 */
	private float progress = 0;

	/** 
	 *  It's true when is check the last set(flag)
	 */
	private boolean isFinalCycle = false;

	/**
	 *  Name of current condition set
	 */
	private String currentConditionSetName;

	
	/**
	 * The status of the process.
	 */
	private String status ;
	
	/**
	 * Checker for conditions
	 */
	private ConditionsChecker conditionsChecker;

	/**
	 * Set with processed external links
	 */
	private Map<String, Exception> processedExternalLinks = new HashMap<String, Exception>();

	/**
	 * Logger
	 */
	 private static final Logger logger = Logger.getLogger(DocumentCheckerImp.class);
	
	/**
	 * Validation worker interactor.
	 */
	private ValidationWorkerInteractor workerInteractor;

	/**
	 * Translator
	 */
	private Translator translator;


	/**
	 * Check links at a given URLs.
	 * 
	 * @param parserCreator
	 *          Parser creator.
	 * @param profilingInformation
	 *          Profiling information.
	 * @param urls
	 *          List with URLs.
	 * @param interactor
	 *          CheckerInteractor
	 * @param problemReporter
	 *          Problem reporter
	 * @param statusReporter
	 *          Status reporter.
	 * @param workerReporter
	 *          Validation worker reporter
	 * @param translator
	 *          Translator
	 */
	@Override
	public void check(ParserCreator parserCreator, ProfilingConditionsInformations profilingInformation, List<String> urls, CheckerInteractor interactor,
			ProblemReporter problemReporter, StatusReporter statusReporter, ValidationWorkerInteractor workerInteractor,
			Translator translator) {

		this.workerInteractor = workerInteractor;
		this.translator = translator;
		
		conditionsChecker = new ConditionsChecker(problemReporter);
		
		//set the initial status
		status = translator.getTranslation(Tags.SUCCESS_STATUS);
		
		// get profile conditions sets from user
		LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> guiConditionsSets = getConditionsSetsFromGUI(interactor);

		// number of current set
		int nuOfCurrentSet = 0;
		// total number of sets
		int nuOfSets = guiConditionsSets.size();

		// message added at progressMonitor's note
		String message = "";

		// iterate over sets
		Iterator<String> iterKey = guiConditionsSets.keySet().iterator();
		while (iterKey.hasNext()) {
			// break the iterate if thread is interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}

			progress = 0;

			// update message
			if (nuOfSets > 1) {
				nuOfCurrentSet++;
				message = "(" + nuOfCurrentSet + "/" + nuOfSets + " sets) ";
			}

			// test if it's the last set
			if (nuOfCurrentSet == nuOfSets) {
				isFinalCycle = true;
			}

			// get condition set name
			String key = iterKey.next();

			currentConditionSetName = key;

			// get the conditions of current set.
			LinkedHashMap<String, LinkedHashSet<String>> guiConditions = guiConditionsSets.get(key);

			// check with this conditions
			checkUsingConditionsSet(guiConditions, message, parserCreator, urls,  profilingInformation, interactor, 
					problemReporter, statusReporter);
		}
	}

	/**
	 * Check links at the given URLs using the given condition set;
	 * 
	 * @param guiConditions
	 *          The conditions set
	 * @param message
	 * @param parserCreator
	 * @param urls
	 * @param interactor
	 * @param problemReporter
	 * @param statusReporter
	 * @param translator
	 */
	private void checkUsingConditionsSet(LinkedHashMap<String, LinkedHashSet<String>> guiConditions, String message,
			ParserCreator parserCreator, List<String> urls, ProfilingConditionsInformations profilingInformation, CheckerInteractor interactor, ProblemReporter problemReporter,
			StatusReporter statusReporter) {

		// report status
		statusReporter.reportStatus(translator.getTranslation(Tags.PROGRESS_STATUS));

		// finder for links
		LinksFinder linksFinder = new LinksFinderImpl();

		// Lists with links to check
		DocumentDetails toProcessLinks = new DocumentDetails();

		// Iterate over URLs
		for (int i = 0; i < urls.size(); i++) {

			// clear the reported problems from the currentTab if this was used in
			// other check.
			problemReporter.clearReportedProblems(TabKeyGenerator.generate(urls.get(i), ""));
			problemReporter.clearReportedProblems(TabKeyGenerator.generate(urls.get(i), currentConditionSetName));
			
			
			// report a note at worker
			workerInteractor.reportNote(message + "Parse file: " + urls.get(i).toString());

			// add found links in toProcessLinks
			try {
				DocumentDetails result = linksFinder.gatherLinksAndConditions(parserCreator, profilingInformation, urls.get(i), guiConditions, interactor);
				toProcessLinks = toProcessLinks.add(result);
				
				if(interactor.isReporteUndefinedConditions()){
					try{
						conditionsChecker.validateAndReport(urls.get(i), profilingInformation.getProfileConditions(interactor.getDocumentType()), result.getAllConditions());
						}
					catch (Exception e) {
						logger.debug(e.getMessage(), e);
					}
				}
			
			} catch (SAXException e) {
				problemReporter.reportException(e, TabKeyGenerator.generate(urls.get(i), ""), urls.get(i));
				status = translator.getTranslation(Tags.FAIL_STATUS);
			} catch (ParserConfigurationException e) {
				status = translator.getTranslation(Tags.FAIL_STATUS);
				problemReporter.reportException(e, TabKeyGenerator.generate(urls.get(i), ""), urls.get(i));
			} catch (IOException e) {
				status = translator.getTranslation(Tags.FAIL_STATUS);
				problemReporter.reportException(e, TabKeyGenerator.generate(urls.get(i), ""), urls.get(i));
			}

			// check if thread was interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}

			// calculate and report progress
			progress = ((i + 1) * 5 / urls.size());
			workerInteractor.reportProgress((int) progress, isFinalCycle);
		}

		//create a progressDeterminator
		ProgressDeterminator progressDeterminator = new ProgressDeterminator(toProcessLinks);
		
		// ------ check external links
		if (interactor.isCheckExternal() && !workerInteractor.isCancelled()) {
			checkExternalLinks(message, toProcessLinks, progressDeterminator, problemReporter);
		}

		// ------- check images
		if (interactor.isCheckImages() && !workerInteractor.isCancelled()) {
			checkImgLinks(message, toProcessLinks, progressDeterminator, problemReporter);
		}

		// -------- check internal links
		if (interactor.isCheckInternal() && !workerInteractor.isCancelled()) {
			InternalLinksChecker internalLinksChecker = new InternalLinksChecker(problemReporter, workerInteractor, translator);
			internalLinksChecker.checkInternalLinks(toProcessLinks, progressDeterminator, message, currentConditionSetName, progress, isFinalCycle, status);
		}

		// report success status
		statusReporter.reportStatus(status);

	}

	/**
	 * Check external links.
	 */
	private void checkExternalLinks(String message, DocumentDetails toProcessLinks, ProgressDeterminator progressDeterminator,  ProblemReporter problemReporter) {

		
		// get external links
		Iterator<Link> iter = toProcessLinks.getExternalLinks().iterator();

		// iterate over external links
		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			// report a note
			workerInteractor.reportNote(message + "Check external link: " + link.getRef());

			if (processedExternalLinks.containsKey(link.getRef())) {
				Exception ex = processedExternalLinks.get(link.getRef());
				if(ex != null){
					problemReporter.reportBrokenLinks(link, ex,TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
				}
			
			} else {
				try {
					// check the link
					ExternalLinksAndImagesChecker.check(link.getRef());
					processedExternalLinks.put(link.getRef(), null);
					
				} catch (IOException ex) {
					processedExternalLinks.put(link.getRef(), ex);
					//change the status
					status = translator.getTranslation(Tags.FAIL_STATUS);
					// report if the link in broken
					problemReporter.reportBrokenLinks(link, ex,  TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
				}
			}

			// check if the thread was interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}
			// report the progress
			progress += progressDeterminator.getExternalProgress() * 95;
			workerInteractor.reportProgress((int) progress, isFinalCycle);

		}

	}

	/**
	 * Check images.
	 */
	private void checkImgLinks(String message, DocumentDetails toProcessLinks, ProgressDeterminator progressDeterminator, ProblemReporter problemReporter) {

		// iterate over image links
		Iterator<Link> iter = toProcessLinks.getImgLinks().iterator();
		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			// report a note
			workerInteractor.reportNote(message + "Check image: " + link.getRef());

			try {
				// check the link
				ExternalLinksAndImagesChecker.check(link.getAbsoluteLocation().toString());

			} catch (IOException ex) {
				//change the status
				status = translator.getTranslation(Tags.FAIL_STATUS);
				// report if the link is broken
				problemReporter.reportBrokenLinks(link, ex, TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
			}

			// check if the thread was interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}

			// report progress
			progress += progressDeterminator.getImageProgress() * 95;
			workerInteractor.reportProgress((int) progress, isFinalCycle);
		}

	}


	/**
	 * Get profile conditions sets from GUI according to checkerInteractor.
	 * 
	 * @param interactor CheckerInteractor.
	 * @return The conditions.
	 */
	private LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> getConditionsSetsFromGUI(
			CheckerInteractor interactor) {
		// profile conditions sets from user
		LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> guiConditionsSets = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>>();

		// if is selected to use profile conditions
		if (interactor.isUsingProfile()) {

			// if is selected to configure a condition set
			if (interactor.isUseManuallyConfiguredConditionsSet()) {
				// add this set in the map (guiConditionsSets)
				// it's one element in map
				guiConditionsSets.put("Local set", interactor.getDefinedConditions());
			}
			// is selected to use all available condition sets
			else {
				ProfilingConditionsInformations profilingConditionsInformations = new ProfilingConditionsInformationsImpl();
				// add all available condition sets in map (guiConditionsSets)
				guiConditionsSets
						.putAll(profilingConditionsInformations.getConditionsSets(interactor.getDocumentType() ) );
				if(guiConditionsSets.isEmpty()){
					//it's not defined a condition set
					guiConditionsSets.put("", new LinkedHashMap<String, LinkedHashSet<String>>());
				}
				
			}
		} else {
			// doesn't use profile conditions;
			guiConditionsSets.put("", new LinkedHashMap<String, LinkedHashSet<String>>());
		}

		return guiConditionsSets;
	}

	
}
