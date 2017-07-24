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

import com.oxygenxml.docbookChecker.Settings;

import ro.sync.exml.editor.id;

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
	public List<Link> check(URL url, Settings settings) {

		LinksFinder linksFinder = new LinksFinder();
		
		List<Link>	brokenLinks = new ArrayList<Link>();

		LinkDetails toProcessLinks;
		
		try {
			toProcessLinks = linksFinder.gatherLinks(url, settings.getCheckExternal());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO print a message
			//documentul trebuie sa fie wellformed
			e.printStackTrace();
			return null;
		}

		// parse xi-include references
		parseIncludedDocumentsLinks(settings.getCheckExternal(), toProcessLinks, linksFinder);

		if(settings.getCheckExternal()){
			// check external links
			checkExternalLinks(toProcessLinks, brokenLinks);
		}
		
		// check images
		checkImgLinks(toProcessLinks, brokenLinks);

		// check internal links
		checkInternalLinks(toProcessLinks, brokenLinks);

		// check xi-include references
		checkIncludedDocumentsLinks(toProcessLinks, brokenLinks);

		return	brokenLinks; 
		}

	
	
	/**
	 * Check external links.
	 */
	private void checkExternalLinks(LinkDetails toProcessLinks, List<Link>	brokenLinks) {

		Iterator<Link> iter = toProcessLinks.getExternalLinks().iterator();

		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			try {
				ContentGetter.openStream(new URL(link.getRef()));
			} catch (IOException e) {
				link.setType("external");
				brokenLinks.add(link);
			}

		}
	}

	
	
	/**
	 * Check images.
	 */
	private void checkImgLinks(LinkDetails toProcessLinks, List<Link>	brokenLinks ) {

		Iterator<Link> iter = toProcessLinks.getImgLinks().iterator();

		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			try {
				ContentGetter.openStream(link.getAbsoluteLocation());
			} catch (IOException e) {
				link.setType("Image");
				brokenLinks.add(link);
			}

		}
	}

	
	
	/**
	 * Check internal link.
	 */
	private void checkInternalLinks(LinkDetails toProcessLinks, List<Link>	brokenLinks) {

		List<Id> paraIds = toProcessLinks.getParaIds();

		Iterator<Link> iter = toProcessLinks.getInternalLinks().iterator();

		while (iter.hasNext()) {
			Link link = (Link) iter.next();
			//check if the list with IDs doesn't contain the reference of link. 
			if(!linkPointsToID(paraIds, link)){
				link.setType("Internal");
				brokenLinks.add(link);
			}
		}
	}

	
	
	/**
	 * Parse included documents. 
	 */
	private void parseIncludedDocumentsLinks(boolean parseExternal, LinkDetails toProcessLinks , LinksFinder linksFinder) {
		List<Link> includedDocumentLinks = toProcessLinks.getIncludedDocumentsLinks();
		Set<Link> visited = new HashSet<Link>();

		for (int i = 0; i < includedDocumentLinks.size(); i++) {
			Link currentLink = includedDocumentLinks.get(i);

			if (!visited.contains(currentLink)) {

				visited.add(currentLink);

				try {
					LinkDetails links = linksFinder.gatherLinks(currentLink.getAbsoluteLocation(), parseExternal);

					//add results in toProcess list
					includedDocumentLinks.addAll(links.getIncludedDocumentsLinks());
					toProcessLinks.getExternalLinks().addAll(links.getExternalLinks());
					toProcessLinks.getImgLinks().addAll(links.getImgLinks());
					toProcessLinks.getInternalLinks().addAll(links.getInternalLinks());
					toProcessLinks.getParaIds().addAll(links.getParaIds());

				} catch (ParserConfigurationException | SAXException | IOException e) {
					//TODO nu e well formed
					System.err.println("*************sax problem**********");
				}
			}
		}
	}

	
	
	/**
	 * Check links that include documents.
	 * 
	 */
	private void checkIncludedDocumentsLinks(LinkDetails toProcessLinks, List<Link>	brokenLinks) {
		List<Link> includedDocumentLinks = toProcessLinks.getIncludedDocumentsLinks();

		for (int i = 0; i < includedDocumentLinks.size(); i++) {
			Link currentLink = includedDocumentLinks.get(i);
			URL currentUrl = currentLink.getAbsoluteLocation();
			if (isParentForBrokenLinks(currentUrl, brokenLinks)) {
				currentLink.setType("Document");
				brokenLinks.add(currentLink);
			}
		}
	}

	private boolean isParentForBrokenLinks(URL url, List<Link>	brokenLinks) {

		for (int i = 0; i < brokenLinks.size(); i++) {
			if (brokenLinks.get(i).getDocumentURL().equals(url) && !brokenLinks.get(i).getType().equals("Document") ) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Check if the reference of link is in the IDs list.
	 * @param list the list with ID's
	 * @param link	the reference
	 * @return <code>true</code>if the list of IDs contains the reference of link.  
	 */
	private boolean linkPointsToID(List<Id> list, Link link){
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).getId().equals(link.getRef()) ){
				return true;
			}
		}
		return false;
	}
}
