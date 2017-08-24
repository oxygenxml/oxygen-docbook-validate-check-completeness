package com.oxygenxml.docbook.checker.parser;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.LocatorImpl;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.profiling.AllConditionsDetector;
import com.oxygenxml.profiling.ProfilingConditionsInformations;

/**
 * SAX event handler.
 * 
 * @author intern4
 *
 */
public class LinkFinderHandler extends DefaultHandler {

	private LinkDetails toReturnLinkDetails = new LinkDetails();

	private Locator locator = new LocatorImpl();


	private ElementFilterDetector elementFilterDetector = null; 
	
	private AllConditionsDetector conditionsDetector = null;
	
	private ElementLinkDetailsDetector elementLinkDetailsDetector ;
	
	
	public LinkFinderHandler(CheckerInteractor interactor,ProfilingConditionsInformations profilingInformation ,LinkedHashMap<String, LinkedHashSet<String>> userConditions)
			throws ParserConfigurationException, SAXException, IOException {

		if(interactor.isUsingProfile()){
			elementFilterDetector = new ElementFilterDetector(userConditions);
		}
		
		if(interactor.isSelectedReporteUndefinedConditions()){
			conditionsDetector = new AllConditionsDetector( profilingInformation);
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
		
		//get the state of this element(filter or not)
		if(elementFilterDetector != null){
			isFilter = elementFilterDetector.startElement(localName, attributes);
		}
		
		//get the conditions from this element
		if(conditionsDetector != null){
			conditionsDetector.startElement(localName, attributes);
		}
		
		//search for linkDetails in this element 
		elementLinkDetailsDetector.startElement(localName, attributes, locator, isFilter, toReturnLinkDetails);
		
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		if(elementFilterDetector != null){
			elementFilterDetector.endElement();
		}
	}

	
	/**
	 * Get founded results.
	 * 
	 * @return results
	 */
	public LinkDetails getResults() {
		if(conditionsDetector != null){
			toReturnLinkDetails.setAllConditions(conditionsDetector.getAllConditionFromDocument());
		}
		return toReturnLinkDetails;
	}

}
