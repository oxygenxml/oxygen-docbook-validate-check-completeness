package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
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

	private Results toProcessLinks = null;

	private List<Link> brokenExternalLinks;

	private List<Link> brokenImgLinks;

	private List<Link> brokenInternalLinks;

	/**
	 * Check links.
	 */
	@Override
	public Results check(URL url) {

		brokenExternalLinks = new ArrayList<Link>();

		brokenImgLinks = new ArrayList<Link>();

		brokenInternalLinks = new ArrayList<Link>();

		LinksFinder linksFinder = new LinksFinder();

		try {
			toProcessLinks = linksFinder.gatherLinks(url);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO print a message
			e.printStackTrace();
			return null;
		}
		
		//check external links
		checkExternalLinks();
		
		//check images
		checkImgLinks();
		
		//check internal links
		checkInternalLinks();
		
		return new Results(brokenExternalLinks, brokenImgLinks, toProcessLinks.getParaIds(), brokenInternalLinks);
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
	private void checkInternalLinks(){
		
		List<Id> paraIds = toProcessLinks.getParaIds();
		
		Iterator<Link> iter = toProcessLinks.getInternalLinks().iterator();
		
		while(iter.hasNext()){
			Link link = (Link) iter.next();
		
			if(!paraIds.contains(link)){
				brokenInternalLinks.add(link);
			}
		}
	}
}
