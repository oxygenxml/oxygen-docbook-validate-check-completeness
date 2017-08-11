package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.LocatorImpl;

import com.oxygenxml.docbookChecker.CheckerInteractor;

/**
 * Sax event handler.
 * 
 * @author intern4
 *
 */
public class LinkFinderHandler extends DefaultHandler {

	private LinkDetails toReturnLinkDetails = new LinkDetails();

	private Locator locator = new LocatorImpl();

	private Map<String, Set<String>> conditionsFromGui;

	//stack with conditions
	private Stack<Map<String, Set<String>>> conditionsStack = new Stack<Map<String,Set<String>>>();

	private ElementConditionsDetector elementConditionsDetector = null; 
	
	private ElementLinkDetailsDetector elementLinkDetailsDetector ;
	
	
	/**
	 * Constructor
	 * 
	 * @param url
	 *          the parsed url
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public LinkFinderHandler(CheckerInteractor interactor, Map<String, Set<String>> userConditions)
			throws ParserConfigurationException, SAXException, IOException {

		if(interactor.isSelectedCheckUsingProfile()){
			elementConditionsDetector = new ElementConditionsDetector();
		}
	
			elementLinkDetailsDetector = new ElementLinkDetailsDetector(interactor);
		
		
		// conditions for view;
		conditionsFromGui = userConditions;
	}

	
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		//search for conditions in this element
		if(elementConditionsDetector != null){
			elementConditionsDetector.startElement(localName, attributes, conditionsFromGui, conditionsStack);
		}
		//search for linkDetails in this element 
		elementLinkDetailsDetector.startElement(localName, attributes, locator, conditionsStack, toReturnLinkDetails);
		
		
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		if(elementConditionsDetector != null){
			// remove a condition from stack when element is closing.
			elementConditionsDetector.endElement(conditionsStack);
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
