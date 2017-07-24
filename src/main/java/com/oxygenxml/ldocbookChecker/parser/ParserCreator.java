package com.oxygenxml.ldocbookChecker.parser;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public interface ParserCreator {

	public XMLReader createXMLReader() throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException, SAXException;
}
