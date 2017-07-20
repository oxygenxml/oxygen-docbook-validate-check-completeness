package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * Finder for links and IDs.
 * 
 * @author intern4
 *
 */
public class LinksFinder {
		
	/**
	 * Gather the references from the content of the given url.
	 * 
	 * @param url
	 *          the url
	 * @return a Links object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws Exception
	 */
	public Results gatherLinks(URL url) throws ParserConfigurationException, SAXException, IOException {
		
		InputStream content = ContentGetter.openStream(url);

		LinksFinderHandler userhandler = new LinksFinderHandler(url);

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		SAXParser saxParser = factory.newSAXParser();
		
		saxParser.parse(content, userhandler);

		try {
			content.close();
		} catch (Exception e) {
		}

		return userhandler.getResults();

	}

}