package com.oxygenxml.profiling;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * Handler for find profile conditions from a document.
 * @author intern4
 *
 */
public class ProfileDocsFinderHandler extends DefaultHandler {
	
	/**
	 * Detector for conditions
	 */
	private AllConditionsDetector conditionsDetector;
	
	/**
	 * Constructor
	 * @param definedAttributesNames Set with defined conditions attributes names.
	 */
	public ProfileDocsFinderHandler(Set<String> definedAttributesNames)  {
		 conditionsDetector = new AllConditionsDetector(definedAttributesNames);
	}

	@Override
	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
			throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		conditionsDetector.startElement( attributes, null);
	}

	/**
	 * Get the found conditions.
	 * @return a map with conditions.
	 */
	public LinkedHashMap<String, LinkedHashSet<String>> getProfilingMap() {
		return conditionsDetector.getAllConditionFromDocument();
	}
}
