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

/**
 * Finder for assembly files(topic files).
 * @author intern4
 *
 */
public class AssemblyFilesFinder {
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
	 * Constructor.
	 * 
	 * @param problemReporter
	 *          Problem reporter.
	 * @param workerInteractor
	 *          Validation worker interactor.
	 * @param translator
	 *          Translator
	 */
	public AssemblyFilesFinder(ProblemReporter problemReporter, ValidationWorkerInteractor workerInteractor,
			Translator translator) {
		this.problemReporter = problemReporter;
		this.workerInteractor = workerInteractor;
		this.translator = translator;
	}

	/**
	 * Find the valid assembly files and report invalid files.
	 * 
	 * @param documentDetails
	 *          DocumentDetails, that contains the links to be process
	 * @param message
	 *          part of the message to be reported
	 * @param currentConditionSetName
	 *          Name of current condition set
	 * @param status
	 *          The status of process
	 * @return A set with found valid assembly files.
	 */
	public List<String> findValidFiles(DocumentDetails documentDetails,
			String message, String currentConditionSetName, String status) {

		List<String> toReturn = new ArrayList<String>();
		
		// get the IDs
		List<AssemblyFileId> assemblyFilesAndIds = documentDetails.getAssemblyFilesAndIds();

		// iterate over the assemblyLinks links
		Iterator<Link> iter = documentDetails.getAssemblyLinks().iterator();
		while (iter.hasNext()) {

			Link link = (Link) iter.next();

			// report a note
			workerInteractor.reportNote(message + "Check assembly link: " + link.getRef());

			// check if the list with IDs contain the reference of link.
			Boolean linkPoints = linkPointsToID(assemblyFilesAndIds, link);

			if (linkPoints == null) {
				// referred ID isn't in IDs list
				Exception ex = new Exception("Resource ID : " + link.getRef() + " wasn't found");

				// change the status
				status = translator.getTranslation(Tags.FAIL_STATUS);

				// report the problem
				problemReporter.reportBrokenLinks(link, ex,
						TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
			} 
			else if (false == linkPoints) {
				// referred ID is in a filtered zone
				Exception ex = new Exception("Reference to resource ID " + link.getRef() + " defined in filtered out content.");

				// change the status
				status = translator.getTranslation(Tags.FAIL_STATUS);

				// report the problem
				problemReporter.reportBrokenLinks(link, ex,
						TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
			}	
			else if (true == linkPoints) {
				toReturn.add(getTheAssemblyFile(assemblyFilesAndIds, link));
				
			}

			// check if thread was interrupted
			if (workerInteractor.isCancelled()) {
				return new ArrayList<String>();
			}

		}
		
		return toReturn;
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
	 * Get the path of assembly file according to the given link reference.
	 * @param assemblyFiles	The assembly files.
	 * @param link The link.
	 * @return the path of assembly file
	 */
	private String getTheAssemblyFile(List<AssemblyFileId> assemblyFiles, Link link) {
		String toReturn = "";

		int size = assemblyFiles.size();
		// iterates over IDs list
		for (int i = 0; i < size; i++) {
			if (link.getRef().equals(assemblyFiles.get(i).getId())) {
				// create the URL of the assembly file
				toReturn = new File(link.getLinkFoundDocumentUrl()).getParent().toString() + File.separator
						+ assemblyFiles.get(i).getName();
				break;
			}
		}

		return toReturn;
	}
}
