package com.oxygenxml.docbook.checker.parser;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;

/**
 * Create a parser using ro.sync.exml.workspace.api.util.XMLUtilAccess
 * @author intern4 
 *
 */
public class OxygenParserCreator implements ParserCreator {

	/**
	 * Create a XMLReader using newNonValidatingXMLReader() method.
	 */
	@Override
	public XMLReader createXMLReader()
			throws ParserConfigurationException, SAXException {
		return PluginWorkspaceProvider.getPluginWorkspace().getXMLUtilAccess().newNonValidatingXMLReader();
	}

}
