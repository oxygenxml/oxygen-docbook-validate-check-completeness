package com.oxygenxml.profiling;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.oxygenxml.docbook.checker.parser.OxygenParserCreator;
import com.oxygenxml.docbook.checker.parser.ParserCreator;

/**
 * Used for find profiling conditions from a document.
 * @author intern4
 *
 */
public class ProfileDocsFinder {
/**
 * Gather profiling conditions from a given URL.
 *	
 * @param url The URL
 * @return A map with conditions.
 * @throws ParserConfigurationException
 * @throws SAXException
 * @throws IOException
 */
	public LinkedHashMap<String, LinkedHashSet<String>> gatherProfilingConditions( String url)
			throws ParserConfigurationException, SAXException, IOException {
		
			ParserCreator parserCreator = new OxygenParserCreator();
			
			ProfileDocsFinderHandler userhandler = new ProfileDocsFinderHandler(parserCreator);

			InputSource is = new InputSource(url);
			
			XMLReader xmlReader = parserCreator.createXMLReader();
			
			xmlReader.setContentHandler(userhandler);
			xmlReader.parse(is);
			
			return userhandler.getProfilingMap();
	}
}
