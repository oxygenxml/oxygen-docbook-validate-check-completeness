package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;
import com.oxygenxml.docbookChecker.CheckerInteractor;

/**
 * Checker for external links
 * 
 * @author intern4
 *
 */
public class LinksCheckerImp implements LinksChecker {

	/**
	 * Check links.
	 */
	@Override
	public void check(ParserCreator parserCreator, String url, CheckerInteractor interactor,
			ProblemReporter problemReporter, StatusReporter statusReporter) {
		try {
			statusReporter.reportStatus(StatusReporter.progress);

			LinksFinder linksFinder = new LinksFinderImpl();

			LinkDetails toProcessLinks = null;
			toProcessLinks = linksFinder.gatherLinks(parserCreator, url, interactor);
			
			if (interactor.isSelectedCheckExternal()) {
				// check external links
				checkExternalLinks(toProcessLinks, problemReporter);
			}

			if (interactor.isSelectedCheckImages()) {
				// check images
				checkImgLinks(toProcessLinks, problemReporter);
			}

			if (interactor.isSelectedCheckInternal()) {
				// check internal links
				checkInternalLinks(interactor, toProcessLinks, problemReporter);
			}

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
	private void checkExternalLinks(LinkDetails toProcessLinks, ProblemReporter problemReporter) {

		Iterator<Link> iter = toProcessLinks.getExternalLinks().iterator();

		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			try {
				// TODO httpConnection(get HEAD)
				ContentGetter.openStream(new URL(link.getRef()));
			} catch (IOException e) {
				link.setType(LinkType.EXTERNAL);
				link.setException(e);
				problemReporter.reportBrokenLinks(link);
			}

		}
	}

	/**
	 * Check images.
	 */
	private void checkImgLinks(LinkDetails toProcessLinks, ProblemReporter problemReporter) {

		Iterator<Link> iter = toProcessLinks.getImgLinks().iterator();

		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			try {
				ContentGetter.openStream(link.getAbsoluteLocation());
			} catch (IOException e) {
				link.setType(LinkType.IMAGE);
				link.setException(e);
				problemReporter.reportBrokenLinks(link);
			}

		}
	}

	/**
	 * Check internal link.
	 */
	private void checkInternalLinks(CheckerInteractor interactor, LinkDetails toProcessLinks,
			ProblemReporter problemReporter) {
		
		List<Id> paraIds = toProcessLinks.getParaIds();

		//conditions from GUI
		Map<String, Set<String>> conditionsTableGUI = interactor.getConditionsTableRows();
		
		Iterator<Link> iter = toProcessLinks.getInternalLinks().iterator();
		while (iter.hasNext()) {
			Link link = (Link) iter.next();
			

			//test if link is filter
			if (!link.isFilter(conditionsTableGUI) ) {

				// check if the list with IDs doesn't contain the reference of link.
				Boolean linkPoints = linkPointsToID(paraIds, link, conditionsTableGUI);
				
				if (linkPoints == null) {
					link.setType(LinkType.INTERNAL);
					link.setException(new Exception("ID: " + link.getRef() + " not found"));
					problemReporter.reportBrokenLinks(link);
				} else if (false == linkPoints) {
					link.setType(LinkType.INTERNAL);
					link.setException(new Exception("Reference to ID " + link.getRef() + " defined in filtered out content."));
					problemReporter.reportBrokenLinks(link);
				}

			}
		}
	}

	/**
	 * Check if the link refers at a valid id from the IDs list.
	 * 
	 * @param list
	 *          the list with ID's
	 * @param link
	 *          the reference
	 * @return <code>true</code> if the link refers at a valid id ,
	 *         <code>false</code> if refers at a filtered id <code>null</code>> if wasn't found the referred id in IDs list
	 */
	private Boolean linkPointsToID(List<Id> list, Link link, Map<String, Set<String>> conditionsTableGUI) {
		Boolean toReturn = null;
		
		//iterates over IDs list 
		for (int i = 0; i < list.size(); i++) {
			
			if (list.get(i).getId().equals(link.getRef())) {
				//was found the referred id
				//check if the id is filter
				if (!list.get(i).isFilter(conditionsTableGUI) ) {
					return true;
				} else {
					toReturn = false;
				}
			}
		}
		return toReturn;
	}

	
}
