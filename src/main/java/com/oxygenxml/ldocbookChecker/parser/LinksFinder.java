package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbookChecker.CheckerInteractor;
/**
 * Interface used for parse and find links.
 * @author intern4
 *
 */
public interface LinksFinder {

	/**
	 * 
	 * @param parserCreator
	 * @param url
	 * @param interactor
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public LinkDetails gatherLinks(ParserCreator parserCreator, String url, LinkedHashMap<String, LinkedHashSet<String>> conditions, CheckerInteractor interactor) throws ParserConfigurationException, SAXException, IOException;
	
}
