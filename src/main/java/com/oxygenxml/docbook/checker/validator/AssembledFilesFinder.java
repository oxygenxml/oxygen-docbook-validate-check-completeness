package com.oxygenxml.docbook.checker.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.oxygenxml.docbook.checker.ValidationWorkerInteractor;
import com.oxygenxml.docbook.checker.parser.AssemblyTopicId;
import com.oxygenxml.docbook.checker.parser.DocumentDetails;
import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.TabKeyGenerator;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

/**
 * Finder for assembled files(topic files).
 * @author intern4
 *
 */
public class AssembledFilesFinder {
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
	public AssembledFilesFinder(ProblemReporter problemReporter, ValidationWorkerInteractor workerInteractor,
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
	 * @param statusChanger
	 *          Changer for status.
	 * @return A set with found valid assembly files.
	 */
	public List<AssemblyTopicId> findValidTopicsAndValidate(DocumentDetails documentDetails,
			String message, String currentConditionSetName, StatusChanger statusChanger) {

		//list with valid topics to return.
		List<AssemblyTopicId> toReturn = new ArrayList<AssemblyTopicId>();
		// get the IDs
		List<AssemblyTopicId> assemblyFilesAndIds = documentDetails.getAssemblyFilesAndIds();

		
		// iterate over the assemblyLinks links
		Iterator<Link> iter = documentDetails.getAssemblyLinks().iterator();
		while (iter.hasNext()) {
			Link link = iter.next();
			
			// report a note
			workerInteractor.reportNote(message + "Check assembly link: " + link.getRef());

			// check if the list with IDs contain the reference of link.
			Boolean linkPoints = linkPointsToID(assemblyFilesAndIds, link);

			if (linkPoints == null) {
				// referred ID isn't in IDs list
				Exception ex = new Exception("Resource ID : " + link.getRef() + " wasn't found");

				// change the status
				statusChanger.changeStatus(translator.getTranslation(Tags.FAIL_STATUS)); 

				// report the problem
				problemReporter.reportBrokenLinks(link, ex,
						TabKeyGenerator.generate(link.getStartDocumentURL(), currentConditionSetName));
			} 
			else if (!linkPoints) {
				// referred ID is in a filtered zone
				Exception ex = new Exception("Reference to resource ID " + link.getRef() + " defined in filtered out content.");

				// change the status
				statusChanger.changeStatus(translator.getTranslation(Tags.FAIL_STATUS)); 

				// report the problem
				problemReporter.reportBrokenLinks(link, ex,
						TabKeyGenerator.generate(link.getStartDocumentURL(), currentConditionSetName));
			}	
			else if (linkPoints) {
				toReturn.add(getTheAssemblyFile(assemblyFilesAndIds, link));
				
			}

			// check if thread was interrupted
			if (workerInteractor.isCancelled()) {
				return new ArrayList<AssemblyTopicId>();
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
	private Boolean linkPointsToID(List<AssemblyTopicId> assemblyFilesIds, Link link) {
		Boolean toReturn = null;
		AssemblyTopicId  currentAssemblyTopic; 
		
		int size = assemblyFilesIds.size();
		// iterates over IDs list
		for (int i = 0; i < size; i++) {
			currentAssemblyTopic = assemblyFilesIds.get(i);
			if (currentAssemblyTopic.getId().equals(link.getRef())) {
				// was found the referred id
				// check if the id is filter
				if (!currentAssemblyTopic.isFilterByConditions()) {
					// was found a valid id
					toReturn = true;
					break;
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
	private AssemblyTopicId getTheAssemblyFile(List<AssemblyTopicId> assemblyFiles, Link link) {
		AssemblyTopicId  currentAssemblyTopic; 
		
		int size = assemblyFiles.size();
		// iterates over IDs list
		for (int i = 0; i < size; i++) {
			currentAssemblyTopic = assemblyFiles.get(i);
			if (link.getRef().equals(currentAssemblyTopic.getId())) {
				// create the URL of the assembly file
				return currentAssemblyTopic;
			}
		}

		return null;
	}
}
