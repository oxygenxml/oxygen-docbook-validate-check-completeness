package com.oxygenxml.ldocbookChecker.parser;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;

public class OxygenParserCreator implements ParserCreator {

	@Override
	public XMLReader createXMLReader()
			throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException, SAXException {
		return PluginWorkspaceProvider.getPluginWorkspace().getXMLUtilAccess().newNonValidatingXMLReader();
	}

}
