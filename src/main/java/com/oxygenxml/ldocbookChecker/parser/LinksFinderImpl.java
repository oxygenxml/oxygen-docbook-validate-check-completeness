package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;

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
public class LinksFinderImpl implements LinksFinder{
	
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
public LinkDetails gatherLinks(ParserCreator parserCreator, String url, CheckerInteractor interactor)
		throws ParserConfigurationException, SAXException, IOException {
		
		LinksFinderHandler userhandler = new LinksFinderHandler( interactor);

		InputSource is = new InputSource(url);
		
		XMLReader xmlReader = parserCreator.createXMLReader();
		
		
//		xmlReader.setErrorHandler(new ErrorHandler() {
//			
//			@Override
//			public void warning(SAXParseException exception) throws SAXException {
//				System.err.println("WARN");
//				exception.printStackTrace();
//				
//			}
//			
//			@Override
//			public void fatalError(SAXParseException exception) throws SAXException {
//				System.err.println("Fatal");
//				exception.printStackTrace();
//			}
//			
//			@Override
//			public void error(SAXParseException exception) throws SAXException {
//				// TODO print in oxygen console
//				System.err.println("Error");
//				exception.printStackTrace();
//			}
//		});

		xmlReader.setContentHandler(userhandler);
		xmlReader.parse(is);
		
		return userhandler.getResults();

	}

}
