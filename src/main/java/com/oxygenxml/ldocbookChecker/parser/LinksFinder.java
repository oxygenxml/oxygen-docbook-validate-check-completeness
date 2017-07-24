package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public interface LinksFinder {

	public LinkDetails gatherLinks(ParserCreator parserCreator, URL url, boolean parseExternal) throws ParserConfigurationException, SAXException, IOException;
	
}
