package com.oxygenxml.docbook.checker.validator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.docbook.checker.ValidationWorkerInteractor;
import com.oxygenxml.docbook.checker.parser.AssemblyFileId;
import com.oxygenxml.docbook.checker.parser.DocumentDetails;
import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.parser.ParserCreator;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.StatusReporter;
import com.oxygenxml.docbook.checker.reporters.TabKeyGenerator;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;
import com.oxygenxml.profiling.ProfilingConditionsInformations;

public class AssemblyLinksChecker {
	/**
	 * Interactor for validations worker.
	 */
	private ValidationWorkerInteractor workerInteractor;

	/**
	 * Reporter for problem
	 */
	private ProblemReporter problemReporter;

	/**
	 * Translator
	 */
	private Translator translator;

	/**
	 * Document checker.
	 */
	private DocumentCheckerImp documentChecker;

	/**
	 * GUI conditions.
	 */
	private LinkedHashMap<String, LinkedHashSet<String>> guiConditions;

	/**
	 * Parser creator
	 */
	private ParserCreator parserCreator;

	/**
	 * Gather for defined profiling conditions
	 */
	private ProfilingConditionsInformations profilingInformation;

	/**
	 * Checker interactor.
	 */
	private CheckerInteractor interactor;

	/**
	 * Status reporter.
	 */
	private StatusReporter statusReporter;

	/**
	 * Constructor.
	 * 
	 * @param problemReporter
	 *          Problem reporter.
	 * @param workerInteractor
	 *          Validation worker interactor.
	 * @param translator
	 *          Translator
	 */
	public AssemblyLinksChecker(DocumentCheckerImp documentChecker,
			LinkedHashMap<String, LinkedHashSet<String>> guiConditions, ParserCreator parserCreator,
			ProfilingConditionsInformations profilingInformation, CheckerInteractor interactor,
			ProblemReporter problemReporter, StatusReporter statusReporter, ValidationWorkerInteractor workerInteractor,
			Translator translator) {
		this.documentChecker = documentChecker;
		this.guiConditions = guiConditions;
		this.parserCreator = parserCreator;
		this.profilingInformation = profilingInformation;
		this.interactor = interactor;
		this.problemReporter = problemReporter;
		this.statusReporter = statusReporter;
		this.workerInteractor = workerInteractor;
		this.translator = translator;
	}

	/**
	 * Check internal links from toProgressLinks.
	 * 
	 * @param documentDetails
	 *          DocumentDetails, that contains the links to be process
	 * @param message
	 *          part of the message to be reported
	 * @param currentConditionSetName
	 *          Name of current condition set
	 * @param progress
	 *          The current progress
	 * @param isFinalCycle
	 *          Is final cycle of progress.
	 * @param status
	 *          The status of process
	 */
	public void checkAssemblyLinks(DocumentDetails documentDetails,
			String message, String currentConditionSetName, float progress, boolean isFinalCycle, String status) {

		// get the IDs
		List<AssemblyFileId> assemblyFiles = documentDetails.getAssemblyFiles();

		// Map with processed IDs
		Set<String> processedAssemblyIds = new HashSet<String>();

		// iterate over the assemblyLinks links
		Iterator<Link> iter = documentDetails.getAssemblyLinks().iterator();
		while (iter.hasNext()) {

			Link link = (Link) iter.next();

			// report a note
			workerInteractor.reportNote(message + "Check assembly link: " + link.getRef());

			// check if the list with IDs contain the reference of link.
			Boolean linkPoints = linkPointsToID(assemblyFiles, link);

			if (linkPoints == null) {
				System.out.println("de negasit");
				// referred ID isn't in IDs list
				Exception ex = new Exception("Resource ID : " + link.getRef() + " wasn't found");

				// change the status
				status = translator.getTranslation(Tags.FAIL_STATUS);

				// report the problem
				problemReporter.reportBrokenLinks(link, ex,
						TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
			} else if (false == linkPoints) {
				System.out.println("filtrat");
				// referred ID is in a filtered zone
				Exception ex = new Exception("Reference to resource ID " + link.getRef() + " defined in filtered out content.");

				// change the status
				status = translator.getTranslation(Tags.FAIL_STATUS);

				// report the problem
				problemReporter.reportBrokenLinks(link, ex,
						TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
			}

			else if (true == linkPoints) {
				// check if assembly file wasn't checked.
				if (!processedAssemblyIds.contains(link.getRef())) {

					// create the URL of the assembly file
					List<String> listUrl = new ArrayList<String>();

					listUrl.add(getTheAssemblyFileList(assemblyFiles, link));

					// check the assembly file
					documentChecker.checkUsingConditionsSet(guiConditions, message, parserCreator, listUrl, profilingInformation,
							interactor, problemReporter, statusReporter);

					processedAssemblyIds.add(link.getRef());
				}

			}

			// check if thread was interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}

		}
	}

	/**
	 * Check if the link refers at a valid id from the assemblyFileIds list.
	 * 
	 * @param listIDs
	 *          the list with ID's
	 * @param link
	 *          the reference(link)
	 * @return <code>true</code> if the link refers at a valid id ,
	 *         <code>false</code> if refers at a filtered id <code>null</code>> if
	 *         wasn't found the referred id in IDs list
	 */
	private Boolean linkPointsToID(List<AssemblyFileId> assemblyFilesIds, Link link) {
		Boolean toReturn = null;

		int size = assemblyFilesIds.size();
		// iterates over IDs list
		for (int i = 0; i < size; i++) {

			if (assemblyFilesIds.get(i).getId().equals(link.getRef())) {
				// was found the referred id
				// check if the id is filter
				if (!assemblyFilesIds.get(i).isFilterByConditions()) {
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
	 * Get the path of assembly file according to the givel link reference.
	 * @param assemblyFiles	The assembly files.
	 * @param link The link.
	 * @return the path of assembly file
	 */
	private String getTheAssemblyFileList(List<AssemblyFileId> assemblyFiles, Link link) {
		String toReturn = "";

		int size = assemblyFiles.size();
		// iterates over IDs list
		for (int i = 0; i < size; i++) {
			if (link.getRef().equals(assemblyFiles.get(i).getId())) {
				// create the URL of the assembly file
				toReturn = new File(link.getDocumentURL()).getParent().toString() + File.separator
						+ assemblyFiles.get(i).getName();
				break;
			}
		}

		return toReturn;
	}
}
