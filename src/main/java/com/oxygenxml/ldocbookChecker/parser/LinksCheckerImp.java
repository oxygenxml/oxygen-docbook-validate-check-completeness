package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbookChecker.Settings;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;


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
	public void check(ParserCreator parserCreator, URL url, Settings settings, ProblemReporter problemReporter) {
		try {
			LinksFinder linksFinder = new LinksFinderImpl();
			
			LinkDetails toProcessLinks = null;
			toProcessLinks = linksFinder.gatherLinks(parserCreator, url, settings.isCheckExternal());
			

			if(settings.isCheckExternal()){
				// check external links
				checkExternalLinks(toProcessLinks, problemReporter);
			}

			// check images
			checkImgLinks(toProcessLinks, problemReporter);

			// check internal links
			checkInternalLinks(toProcessLinks, problemReporter);

			
		} catch (SAXException e) {
			System.err.println("INITIAL " + e);
			e.printStackTrace();
			System.err.println("EMBEDDED " + e.getException());
			if(e.getException() != null){
				e.getException().printStackTrace();
			}

		} catch (ParserConfigurationException e) {
			// TODO print a message
			//documentul trebuie sa fie wellformed
			e.printStackTrace();
		} catch (IOException e) {
			
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
			//check if the list with IDs doesn't contain the reference of link. 
			if(!linkPointsToID(paraIds, link)){
				link.setType(LinkType.INTERNAL);
				problemReporter.reportBrokenLinks(link);
			}
		}
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
