package com.oxygenxml.docbook.checker.parser;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.profiling.ProfilingConditionsInformations;

/**
 * Finder for links, IDs and conditions.
 * 
 * @author intern4
 *
 */
public class LinksFinderImpl implements LinksFinder {

	/**
	 * Gather the references and conditions from the content of the given URL. 
	 *
	 * @param parserCreator Parser creator.
	 * @param profilingInformation Profiling informations.
	 * @param url	The URL of the document.
	 * @param startDocumentUrl The URL of the document where the parse started.
	 * @param conditions 	The conditions that filter the document.
	 * @param interactor Checker interactor.
	 * 
	 * @return The links, IDs and conditions found.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public DocumentDetails gatherLinksAndConditions(ParserCreator parserCreator, ProfilingConditionsInformations profilingInformation, 
			String url, String startDocumentURL, LinkedHashMap<String, LinkedHashSet<String>> conditions, CheckerInteractor interactor)
		 throws ParserConfigurationException, SAXException, IOException {

		InputSource is = new InputSource(url);

		XMLReader xmlReader = parserCreator.createXMLReader();

		LinkFinderHandler userhandler = new LinkFinderHandler(startDocumentURL, interactor, profilingInformation, conditions);
		xmlReader.setContentHandler(userhandler);
		xmlReader.parse(is);

		return userhandler.getResults();

	}


}