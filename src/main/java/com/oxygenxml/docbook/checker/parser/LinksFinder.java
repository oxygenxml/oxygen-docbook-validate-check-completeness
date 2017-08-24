package com.oxygenxml.docbook.checker.parser;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
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
	public LinkDetails gatherLinksAndConditions(ParserCreator parserCreator, ProfilingConditionsInformations profilingInformation, String url, LinkedHashMap<String, LinkedHashSet<String>> conditions, CheckerInteractor interactor) throws ParserConfigurationException, SAXException, IOException;
	
}
