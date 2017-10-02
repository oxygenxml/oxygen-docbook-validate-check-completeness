package com.oxygenxml.docbook.checker.parser;

import org.xml.sax.Locator;

import com.oxygenxml.docbook.checker.CheckerInteractor;

public class ElementAssemblyFileAndRefDetector {

	/**
	 * Checker interacor.
	 */
	private CheckerInteractor interactor;

	/**
	 * The Xlink namespace.
	 */
	private static final String XLINK_NAMESPACE = "http://www.w3.org/1999/xlink";

	/**
	 * The url of the document.
	 */
	private String documentURL;

	
	/**
	 * Constructor
	 * @param interactor Checker interactor .
	 */
	public ElementAssemblyFileAndRefDetector(CheckerInteractor interactor, String documentURL) {

		this.interactor = interactor;
		this.documentURL = documentURL;

	}

	/**
	 * Detect assembly files and assembly links when element start 
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param isFilter <code>true</code> if element is filter, <code>false</code>otherwise.
	 * @param resultDocumentDetails Object to store found documentDetails
	 */
	public void startElement(String localName, org.xml.sax.Attributes attributes,
			Locator locator, boolean isFilter , DocumentDetails resultDocumentDetails) {

		//Search for assembly file;
		findAssemblyFile(localName, attributes, locator, isFilter, resultDocumentDetails);
		
		//Search for assembly link
		if (!isFilter) {
			findAssemblyLink(localName, attributes, locator, resultDocumentDetails);
		}
	}

	/**
	 * Search for assembly file.
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param resultDocumentDetails Object to store found documentDetails
	 */
	private void findAssemblyFile(String localName, org.xml.sax.Attributes attributes, Locator locator, boolean isFilter,
			DocumentDetails resultDocumentDetails) {
		// assembly file tag
		if ("resource".equals(localName)) {

			String atributeFileVal = attributes.getValue("href");

			String atributeIdVal = attributes.getValue("xml:id");

			if (atributeFileVal != null && atributeIdVal != null ) {
				// add file in resultLinkDetails
				resultDocumentDetails.addAssemblyFile(new AssemblyFileId(atributeIdVal, atributeFileVal, isFilter));
			}
		}
	}

	/**
	 * Search for assembly links
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param resultDocumentDetails Object to store found documentDetails
	 */
	private void findAssemblyLink(String localName, org.xml.sax.Attributes attributes, Locator locator,	DocumentDetails resultDocumentDetails) {
		
		// assembly reference tag
		if ("module".equals(localName)) {
			String atributeVal = attributes.getValue("resourceref");

			if (atributeVal != null) {
				// add a new Link in resultLinkDetails
				resultDocumentDetails.addAssemblyLink(new Link(atributeVal, documentURL, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));

			}
		}
	}

}
