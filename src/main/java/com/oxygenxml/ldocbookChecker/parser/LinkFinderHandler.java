package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.LocatorImpl;

import com.oxygenxml.docbookChecker.CheckerInteractor;

/**
 * SAX event handler.
 * 
 * @author intern4
 *
 */
public class LinkFinderHandler extends DefaultHandler {

	private LinkDetails toReturnLinkDetails = new LinkDetails();

	private Locator locator = new LocatorImpl();


	private ElementConditionsDetector elementConditionsDetector = null; 
	
	private ElementLinkDetailsDetector elementLinkDetailsDetector ;
	
	
	
	public LinkFinderHandler(CheckerInteractor interactor, LinkedHashMap<String, LinkedHashSet<String>> userConditions)
			throws ParserConfigurationException, SAXException, IOException {

		if(interactor.isUsingProfile()){
			elementConditionsDetector = new ElementConditionsDetector(userConditions);
		}
	
			elementLinkDetailsDetector = new ElementLinkDetailsDetector(interactor);
		
		
	}

	
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
	
		boolean isFilter = false;
		
		//search for conditions in this element
		if(elementConditionsDetector != null){
			isFilter = elementConditionsDetector.startElement(localName, attributes);
		}
		//search for linkDetails in this element 
		elementLinkDetailsDetector.startElement(localName, attributes, locator, isFilter, toReturnLinkDetails);
		
		
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		if(elementConditionsDetector != null){
			elementConditionsDetector.endElement();
		}
	}

	
	/**
	 * Get founded results.
	 * 
	 * @return results
	 */
	public LinkDetails getResults() {
		return toReturnLinkDetails;
	}

	
}
