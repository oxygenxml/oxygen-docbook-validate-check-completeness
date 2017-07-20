package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
	
	private Links toProcessLinks = null;

	private Set<Link> brokenExternalLinks ;
	
	private Set<Link> brokenImgLinks;
	
	private Set<Link> brokenInternalLinks;
	
	/**
	 * Check external links.
	 */
	@Override
	public Links check(URL url) {
		
		 brokenExternalLinks = new LinkedHashSet<>();
			
			 brokenImgLinks = new LinkedHashSet<>();
			
			 brokenInternalLinks = new LinkedHashSet<>();
			

		LinksFinder linksFinder = new LinksFinder();

		try {
			toProcessLinks = linksFinder.gatherLinks(url);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		checkExternalLinks();
		checkImgLinks();


		return new Links(brokenExternalLinks, brokenImgLinks, brokenInternalLinks);
	}
	
	
	
	private void checkExternalLinks(){
		XmlContentGetter contentGetter = new XmlContentGetter();

		Iterator<Link> iter = toProcessLinks.getExternalLinks().iterator();
		
		while (iter.hasNext()) {
			Link link = (Link) iter.next();
			
			try {
				contentGetter.getXml(new URL(link.getRef()));
			} catch (IOException e) {
				brokenExternalLinks.add(link);
			}
			
		}
	}
	
	private void checkImgLinks(){
		XmlContentGetter contentGetter = new XmlContentGetter();

		Iterator<Link> iter = toProcessLinks.getImgLinks().iterator();
		
		while (iter.hasNext()) {
			Link link = (Link) iter.next();
			
			try {
				System.out.println(link.getAbsoluteLocation().toString());
				contentGetter.getXml(link.getAbsoluteLocation());
			} catch (IOException e) {
				System.out.println(link);
				brokenImgLinks.add(link);
			}
			
		}
	}
}
