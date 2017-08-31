package com.oxygenxml.docbook.checker.validator;

import java.util.Iterator;
import java.util.List;

import com.oxygenxml.docbook.checker.ValidationWorkerInteractor;
import com.oxygenxml.docbook.checker.parser.DocumentDetails;
import com.oxygenxml.docbook.checker.parser.Id;
import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.TabKeyGenerator;

/**
 * Check the internal links
 * @author intern4
 *
 */
public class InternalLinksChecker {

	
	private ValidationWorkerInteractor workerInteractor;
	private ProblemReporter problemReporter;

	public InternalLinksChecker( ProblemReporter problemReporter, ValidationWorkerInteractor workerInteractor) {
		this.problemReporter = problemReporter;
		this.workerInteractor = workerInteractor;
	}
	
	/**
	 * Check internal links from toProgressLinks.
	 * @param toProcessLinks
	 * @param message
	 * @param currentConditionSetName
	 * @param progress
	 * @param isFinalCycle
	 */
	public void checkInternalLinks(DocumentDetails toProcessLinks, String message, String currentConditionSetName, float progress, boolean isFinalCycle) {

		// get the IDs
		List<Id> paraIds = toProcessLinks.getParaIds();


		// iterate over the internal links
		Iterator<Link> iter = toProcessLinks.getInternalLinks().iterator();
		while (iter.hasNext()) {

			Link link = (Link) iter.next();

			// report a note
			workerInteractor.reportInProcessElement(message + "Check internal link: " + link.getRef());

			// check if the list with IDs doesn't contain the reference of link.
			Boolean linkPoints = linkPointsToID(paraIds, link);

			if (linkPoints == null) {
				// referred ID isn't in IDs list
				link.setException(new Exception("ID: " + link.getRef() + " not found"));
				problemReporter.reportBrokenLinks(link,  TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
			} else if (false == linkPoints) {
				// referred ID is in a filtered zone
				link.setException(new Exception("Reference to ID " + link.getRef() + " defined in filtered out content."));
				problemReporter.reportBrokenLinks(link,  TabKeyGenerator.generate(link.getDocumentURL(), currentConditionSetName));
			}

			// check if thread was interrupted
			if (workerInteractor.isSetIsCancelled()) {
				break;
			}

			// report a progress
			progress += toProcessLinks.getInternalProgress() * 95;
			workerInteractor.reportProgress((int) progress, isFinalCycle);

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

		// iterates over IDs list
		for (int i = 0; i < listIDs.size(); i++) {

			if (listIDs.get(i).getId().equals(link.getRef())) {
				// was found the referred id
				// check if the id is filter
				if (!listIDs.get(i).isFilterByConditions()) {
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
