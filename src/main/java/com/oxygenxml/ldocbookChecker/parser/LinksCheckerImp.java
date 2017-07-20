package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Checker for external links
 * 
 * @author intern4
 *
 */
public class LinksCheckerImp implements LinksChecker {

	/**
	 * Links and IDs for process.
	 */
	private Results toProcessLinks = null;

	/**
	 * Broken external links founded.
	 */
	private List<Link> brokenExternalLinks;

	/**
	 * Broken images links founded.
	 */
	private List<Link> brokenImgLinks;

	/**
	 * Broken internal links founded.
	 */
	private List<Link> brokenInternalLinks;

	/**
	 * Broken links that includes documents.
	 */
	private List<Link> brokenIncludedDocumentsLink;

	/**
	 * Finder for links and IDs.
	 */
	private LinksFinder linksFinder = new LinksFinder();

	
	
	/**
	 * Check links.
	 */
	@Override
	public Results check(URL url) {

		brokenExternalLinks = new ArrayList<Link>();

		brokenImgLinks = new ArrayList<Link>();

		brokenInternalLinks = new ArrayList<Link>();

		brokenIncludedDocumentsLink = new ArrayList<Link>();

		try {
			toProcessLinks = linksFinder.gatherLinks(url);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO print a message
			//documentul trebuie sa fie wellformed
			e.printStackTrace();
			return null;
		}

		// parse xi-include references
		parseIncludedDocumentsLinks();

		// check external links
		checkExternalLinks();

		// check images
		checkImgLinks();

		// check internal links
		checkInternalLinks();

		// check xi-include references
		checkIncludedDocumentsLinks();

		return new Results(brokenIncludedDocumentsLink, brokenExternalLinks, brokenImgLinks, toProcessLinks.getParaIds(),
				brokenInternalLinks);
	}

	
	
	/**
	 * Check external links.
	 */
	private void checkExternalLinks() {

		Iterator<Link> iter = toProcessLinks.getExternalLinks().iterator();

		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			try {
				ContentGetter.openStream(new URL(link.getRef()));
			} catch (IOException e) {
				System.out.println(link.toString());
				brokenExternalLinks.add(link);
			}

		}
	}

	
	
	/**
	 * Check images.
	 */
	private void checkImgLinks() {

		Iterator<Link> iter = toProcessLinks.getImgLinks().iterator();

		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			try {
				System.out.println(link.getAbsoluteLocation().toString());
				ContentGetter.openStream(link.getAbsoluteLocation());
			} catch (IOException e) {
				System.out.println(link);
				brokenImgLinks.add(link);
			}

		}
	}

	
	
	/**
	 * Check internal link.
	 */
	private void checkInternalLinks() {

		List<Id> paraIds = toProcessLinks.getParaIds();

		Iterator<Link> iter = toProcessLinks.getInternalLinks().iterator();

		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			if (!paraIds.contains(link)) {
				brokenInternalLinks.add(link);
			}
		}
	}

	
	
	/**
	 * Parse included documents. 
	 */
	private void parseIncludedDocumentsLinks() {
		List<Link> includedDocumentLinks = toProcessLinks.getIncludedDocumentsLinks();
		Set<Link> visited = new HashSet<Link>();

		for (int i = 0; i < includedDocumentLinks.size(); i++) {
			Link currentLink = includedDocumentLinks.get(i);

			if (!visited.contains(currentLink)) {

				visited.add(currentLink);

				try {
					Results links = linksFinder.gatherLinks(currentLink.getAbsoluteLocation());

					//add results in toProcess list
					includedDocumentLinks.addAll(links.getIncludedDocumentsLinks());
					toProcessLinks.getExternalLinks().addAll(links.getExternalLinks());
					toProcessLinks.getImgLinks().addAll(links.getImgLinks());
					toProcessLinks.getInternalLinks().addAll(links.getInternalLinks());
					toProcessLinks.getParaIds().addAll(links.getParaIds());

				} catch (ParserConfigurationException | SAXException | IOException e) {
					System.err.println("*************sax problem**********");
				}
			}
		}
	}

	
	
	/**
	 * Check links that include documents.
	 * 
	 */
	private void checkIncludedDocumentsLinks() {
		List<Link> includedDocumentLinks = toProcessLinks.getIncludedDocumentsLinks();

		for (int i = 0; i < includedDocumentLinks.size(); i++) {
			Link currentLink = includedDocumentLinks.get(i);
			URL currentUrl = currentLink.getAbsoluteLocation();
			if (isParentForBrokenLinks(currentUrl)) {
				brokenIncludedDocumentsLink.add(currentLink);
			}
		}
	}

	private boolean isParentForBrokenLinks(URL url) {

		for (int i = 0; i < brokenExternalLinks.size(); i++) {
			if (brokenExternalLinks.get(i).getDocumentURL().equals(url)) {
				return true;
			}
		}

		for (int i = 0; i < brokenImgLinks.size(); i++) {
			if (brokenImgLinks.get(i).getDocumentURL().equals(url)) {
				return true;
			}
		}

		for (int i = 0; i < brokenInternalLinks.size(); i++) {
			if (brokenInternalLinks.get(i).getDocumentURL().equals(url)) {
				return true;
			}
		}

		return false;
	}
}
