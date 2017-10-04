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
import com.oxygenxml.docbook.checker.parser.AssemblyTopicId;
import com.oxygenxml.docbook.checker.parser.DocumentDetails;
import com.oxygenxml.docbook.checker.parser.Id;
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
public class DocumentCheckerImp implements DocumentChecker, StatusChanger {

	/**
	 * Name of current condition set
	 */
	private String currentConditionSetName;

	/**
	 * The status of the process.
	 */
	private String status;

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
	 * Link finder.
	 */
	private LinksFinder linksFinder = new LinksFinderImpl();

	/**
	 * Parser creator.
	 */
	private ParserCreator parserCreator;

	/**
	 * Message added at progress dialog's note
	 */
	private String message = "";

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
	public void check(ParserCreator parserCreator, ProfilingConditionsInformations profilingInformation,
			List<String> urls, CheckerInteractor interactor, ProblemReporter problemReporter, StatusReporter statusReporter,
			ValidationWorkerInteractor workerInteractor, Translator translator) {

		this.parserCreator = parserCreator;
		this.workerInteractor = workerInteractor;
		this.translator = translator;

		conditionsChecker = new ConditionsChecker(problemReporter);

		// set the initial status
		status = translator.getTranslation(Tags.SUCCESS_STATUS);

		// get profile conditions sets from user
		LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> guiConditionsSets = getConditionsSetsFromGUI(
				interactor);

		// number of current set
		int nuOfCurrentSet = 0;
		// total number of sets
		int nuOfSets = guiConditionsSets.size();

		// iterate over sets
		Iterator<String> iterKey = guiConditionsSets.keySet().iterator();
		while (iterKey.hasNext()) {
			// break the iterate if thread is interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}

			// update message
			if (nuOfSets > 1) {
				nuOfCurrentSet++;
				message = "(" + nuOfCurrentSet + "/" + nuOfSets + " sets) ";
			}

			// get condition set name
			String key = iterKey.next();

			currentConditionSetName = key;

			// get the conditions of current set.
			LinkedHashMap<String, LinkedHashSet<String>> guiConditions = guiConditionsSets.get(key);

			// check with this conditions
			checkUsingConditionsSet(guiConditions, urls, profilingInformation, interactor, problemReporter, statusReporter);
		}
	}

	/**
	 * Check links at the given URLs using the given condition set;
	 * 
	 * @param guiConditions
	 *          The conditions set
	 * @param urls
	 *          The URLs that will be parse.
	 * @param interactor
	 *          Checker interactor
	 * @param problemReporter
	 *          Problem reporter.
	 * @param statusReporter
	 *          Status reporter
	 * @param translator
	 *          Translator
	 */
	private void checkUsingConditionsSet(LinkedHashMap<String, LinkedHashSet<String>> guiConditions, List<String> urls,
			ProfilingConditionsInformations profilingInformation, CheckerInteractor interactor,
			ProblemReporter problemReporter, StatusReporter statusReporter) {

		// report status
		statusReporter.reportStatus(translator.getTranslation(Tags.PROGRESS_STATUS));

		// Lists with links to check
		DocumentDetails toProcessLinks = new DocumentDetails();

		// Iterate over URLs
		for (int i = 0; i < urls.size(); i++) {

			// check if thread was interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}

			// clear the reported problems from the currentTab if this was used in
			// other check.
			problemReporter.clearReportedProblems(TabKeyGenerator.generate(urls.get(i), ""));
			problemReporter.clearReportedProblems(TabKeyGenerator.generate(urls.get(i), currentConditionSetName));

			// report a note at worker
			workerInteractor.reportNote(message + "Parse file: " + urls.get(i));

			// add found links in toProcessLinks
			try {
				toProcessLinks = linksFinder.gatherLinksAndConditions(parserCreator, profilingInformation, urls.get(i),
						urls.get(i), guiConditions, interactor);

				if (interactor.isReporteUndefinedConditions()) {
					try {
						conditionsChecker.validateAndReport(urls.get(i),
								profilingInformation.getProfileConditions(interactor.getDocumentType()),
								toProcessLinks.getAllConditions());
					} catch (Exception e) {
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

			
			//report duplicate paragraph Ids 
			reportDuplicateIds(toProcessLinks, problemReporter, TabKeyGenerator.generate(urls.get(i), currentConditionSetName) );
			
			// check images, external and internal links.
			checkExternalInternalImage(interactor, toProcessLinks, problemReporter);

			// check assembly files(topics)
			checkAssemblyFiles(urls.get(i), toProcessLinks, interactor, problemReporter, profilingInformation, guiConditions);

		}

		// report the status
		statusReporter.reportStatus(status);
	}

	/**
	 * Check images, external and internal links from the given document details.
	 * 
	 * @param interactor
	 *          Checker interactor
	 * @param toProcessLinks
	 *          Object that contains links to process.
	 * @param problemReporter
	 *          Problem reporter.
	 */
	private void checkExternalInternalImage(CheckerInteractor interactor, DocumentDetails toProcessLinks,
			ProblemReporter problemReporter) {
		// ------ check external links
		if (interactor.isCheckExternal() && !workerInteractor.isCancelled()) {
			checkExternalLinks(toProcessLinks, problemReporter);
		}

		// ------- check images
		if (interactor.isCheckImages() && !workerInteractor.isCancelled()) {
			checkImgLinks(toProcessLinks, problemReporter);
		}

		// -------- check internal links
		if (interactor.isCheckInternal() && !workerInteractor.isCancelled()) {
			InternalLinksChecker internalLinksChecker = new InternalLinksChecker(problemReporter, workerInteractor,
					translator);
			internalLinksChecker.checkInternalLinks(toProcessLinks, message, currentConditionSetName, this);
		}
	}

	/**
	 * Check the assembly files(topics).
	 * 
	 * @param parentDocumentURL
	 *          The URL of the document where was found the assembly files.
	 * @param toProcessLinks
	 *          Object that contains links that will be check.
	 * @param interactor
	 *          Checker interactor.
	 * @param problemReporter
	 *          Problem reporter.
	 * @param profilingInformation
	 *          ProfilingConditionsInformations
	 * @param guiConditions
	 *          conditions from GUI.
	 */
	private void checkAssemblyFiles(String parentDocumentURL, DocumentDetails toProcessLinks,
			CheckerInteractor interactor, ProblemReporter problemReporter,
			ProfilingConditionsInformations profilingInformation,
			LinkedHashMap<String, LinkedHashSet<String>> guiConditions) {

		// -------- check assembly files
		AssembledFilesFinder assemblyFilesFinder = new AssembledFilesFinder(problemReporter, workerInteractor, translator);

		// gather the valid paths to topic files. (assemblyFiles)
		List<AssemblyTopicId> assemblyFiles = assemblyFilesFinder.findValidTopicsAndValidate(toProcessLinks, message,
				currentConditionSetName, this);

		// parse the files and gather the links.
		if (!assemblyFiles.isEmpty()) {

			DocumentDetails toProcessLinksFromAssemblyFiles = new DocumentDetails();
			DocumentDetails auxDocDetails;

			int size = assemblyFiles.size();
			for (int i = 0; i < size; i++) {

				// check if thread was interrupted
				if (workerInteractor.isCancelled()) {
					break;
				}

				// current assembly file
				AssemblyTopicId currentAssemblyFile = assemblyFiles.get(i);

				// the URL of assembly file
				String docUrl = currentAssemblyFile.getAbsoluteLocation().toString();

				// report a note at worker
				workerInteractor.reportNote(message + "Parse file: " + docUrl);

				// add found links in toProcessLinks
				try {
					auxDocDetails = linksFinder.gatherLinksAndConditions(parserCreator, profilingInformation, docUrl,
							parentDocumentURL, guiConditions, interactor);

					toProcessLinksFromAssemblyFiles.add(auxDocDetails);

					if (interactor.isReporteUndefinedConditions()) {
						try {
							conditionsChecker.validateAndReport(parentDocumentURL,
									profilingInformation.getProfileConditions(interactor.getDocumentType()),
									auxDocDetails.getAllConditions());

						} catch (Exception e) {
							logger.debug(e.getMessage(), e);
						}
					}

				} catch (SAXException e) {
					status = translator.getTranslation(Tags.FAIL_STATUS);
					problemReporter.reportBrokenLinks(
							new Link(currentAssemblyFile.getName(), "", currentAssemblyFile.getLinkFoundDocumentUrl(), currentAssemblyFile.getLine(),
									currentAssemblyFile.getColumn()),
							e, TabKeyGenerator.generate(parentDocumentURL, currentConditionSetName));

				} catch (ParserConfigurationException e) {
					status = translator.getTranslation(Tags.FAIL_STATUS);
					problemReporter.reportBrokenLinks(
							new Link(currentAssemblyFile.getName(), "", currentAssemblyFile.getLinkFoundDocumentUrl(), currentAssemblyFile.getLine(),
									currentAssemblyFile.getColumn()),
							e, TabKeyGenerator.generate(parentDocumentURL, currentConditionSetName));
				
				} catch (IOException e) {
					status = translator.getTranslation(Tags.FAIL_STATUS);
					problemReporter.reportBrokenLinks(
							new Link(currentAssemblyFile.getName(), "", currentAssemblyFile.getLinkFoundDocumentUrl(), currentAssemblyFile.getLine(),
									currentAssemblyFile.getColumn()),
							e, TabKeyGenerator.generate(parentDocumentURL, currentConditionSetName));
				}

			}
			
			reportDuplicateIds(toProcessLinksFromAssemblyFiles, problemReporter, 
					TabKeyGenerator.generate(parentDocumentURL, currentConditionSetName) );
			
			// check the links.
			checkExternalInternalImage(interactor, toProcessLinksFromAssemblyFiles, problemReporter);
		}
	}

	/**
	 * Check external links.
	 * 
	 * @param toProcessLinks
	 *          Object that contains the links.
	 * @param problemReporter
	 *          Problem reporter.
	 */
	private void checkExternalLinks(DocumentDetails toProcessLinks, ProblemReporter problemReporter) {

		// get external links
		Iterator<Link> iter = toProcessLinks.getExternalLinks().iterator();

		// iterate over external links
		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			// report a note
			workerInteractor.reportNote(message + "Check external link: " + link.getRef());

			if (processedExternalLinks.containsKey(link.getRef())) {
				Exception ex = processedExternalLinks.get(link.getRef());
				if (ex != null) {
					problemReporter.reportBrokenLinks(link, ex,
							TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
				}

			} else {
				try {
					// check the link
					ExternalLinksAndImagesChecker.check(link.getRef());
					processedExternalLinks.put(link.getRef(), null);

				} catch (IOException ex) {
					processedExternalLinks.put(link.getRef(), ex);
					// change the status
					status = translator.getTranslation(Tags.FAIL_STATUS);
					// report if the link in broken
					problemReporter.reportBrokenLinks(link, ex,
							TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
				}
			}

			// check if the thread was interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}
		}

	}

	/**
	 * Check images.
	 * 
	 * @param toProcessLinks
	 *          Object that contains the links.
	 * @param problemReporter
	 *          Problem reporter.
	 */
	private void checkImgLinks(DocumentDetails toProcessLinks, ProblemReporter problemReporter) {

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
				// change the status
				status = translator.getTranslation(Tags.FAIL_STATUS);
				// report if the link is broken
				problemReporter.reportBrokenLinks(link, ex,
						TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
			}

			// check if the thread was interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}

		}

	}

	/**
	 * Get profile conditions sets from GUI according to checkerInteractor.
	 * 
	 * @param interactor
	 *          CheckerInteractor.
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
				guiConditionsSets.putAll(profilingConditionsInformations.getConditionsSets(interactor.getDocumentType()));
				if (guiConditionsSets.isEmpty()) {
					// it's not defined a condition set
					guiConditionsSets.put("", new LinkedHashMap<String, LinkedHashSet<String>>());
				}

			}
		} else {
			// doesn't use profile conditions;
			guiConditionsSets.put("", new LinkedHashMap<String, LinkedHashSet<String>>());
		}

		return guiConditionsSets;
	}

	/**
	 * Report the duplicate Ids form given documentDetails,
	 * @param documentDetails
	 * @param problemReporter
	 * @param tabKey
	 */
	private void reportDuplicateIds(DocumentDetails documentDetails , ProblemReporter problemReporter, String tabKey){
		
		//Iterate over Ids
		int size = documentDetails.getDuplicateParaIds().size();
		for (int i = 0; i < size; i++) {
			Id duplicateId = documentDetails.getDuplicateParaIds().get(i);
			
			//if the id is not filter
			if(!duplicateId.isFilterByConditions()){
				String message = "ID: "+"\"" + duplicateId.getId() + "\"" +" has already been defined.";
				problemReporter.reportDupicateId(duplicateId, message, tabKey);
			}
		}
		
	}

	/**
	 * Change the status variable with the given new string.
	 */
	@Override
	public void changeStatus(String newStatus) {
		status = newStatus;
	}
}
