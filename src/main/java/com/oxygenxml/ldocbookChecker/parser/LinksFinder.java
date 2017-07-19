package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * Finder for external links.
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
	public Links gatherLinks(URL url) throws ParserConfigurationException, SAXException, IOException {
		XmlContentGetter contentGetter = new XmlContentGetter();
		InputStream content = null;

		content = contentGetter.getXml(url);

		SAXParserFactory factory = SAXParserFactory.newInstance();

		LinksFinderHandler userhandler = new LinksFinderHandler(url);

		SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(content, userhandler);

		try {
			content.close();
		} catch (Exception e) {
		}

		return userhandler.getLinks();

	}

}