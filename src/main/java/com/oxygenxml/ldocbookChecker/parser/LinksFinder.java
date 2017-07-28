package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbookChecker.CheckerInteractor;

public interface LinksFinder {

	public LinkDetails gatherLinks(ParserCreator parserCreator, String url, CheckerInteractor interactor) throws ParserConfigurationException, SAXException, IOException;
	
}
