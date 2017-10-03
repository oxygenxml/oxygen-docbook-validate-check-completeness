package com.oxygenxml.docbook.checker.validator;

import java.util.Iterator;
import java.util.List;

import com.oxygenxml.docbook.checker.ValidationWorkerInteractor;
import com.oxygenxml.docbook.checker.parser.DocumentDetails;
import com.oxygenxml.docbook.checker.parser.Id;
import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.TabKeyGenerator;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

/**
 * Check the internal links
 * @author intern4
 *
 */
public class InternalLinksChecker {

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
	 * @param problemReporter Problem reporter.
	 * @param workerInteractor Validation worker interactor.
	 * @param translator Translator
	 */
	public InternalLinksChecker( ProblemReporter problemReporter, ValidationWorkerInteractor workerInteractor, Translator translator) {
		this.problemReporter = problemReporter;
		this.workerInteractor = workerInteractor;
		this.translator = translator;
	}
	
	/**
	 * Check internal links from toProgressLinks.
	 * @param toProcessLinks DocumentDetails, that contains the links to be process
	 * @param message part of the message to be reported
	 * @param currentConditionSetName Name of current  condition set 
	 * @param status The status of process
	 */
	public void checkInternalLinks(DocumentDetails toProcessLinks, String message, String currentConditionSetName, String status) {

		// get the IDs
		List<Id> paraIds = toProcessLinks.getValidParaIds();


		// iterate over the internal links
		Iterator<Link> iter = toProcessLinks.getInternalLinks().iterator();
		while (iter.hasNext()) {

			Link link = (Link) iter.next();

			// report a note
			workerInteractor.reportNote(message + "Check internal link: " + link.getRef());

			// check if the list with IDs doesn't contain the reference of link.
			Boolean linkPoints = linkPointsToID(paraIds, link);

			if (linkPoints == null) {
				// referred ID isn't in IDs list
				Exception ex = new Exception("ID: " + link.getRef() + " wasn't found");
				
				//change the status
				status = translator.getTranslation(Tags.FAIL_STATUS);
				
				//report the problem
				problemReporter.reportBrokenLinks(link, ex ,   TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
			} else if (false == linkPoints) {
				// referred ID is in a filtered zone
				Exception ex = new Exception("Reference to ID " + link.getRef() + " defined in filtered out content.");
			
				//change the status
				status = translator.getTranslation(Tags.FAIL_STATUS);
				
				//report the problem
				problemReporter.reportBrokenLinks(link, ex ,TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
			}

			// check if thread was interrupted
			if (workerInteractor.isCancelled()) {
				break;
			}


		}
	}

	/**
	 * Check if the link refers at a valid id from the IDs list.
	 * 
	 * @param listIDs
	 *          the list with ID's
	 * @param link
	 *          the reference(link)
	 * @return <code>true</code> if the link refers at a valid id ,
	 *         <code>false</code> if refers at a filtered id <code>null</code>> if
	 *         wasn't found the referred id in IDs list
	 */
	private Boolean linkPointsToID(List<Id> listIDs, Link link) {
		Boolean toReturn = null;

		// iterates over IDs set
		Iterator<Id> idIter = listIDs.iterator();
		while (idIter.hasNext()) {
			Id id = (Id) idIter.next();
			
			if (id.getId().equals(link.getRef())) {
				// was found the referred id
				// check if the id is filter
				if (!id.isFilterByConditions()) {
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
}
