package com.oxygenxml.docbook.checker.validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

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
	float progress = 0;

	/** 
	 *  It's true when is check the last set(flag)
	 */
	boolean isFinalCycle = false;

	/**
	 *  Name of current condition set
	 */
	String currentConditionSetName;

	private ConditionsChecker conditionsChecker;

	/**
	 * Set with processed external links
	 */
	Map<String, Exception> processedExternalLinks = new HashMap<String, Exception>();

	/**
	 * Validation worker interactor.
	 */
	private ValidationWorkerInteractor workerInteractor;

	/**
	 * Check links at a given URLs.
	 */
	@Override
	public void check(ParserCreator parserCreator, ProfilingConditionsInformations profilingInformation, List<String> urls, CheckerInteractor interactor,
			ProblemReporter problemReporter, StatusReporter statusReporter, ValidationWorkerInteractor workerInteractor,
			Translator translator) {

		this.workerInteractor = workerInteractor;
		conditionsChecker = new ConditionsChecker(problemReporter);
		
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
					problemReporter, statusReporter, translator);
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
	 * @param workerInteractor
	 * @param translator
	 */
	private void checkUsingConditionsSet(LinkedHashMap<String, LinkedHashSet<String>> guiConditions, String message,
			ParserCreator parserCreator, List<String> urls, ProfilingConditionsInformations profilingInformation, CheckerInteractor interactor, ProblemReporter problemReporter,
			StatusReporter statusReporter, Translator translator) {

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
			workerInteractor.reportInProcessElement(message + "Parse file: " + urls.get(i).toString());

			// add found links in toProcessLinks
			try {
				
				toProcessLinks = toProcessLinks.add(linksFinder.gatherLinksAndConditions(parserCreator, profilingInformation, urls.get(i), guiConditions, interactor));
				if(interactor.isReporteUndefinedConditions()){
					try{
						conditionsChecker.validateAndReport(urls.get(i), profilingInformation.getProfileConditions(interactor.getDocumentType()), toProcessLinks.getAllConditions());
						}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			
			} catch (SAXException e) {
				e.printStackTrace();
				problemReporter.reportException(e, TabKeyGenerator.generate(urls.get(i), ""), urls.get(i));
				statusReporter.reportStatus(translator.getTranslation(Tags.FAIL_STATUS));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				statusReporter.reportStatus(translator.getTranslation(Tags.FAIL_STATUS));
				problemReporter.reportException(e, TabKeyGenerator.generate(urls.get(i), ""), urls.get(i));
			} catch (IOException e) {
				e.printStackTrace();
				statusReporter.reportStatus(translator.getTranslation(Tags.FAIL_STATUS));
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

		// ------ check external links
		if (interactor.isCheckExternal() && !workerInteractor.isCancelled()) {
			checkExternalLinks(message, toProcessLinks, problemReporter);
		}

		// ------- check images
		if (interactor.isCheckImages() && !workerInteractor.isCancelled()) {
			checkImgLinks(message, toProcessLinks, problemReporter);
		}

		// -------- check internal links
		if (interactor.isCheckInternal() && !workerInteractor.isCancelled()) {
			InternalLinksChecker internalLinksChecker = new InternalLinksChecker(problemReporter, workerInteractor);
			internalLinksChecker.checkInternalLinks(toProcessLinks, message, currentConditionSetName, progress, isFinalCycle);
		}

		// report success status
		statusReporter.reportStatus(translator.getTranslation(Tags.SUCCESS_STATUS));

	}

	/**
	 * Check external links.
	 */
	private void checkExternalLinks(String message, DocumentDetails toProcessLinks, ProblemReporter problemReporter) {

		
		// get external links
		Iterator<Link> iter = toProcessLinks.getExternalLinks().iterator();

		// iterate over external links
		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			// report a note
			workerInteractor.reportInProcessElement(message + "Check external link: " + link.getRef());

			if (processedExternalLinks.containsKey(link.getRef())) {
				Exception ex = processedExternalLinks.get(link.getRef());
				if(ex != null){
					link.setException(processedExternalLinks.get(link.getRef()));
					problemReporter.reportBrokenLinks(link, TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
				}
			
			} else {
				try {
					// check the link
					ExternalLinksAndImagesChecker.check(link.getRef());
					processedExternalLinks.put(link.getRef(), null);
					
				} catch (IOException e) {
					link.setException(e);
					processedExternalLinks.put(link.getRef(), e);
					// report if the link in broken
					problemReporter.reportBrokenLinks(link,  TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
				}
			}

			// check if the thread was interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}
			// report the progress
			progress += toProcessLinks.getExternalProgress() * 95;
			workerInteractor.reportProgress((int) progress, isFinalCycle);

		}

	}

	/**
	 * Check images.
	 */
	private void checkImgLinks(String message, DocumentDetails toProcessLinks, ProblemReporter problemReporter) {

		// iterate over image links
		Iterator<Link> iter = toProcessLinks.getImgLinks().iterator();
		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			// report a note
			workerInteractor.reportInProcessElement(message + "Check image: " + link.getRef());

			try {
				// check the link
				ExternalLinksAndImagesChecker.check(link.getAbsoluteLocation().toString());

			} catch (IOException e) {
				link.setException(e);
				// report if the link is broken
				problemReporter.reportBrokenLinks(link,  TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
			}

			// check if the thread was interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}

			// report progress
			progress += toProcessLinks.getImageProgress() * 95;
			workerInteractor.reportProgress((int) progress, isFinalCycle);
		}

	}


	/**
	 * Get profile conditions sets from GUI.
	 * 
	 * @param interactor
	 * @return
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
