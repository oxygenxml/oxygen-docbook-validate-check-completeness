package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.oxygenxml.docbookChecker.CheckerInteractor;

/**
 * Finder for links and IDs.
 * 
 * @author intern4
 *
 */
public class LinksFinderImpl implements LinksFinder {

	/**
	 * Gather the references from the content of the given url.
	 * 
	 * @param String
	 *          the url
	 * @return a Links object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws Exception
	 */
	public LinkDetails gatherLinks(ParserCreator parserCreator, String url, Map<String, Set<String>> conditions,
			CheckerInteractor interactor) throws ParserConfigurationException, SAXException, IOException {

		InputSource is = new InputSource(url);

		XMLReader xmlReader = parserCreator.createXMLReader();

		if (interactor.isSelectedCheckUsingProfile()) {
			LinkWithConditionsFinderHandler userhandler = new LinkWithConditionsFinderHandler(interactor, conditions);
			xmlReader.setContentHandler(userhandler);
			xmlReader.parse(is);

			return userhandler.getResults();
		}

		else {
			LinksFinderHandler userhandler = new LinksFinderHandler(interactor);
			xmlReader.setContentHandler(userhandler);
			xmlReader.parse(is);
			System.out.println("rezultate : " +userhandler.getResults().toString());
			return userhandler.getResults();

		}

	}

}
