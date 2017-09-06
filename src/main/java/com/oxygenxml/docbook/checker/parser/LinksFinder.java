package com.oxygenxml.docbook.checker.parser;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
/**
 * Interface used for parse and find links and conditions.
 * @author intern4
 *
 */
public interface LinksFinder {

	/**
	 * Gather for links, IDs and conditions from the document linked at the given URL.
	 * 
	 * @param parserCreator Parser creator.
	 * @param profilingInformation Profiling informations.
	 * @param url	The URL of the document.
	 * @param conditions 	The conditions that filter the document.
	 * @param interactor Checker interactor.
	 * @return The links, IDs and conditions found.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public DocumentDetails gatherLinksAndConditions(ParserCreator parserCreator, ProfilingConditionsInformations profilingInformation, 
			String url, LinkedHashMap<String, LinkedHashSet<String>> conditions, CheckerInteractor interactor) 
					throws ParserConfigurationException, SAXException, IOException;
	
}
