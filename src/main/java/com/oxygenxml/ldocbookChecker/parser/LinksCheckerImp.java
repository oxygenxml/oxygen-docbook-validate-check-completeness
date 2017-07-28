package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;

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
	public void check(ParserCreator parserCreator, String url, CheckerInteractor interactor, ProblemReporter problemReporter, StatusReporter statusReporter) {
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
				checkInternalLinks(toProcessLinks, problemReporter);
			}
			
			statusReporter.reportStatus(StatusReporter.succes);
		} 
		catch (SAXException e) {
			System.err.println("catch");
			problemReporter.reportException(e);
			statusReporter.reportStatus(StatusReporter.fail);
		}
		catch (ParserConfigurationException e) {
			System.err.println("catch");

			statusReporter.reportStatus(StatusReporter.fail);
			problemReporter.reportException(e);
		} 
		catch (IOException e) {
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
	private void checkInternalLinks(LinkDetails toProcessLinks, ProblemReporter problemReporter) {

		List<Id> paraIds = toProcessLinks.getParaIds();

		Iterator<Link> iter = toProcessLinks.getInternalLinks().iterator();

		while (iter.hasNext()) {
			Link link = (Link) iter.next();
			// check if the list with IDs doesn't contain the reference of link.
			if (!linkPointsToID(paraIds, link)) {
				link.setType(LinkType.INTERNAL);
				link.setException(new Exception("ID: " + link.getRef() + " not found"));
				problemReporter.reportBrokenLinks(link);
			}
		}
	}

	/**
	 * Check if the reference of link is in the IDs list.
	 * 
	 * @param list
	 *          the list with ID's
	 * @param link
	 *          the reference
	 * @return <code>true</code>if the list of IDs contains the reference of link.
	 */
	private boolean linkPointsToID(List<Id> list, Link link) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId().equals(link.getRef())) {
				return true;
			}
		}
		return false;
	}
}
