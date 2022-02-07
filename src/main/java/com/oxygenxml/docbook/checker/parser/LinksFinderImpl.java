package com.oxygenxml.docbook.checker.parser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.profiling.ProfilingConditionsInformations;

/**
 * Finder for links, IDs and conditions.
 * 
 * @author Cosmin Duna
 *
 */
public class LinksFinderImpl implements LinksFinder {

	/**
	 * Logger
	 */
	 private static final Logger logger = LoggerFactory.getLogger(LinksFinderImpl.class);
	
	
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
			URL url, URL startDocumentURL, LinkedHashMap<String, LinkedHashSet<String>> conditions, CheckerInteractor interactor)
		 throws ParserConfigurationException, SAXException, IOException {


		XMLReader xmlReader = parserCreator.createXMLReader();

		LinkFinderHandler userhandler = new LinkFinderHandler(startDocumentURL, interactor, profilingInformation, conditions);
		xmlReader.setContentHandler(userhandler);
		try {
			xmlReader.parse(url.toURI().toString());
		} catch (URISyntaxException e) {
			logger.debug(e.getMessage(), e);
		}

		return userhandler.getResults();

	}


}