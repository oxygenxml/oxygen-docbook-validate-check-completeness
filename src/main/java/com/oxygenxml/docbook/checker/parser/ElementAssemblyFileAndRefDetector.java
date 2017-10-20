package com.oxygenxml.docbook.checker.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

import org.xml.sax.Locator;

/**
 * Detector for topics and assembly links in assembly file.
 * @author intern4
 *
 */
public class ElementAssemblyFileAndRefDetector {

	/**
	 * The url of the document.
	 */
	private URL documentURL;

	/**
	 * The stack of location.
	 */
	private Stack<URL> locationStack = new Stack<URL>();
	
	/**
	 * The document details found.
	 */
	private DocumentDetails resultDocumentDetails = new DocumentDetails();
	
	
	
	/**
	 * Constructor
	 * @param interactor Checker interactor .
	 */
	public ElementAssemblyFileAndRefDetector( URL documentURL) {
		this.documentURL = documentURL;
	}

	/**
	 * Detect assembled files(topics) and assembly links when element start 
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param isFilter <code>true</code> if element is filter, <code>false</code>otherwise.
	 * @param resultDocumentDetails Object to store found documentDetails
	 */
	public void startElement(String localName, org.xml.sax.Attributes attributes,
			Locator locator, boolean isFilter ) {

		//Search for assembly file;
		findAssembledFile(localName, attributes, locator, isFilter);
		
		//Search for assembly link
		if (!isFilter) {
			findAssemblyLink(localName, attributes, locator);
		}
		
		try {
			locationStack.push(new URL(locator.getSystemId()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Pop element from locationStack.
	 * 
	 */
	public void endElement(){
		locationStack.pop();
	}

	/**
	 * Search for assembled file(topic) with ID.
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 */
	private void findAssembledFile(String localName, org.xml.sax.Attributes attributes, Locator locator, boolean isFilter) {
		// assembly file tag
		if ("resource".equals(localName)) {

			String atributeFileVal = attributes.getValue("href");

			String atributeIdVal = attributes.getValue("xml:id");

			if (atributeFileVal != null && atributeIdVal != null ) {
				// add file in resultLinkDetails
				resultDocumentDetails.addAssemblyTopic(new AssemblyTopicId(atributeIdVal, atributeFileVal, locator.getSystemId(), isFilter, locator.getLineNumber() , locator.getColumnNumber()));
			}
		}
	}

	/**
	 * Search for assembly links
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 */
	private void findAssemblyLink(String localName, org.xml.sax.Attributes attributes, Locator locator) {
		
		// assembly reference tag
		if ("module".equals(localName)) {
			String atributeVal = attributes.getValue("resourceref");

			if (atributeVal != null) {
				// add a new Link in resultLinkDetails
				resultDocumentDetails.addAssemblyLink(new Link(atributeVal, LinkType.ASSEMBLY, documentURL, (Stack<URL>)locationStack.clone(),
						locator.getLineNumber(), locator.getColumnNumber()));

			}
		}
	}

	
	/**
	 * Get found results.
	 * 
	 * @return results The results.
	 */
	public DocumentDetails getResults() {
		return resultDocumentDetails;
	}
}
