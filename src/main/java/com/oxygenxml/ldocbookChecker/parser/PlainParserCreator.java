package com.oxygenxml.ldocbookChecker.parser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class PlainParserCreator implements ParserCreator {

	@Override
	public XMLReader createXMLReader() throws ParserConfigurationException, SAXException {

		System.out.println("creazaParserr");
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);

		factory.setFeature("http://apache.org/xml/features/xinclude", true);
		
		SAXParser saxParser = factory.newSAXParser();
		
		System.out.println("a creat parser : " + saxParser.toString());
		return saxParser.getXMLReader();
	}

}
 