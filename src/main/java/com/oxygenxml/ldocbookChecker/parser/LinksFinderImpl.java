package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;


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
	 * @param url
	 *          the url
	 * @return a Links object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws Exception
	 */
public LinkDetails gatherLinks(ParserCreator parserCreator, URL url, boolean parseExternal)
		throws ParserConfigurationException, SAXException, IOException {
		
//		InputStream content = ContentGetter.openStream(url);
		
		LinksFinderHandler userhandler = new LinksFinderHandler(url, parseExternal);

		InputSource is = new InputSource(url.toString());
		XMLReader xmlReader = parserCreator.createXMLReader();
		xmlReader.setErrorHandler(new ErrorHandler() {
			
			@Override
			public void warning(SAXParseException exception) throws SAXException {
				System.err.println("WARN");
				exception.printStackTrace();
				
			}
			
			@Override
			public void fatalError(SAXParseException exception) throws SAXException {
				System.err.println("Fatal");
				exception.printStackTrace();
			}
			
			@Override
			public void error(SAXParseException exception) throws SAXException {
				// TODO Auto-generated method stub
				System.err.println("Error");
				exception.printStackTrace();
			}
		});
//		is.setSystemId(url.toString());
		xmlReader.setContentHandler(userhandler);
		xmlReader.parse(is);

//		try {
//			content.close();
//		} catch (Exception e) {
//		}

		return userhandler.getResults();

	}

}
