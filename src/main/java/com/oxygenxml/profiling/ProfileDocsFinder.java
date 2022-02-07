package com.oxygenxml.profiling;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.oxygenxml.docbook.checker.parser.OxygenParserCreator;
import com.oxygenxml.docbook.checker.parser.ParserCreator;

/**
 * Used for find profiling conditions from a document.
 * 
 * @author Cosmin Duna
 *
 */
public class ProfileDocsFinder {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProfileDocsFinder.class);

	/**
	 * Gather profiling conditions from a given URL.
	 * 
	 * @param url
	 *          The URL
	 * @return A map with conditions.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public LinkedHashMap<String, LinkedHashSet<String>> gatherProfilingConditions(URL url,
			Set<String> definedAttributesNames) throws ParserConfigurationException, SAXException, IOException {

		ParserCreator parserCreator = new OxygenParserCreator();

		ProfileDocsFinderHandler userhandler = new ProfileDocsFinderHandler(definedAttributesNames);

		XMLReader xmlReader = parserCreator.createXMLReader();

		xmlReader.setContentHandler(userhandler);
		try {
			xmlReader.parse(url.toURI().toString());
		} catch (URISyntaxException e) {
			logger.debug(e.getMessage(), e);
		}

		return userhandler.getProfilingMap();
	}
}
