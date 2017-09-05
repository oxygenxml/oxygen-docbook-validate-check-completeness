package com.oxygenxml.profiling;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.oxygenxml.docbook.checker.parser.ParserCreator;
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
	
	public ProfileDocsFinderHandler(Set<String> definedAttributesNames) throws ParserConfigurationException, SAXException, IOException {
		 conditionsDetector = new AllConditionsDetector(definedAttributesNames);
	}

	@Override
	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
			throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		conditionsDetector.startElement(localName, attributes);
	}


	public LinkedHashMap<String, LinkedHashSet<String>> getProfilingMap() {
		return conditionsDetector.getAllConditionFromDocument();
	}
}
