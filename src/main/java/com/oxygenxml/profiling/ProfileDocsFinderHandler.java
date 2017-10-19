package com.oxygenxml.profiling;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

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
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public ProfileDocsFinderHandler(Set<String> definedAttributesNames) throws ParserConfigurationException, SAXException, IOException {
		 conditionsDetector = new AllConditionsDetector(definedAttributesNames);
	}

	@Override
	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
			throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		conditionsDetector.startElement(localName, attributes, null);
	}

	/**
	 * Get the found conditions.
	 * @return a map with conditions.
	 */
	public LinkedHashMap<String, LinkedHashSet<String>> getProfilingMap() {
		return conditionsDetector.getAllConditionFromDocument();
	}
}
