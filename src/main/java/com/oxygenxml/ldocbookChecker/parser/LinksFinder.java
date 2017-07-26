package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbookChecker.Settings;

public interface LinksFinder {

	public LinkDetails gatherLinks(ParserCreator parserCreator, URL url, Settings settings) throws ParserConfigurationException, SAXException, IOException;
	
}
