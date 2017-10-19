package com.oxygenxml.docbook.checker.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.xml.sax.Locator;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.docbook.checker.reporters.OxygenStatusReporter;

/**
 * Link details detector from a element.
 * @author intern4
 *
 */
public class ElementLinkDetailsDetector {

	/**
	 * Checker interacor.
	 */
	private CheckerInteractor interactor;

	/**
	 * The Xlink namespace.
	 */
	private static final String XLINK_NAMESPACE = "http://www.w3.org/1999/xlink";

	/**
	 * The URL of the document.
	 */
	private URL documentURL;

	/**
	 * The stack of location.
	 */
	private Stack<URL> locationStack = new Stack<URL>();

	/**
	 * The document details found.
	 */
	private DocumentDetails toReturnLinksDetails = new DocumentDetails();
	
	/**
	 * Logger
	 */
	 private static final Logger logger = Logger.getLogger(OxygenStatusReporter.class);
	
	
	/**
	 * Constructor
	 * @param interactor Checker interactor .
	 * @param documentURL The URL of document.
	 */
	public ElementLinkDetailsDetector(CheckerInteractor interactor, URL documentURL) {

		this.interactor = interactor;
		this.documentURL = documentURL;

	}

	/**
	 * Detect linkDetails when element start 
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param isFilter <code>true</code> if element is filter, <code>false</code>otherwise.
	 * @param resultLinkDetails Object to store found linkDetails
	 */
	public void startElement(String localName, org.xml.sax.Attributes attributes,
			Locator locator, boolean isFilter) {

		//Search for external links
		if( (interactor.isCheckExternal() || interactor.isGenerateHierarchyReport())  && !isFilter) {
			findExternalLink(localName, attributes, locator);
		}
		//Search for internal links
		if ((interactor.isCheckImages()|| interactor.isGenerateHierarchyReport()) && !isFilter ) {
			findImgLink(localName, attributes, locator);
		}

		//Search for IDs and internal links.
		if (interactor.isCheckInternal() || interactor.isGenerateHierarchyReport()) {
			if(!isFilter){
				findInternalLink(localName, attributes, locator);
			}
			if(interactor.isCheckInternal()){
				findParaIds(localName, attributes, locator,isFilter);
			}
		}
		
		try {
			locationStack.push(new URL(locator.getSystemId()) );
		} catch (MalformedURLException e) {
			logger.debug(e.getMessage(), e);
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
	 * Search for external links
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param resultLinkDetails Object to store found linkDetails
	 */
	private void findExternalLink(String localName, org.xml.sax.Attributes attributes, Locator locator) {
		String atributeVal;
		// db5
		// link tag
		if ("link".equals(localName)) {

			atributeVal = attributes.getValue(XLINK_NAMESPACE, "href");
			// attribute href
			if (atributeVal != null) {
				// add a new Link in resultLinkDetails
				toReturnLinksDetails.addExternalLink(new Link(atributeVal, documentURL, (Stack<URL>)locationStack.clone(), 
						locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		// db4
		if ("ulink".equals(localName)) {
			atributeVal = attributes.getValue("url");

			if (atributeVal != null) {
				// add a new Link in resultLinkDetails
				toReturnLinksDetails.addExternalLink(new Link(atributeVal, documentURL,  (Stack<URL>)locationStack.clone(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}
	}

	/**
	 * Search for image links
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param resultLinkDetails Object to store found linkDetails
	 */
	private void findImgLink(String localName, org.xml.sax.Attributes attributes, Locator locator) {
		// db5
		if ("imagedata".equals(localName)) {
			String atributeVal = attributes.getValue("fileref");

			if (atributeVal != null) {
				// add a new Link in resultLinkDetails
				toReturnLinksDetails.addImage(new Link(atributeVal, documentURL, (Stack<URL>)locationStack.clone(), locator.getLineNumber(), locator.getColumnNumber()));

			}
		}

		// db4
		if ("inlinegraphic".equals(localName) || "graphic".equals(localName)) {
			String atributeVal = attributes.getValue("fileref");

			if (atributeVal != null) {
				// add new Link in resultLinkDetails
				toReturnLinksDetails.addImage(new Link(atributeVal, documentURL, (Stack<URL>)locationStack.clone(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}
	}

	/**
	 * Search for IDs
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator of element.
	 * @param isFilter <code>true</code> if element is filter, <code>false</code>otherwise.
	 * @param resultLinkDetails Object to store found linkDetails
	 */
	private void findParaIds(String localName, org.xml.sax.Attributes attributes, Locator locator, boolean isFilter) {
		if (!"resource".equals(localName)) {
			// db5
			String atributeVal = attributes.getValue("xml:id");

			if (atributeVal == null) {
				// db4
				atributeVal = attributes.getValue("id");
			}
			if (atributeVal != null) {
				Id newId = new Id(atributeVal, isFilter, locator.getSystemId(), locator.getLineNumber(),
						locator.getColumnNumber());

				// add new ID in resultLinkDetails
				toReturnLinksDetails.addId(newId);
			}
		}
	}

	/**
	 * Search for internal links
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param resultLinkDetails Object to store found linkDetails
	 */
	private void findInternalLink(String localName, org.xml.sax.Attributes attributes, Locator locator) {
		// db4 and db5
		// link tag
		if ("link".equals(localName)) {
			String atributeVal = attributes.getValue("linkend");

			// linkend attribute
			if (atributeVal != null) {
				// add new Link in resultLinkDetails
				toReturnLinksDetails.addInternalLink(new Link(atributeVal, documentURL,  (Stack<URL>)locationStack.clone(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		// xref tag
		if ("xref".equals(localName)) {
			String atributeVal = attributes.getValue("linkend");

			// linkend attribute
			if (atributeVal != null) {
				// add new Link in resultLinkDetails
				toReturnLinksDetails.addInternalLink(new Link(atributeVal, documentURL, (Stack<URL>)locationStack.clone(), locator.getLineNumber(), locator.getColumnNumber()));

			} else {
				// xlink:href for db5
				//the value is in format:"#value"; 
				atributeVal = attributes.getValue(XLINK_NAMESPACE, "href").substring(1);
				if (atributeVal != null) {
					// add new Link in resultLinkDetails
					toReturnLinksDetails.addInternalLink(new Link(atributeVal, documentURL, (Stack<URL>)locationStack.clone(), locator.getLineNumber(), locator.getColumnNumber()));
				}
			}
		}
	}

	
	/**
	 * Get found results.
	 * 
	 * @return results The results.
	 */
	public DocumentDetails getResults() {
		return toReturnLinksDetails;
	}
}
