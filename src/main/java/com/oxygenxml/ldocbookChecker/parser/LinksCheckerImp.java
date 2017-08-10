package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.docbookChecker.WorkerReporter;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

/**
 * Checker for links.
 * 
 * @author intern4
 *
 */
public class LinksCheckerImp implements LinksChecker {

	// is true when Thread is interrupted
	boolean threadInterrupted = false;

	// progress form progressMonitor
	float progress = 0;

	// is true when is check the last set.
	boolean isFinalCycle = false;

	/**
	 * Check links at a given URLs.
	 */
	@Override
	public void check(ParserCreator parserCreator, List<String> urls, CheckerInteractor interactor,
			ProblemReporter problemReporter, StatusReporter statusReporter, WorkerReporter workerReporter) {

		// get profile conditions sets from user
		Map<String, Map<String, Set<String>>> guiConditionsSets = getConditionsSetsFromGUI(interactor);
		
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
			if (threadInterrupted) {
				break;
			}

			// update message
			if (nuOfSets > 1) {
				nuOfCurrentSet++;
				message = "(" + nuOfCurrentSet + "/" + nuOfSets + " sets) ";
			}

			// test if it's the last set
			if (nuOfCurrentSet == nuOfSets) {
				isFinalCycle = true;
			}

			// get conditions of set
			String key = iterKey.next();
			Map<String, Set<String>> guiConditions = guiConditionsSets.get(key);

			// check with this conditions
			checkUsingConditionsSet(guiConditions, message, parserCreator, urls, interactor, problemReporter, statusReporter,
					workerReporter);
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
	 * @param workerReporter
	 */
	private void checkUsingConditionsSet(Map<String, Set<String>> guiConditions, String message,
			ParserCreator parserCreator, List<String> urls, CheckerInteractor interactor, ProblemReporter problemReporter,
			StatusReporter statusReporter, WorkerReporter workerReporter) {

		try {
			//report status
			statusReporter.reportStatus(StatusReporter.progress);

			//finder for links
			LinksFinder linksFinder = new LinksFinderImpl();
			
			//Lists with links to check
			LinkDetails toProcessLinks = new LinkDetails();

			//parse all URLs
			for (int i = 0; i < urls.size(); i++) {

				//report a note at worker
				workerReporter.reportInProcessElement(message + "Parse file: " + urls.get(i).toString());

				//add found links in toProcessLinks 
				toProcessLinks = toProcessLinks
						.add(linksFinder.gatherLinks(parserCreator, urls.get(i), guiConditions, interactor));

				//check if thread was interrupted
				if (Thread.interrupted() || threadInterrupted) {
					threadInterrupted = true;
					break;
				}
				
				//calculate and report progress
				progress = ((i + 1) * 5 / urls.size());
				workerReporter.reporteProgress((int) progress, isFinalCycle);
			}

			// ------ check external links
			if (interactor.isSelectedCheckExternal() && !threadInterrupted) {
				checkExternalLinks(message, toProcessLinks, problemReporter, guiConditions, workerReporter);
			}

			
			// ------- check images
			if (interactor.isSelectedCheckImages() && !threadInterrupted) {
				checkImgLinks(message, toProcessLinks, problemReporter, guiConditions, workerReporter);
			}

			
			// -------- check internal links
			if (interactor.isSelectedCheckInternal() && !threadInterrupted) {
				checkInternalLinks(message, toProcessLinks, problemReporter, guiConditions, workerReporter);
			}

			// report success status
			statusReporter.reportStatus(StatusReporter.succes);

			
		} catch (SAXException e) {
			System.err.println("catch");
			problemReporter.reportException(e);
			statusReporter.reportStatus(StatusReporter.fail);
		} catch (ParserConfigurationException e) {
			System.err.println("catch");

			statusReporter.reportStatus(StatusReporter.fail);
			problemReporter.reportException(e);
		} catch (IOException e) {
			System.err.println("catch");

			statusReporter.reportStatus(StatusReporter.fail);
			problemReporter.reportException(e);
		}

	}

	/**
	 * Check external links.
	 */
	private void checkExternalLinks(String message, LinkDetails toProcessLinks, ProblemReporter problemReporter,
			Map<String, Set<String>> guiConditions, WorkerReporter workerReporter) {

		//get external links
		Iterator<Link> iter = toProcessLinks.getExternalLinks().iterator();

		//iterate over external links 
		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			//check if the link is not filter
			if (!link.isFilter(guiConditions)) {

				//report a note
				workerReporter.reportInProcessElement(message + "Check external link: " + link.getRef());

				try {
					//check the link
					ConnectionLinkChecker.check(link.getRef());
					
				}catch (IOException e) {
					link.setType(LinkType.EXTERNAL);
					link.setException(e);
					//report if the link in broken
					problemReporter.reportBrokenLinks(link);
				}
			}

			//check if the thread was interrupted
			if (Thread.interrupted()) {
				threadInterrupted = true;
				break;
			}
			//report the progress
			progress += toProcessLinks.getExternalProgress() * 95;
			workerReporter.reporteProgress((int) progress, isFinalCycle);
		}
	}

	/**
	 * Check images.
	 */
	private void checkImgLinks(String message, LinkDetails toProcessLinks, ProblemReporter problemReporter,
			Map<String, Set<String>> guiConditions, WorkerReporter workerReporter) {

		//iterate over image links
		Iterator<Link> iter = toProcessLinks.getImgLinks().iterator();
		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			//check if the link is not filter
			if (!link.isFilter(guiConditions)) {

				//report a note
				workerReporter.reportInProcessElement(message + "Check image: " + link.getRef());

				try {
					//check the link
					ConnectionLinkChecker.check(link.getAbsoluteLocation().toString());

				} catch (IOException e) {
					link.setType(LinkType.IMAGE);
					link.setException(e);
					//report if the link is broken
					problemReporter.reportBrokenLinks(link);
				}
			}

			//check if the thread was interrupted
			if (Thread.interrupted()) {
				threadInterrupted = true;
				break;
			}
			
			//report progress
			progress += toProcessLinks.getImageProgress() * 95;
			workerReporter.reporteProgress((int) progress, isFinalCycle);
		}

	}

	/**
	 * Check internal links.
	 */
	private void checkInternalLinks(String message, LinkDetails toProcessLinks, ProblemReporter problemReporter,
			Map<String, Set<String>> guiConditions, WorkerReporter workerReporter) {

		//get the IDs
		List<Id> paraIds = toProcessLinks.getParaIds();

		//iterate over the internal links
		Iterator<Link> iter = toProcessLinks.getInternalLinks().iterator();
		while (iter.hasNext()) {

			Link link = (Link) iter.next();

			// test if link is filter
			if (!link.isFilter(guiConditions)) {
				
				//report a note
				workerReporter.reportInProcessElement(message + "Check internal link: " + link.getRef());

				// check if the list with IDs doesn't contain the reference of link.
				Boolean linkPoints = linkPointsToID(paraIds, link, guiConditions);
				
				if (linkPoints == null) {
					//referred ID isn't in IDs list
					link.setType(LinkType.INTERNAL);
					link.setException(new Exception("ID: " + link.getRef() + " not found"));
					problemReporter.reportBrokenLinks(link);
				} else if (false == linkPoints) {
					//referred ID is in a filtered zone  
					link.setType(LinkType.INTERNAL);
					link.setException(new Exception("Reference to ID " + link.getRef() + " defined in filtered out content."));
					problemReporter.reportBrokenLinks(link);
				}

			}

			//check if thread was interrupted
			if (Thread.interrupted()) {
				threadInterrupted = true;
				break;
			}
			
			//report a progress
			progress += toProcessLinks.getInternalProgress() * 95;
			workerReporter.reporteProgress((int) progress, isFinalCycle);

		}
	}

	/**
	 * Check if the link refers at a valid id from the IDs list.
	 * 
	 * @param list
	 *          the list with ID's
	 * @param link
	 *          the reference(link)
	 * @return <code>true</code> if the link refers at a valid id ,
	 *         <code>false</code> if refers at a filtered id <code>null</code>> if
	 *         wasn't found the referred id in IDs list
	 */
	private Boolean linkPointsToID(List<Id> list, Link link, Map<String, Set<String>> conditionsTableGUI) {
		Boolean toReturn = null;

		// iterates over IDs list
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getId().equals(link.getRef())) {
				// was found the referred id
				// check if the id is filter
				if (!list.get(i).isFilter(conditionsTableGUI)) {
					// was found a valid id
					return true;
				} else {
					// id was found, but is filter
					toReturn = false;
				}
			}
		}
		return toReturn;
	}

	/**
	 * Get profile conditions sets from GUI.
	 * 
	 * @param interactor
	 * @return
	 */
	private Map<String, Map<String, Set<String>>> getConditionsSetsFromGUI(CheckerInteractor interactor) {
		// profile conditions sets from user
		Map<String, Map<String, Set<String>>> guiConditionsSets = new HashMap<String, Map<String, Set<String>>>();

		// if is selected to use profile conditions
		if (interactor.isSelectedCheckUsingProfile()) {

			// if is selected to configure a condition set
			if (interactor.isSelectedConfigConditionsSet()) {
				// add this set in the map (guiConditionsSets)
				// it's one element in map
				guiConditionsSets.put("set", interactor.getConditionsTableRows());
			}
			// is selected to use all available condition sets
			else {
				ProfilingConditionsInformations profilingConditionsInformations = new ProfilingConditionsInformationsImpl();
				// add all available condition sets in map (guiConditionsSets)
				guiConditionsSets
						.putAll(profilingConditionsInformations.getConditionsSets(ProfilingConditionsInformations.DOCBOOK));
			}
		}else{
			//doesn't use profile conditions;
			guiConditionsSets.put("withoutConds", new HashMap<String, Set<String>>());
		}

		return guiConditionsSets;
	}
}
