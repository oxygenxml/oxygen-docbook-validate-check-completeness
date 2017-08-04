package com.oxygenxml.profiling;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.oxygenxml.ldocbookChecker.parser.ParserCreator;

/**
 * Finder for profiling conditions from document to check.
 * @author intern4
 *
 */
public class ProfileDocsFinder {

	public Map<String, Set<String>> gatherProfilingConditions(ParserCreator parserCreator, String url)
			throws ParserConfigurationException, SAXException, IOException {
			
			ProfileDocsFinderHandler userhandler = new ProfileDocsFinderHandler(parserCreator);

			InputSource is = new InputSource(url);
			
			XMLReader xmlReader = parserCreator.createXMLReader();
			
			xmlReader.setContentHandler(userhandler);
			xmlReader.parse(is);
			
			return userhandler.getProfilingMap();
	}
}
