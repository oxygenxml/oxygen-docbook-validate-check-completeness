package com.oxygenxml.docbook.checker.parser;

import org.xml.sax.Locator;

import com.oxygenxml.docbook.checker.CheckerInteractor;

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
	 * The url of the document.
	 */
	private String documentURL;

	/**
	 * Constructor
	 * @param interactor Checker interactor .
	 */
	public ElementLinkDetailsDetector(CheckerInteractor interactor, String documentURL) {

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
			Locator locator, boolean isFilter , DocumentDetails resultLinkDetails) {

		//Search for external links
		if (interactor.isCheckExternal() && !isFilter) {
			findExternalLink(localName, attributes, locator, resultLinkDetails);
		}
		
		//Search for internal links
		if (interactor.isCheckImages() && !isFilter ) {
			findImgLink(localName, attributes, locator, resultLinkDetails);
		}

		//Search for IDs and internal links.
		if (interactor.isCheckInternal()) {
			if(!isFilter){
				findInternalLink(localName, attributes, locator, resultLinkDetails);
			}	
			findParaIds(localName, attributes, isFilter, resultLinkDetails);
		}
	}

	/**
	 * Search for external links
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param resultLinkDetails Object to store found linkDetails
	 */
	private void findExternalLink(String localName, org.xml.sax.Attributes attributes, Locator locator, 
			DocumentDetails resultLinkDetails) {
		String atributeVal;
		// db5
		// link tag
		if ("link".equals(localName)) {

			atributeVal = attributes.getValue(XLINK_NAMESPACE, "href");

			// attribute href
			if (atributeVal != null) {
				// add a new Link in resultLinkDetails
				resultLinkDetails.addExternalLink(new Link(atributeVal, documentURL, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		// db4
		if ("ulink".equals(localName)) {
			atributeVal = attributes.getValue("url");

			if (atributeVal != null) {
				// add a new Link in resultLinkDetails
				resultLinkDetails.addExternalLink(new Link(atributeVal, documentURL,  locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
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
	private void findImgLink(String localName, org.xml.sax.Attributes attributes, Locator locator,	DocumentDetails resultLinkDetails) {
		// db5
		if ("imagedata".equals(localName)) {
			String atributeVal = attributes.getValue("fileref");

			if (atributeVal != null) {
				// add a new Link in resultLinkDetails
				resultLinkDetails.addImage(new Link(atributeVal, documentURL, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));

			}
		}

		// db4
		if ("inlinegraphic".equals(localName) || "graphic".equals(localName)) {
			String atributeVal = attributes.getValue("fileref");

			if (atributeVal != null) {
				// add new Link in resultLinkDetails
				resultLinkDetails.addImage(new Link(atributeVal, documentURL, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}
	}

	/**
	 * Search for IDs
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param isFilter <code>true</code> if element is filter, <code>false</code>otherwise.
	 * @param resultLinkDetails Object to store found linkDetails
	 */
	private void findParaIds(String localName, org.xml.sax.Attributes attributes, boolean isFilter,
			DocumentDetails resultLinkDetails) {
		// db5
		String atributeVal = attributes.getValue("xml:id");

		if (atributeVal == null) {
			// db4
			atributeVal = attributes.getValue("id");
		}
		if (atributeVal != null) {
			Id newId = new Id(atributeVal, isFilter);

			// add new ID in resultLinkDetails
			resultLinkDetails.addId(newId);
		}
	}

	/**
	 * Search for internal links
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param resultLinkDetails Object to store found linkDetails
	 */
	private void findInternalLink(String localName, org.xml.sax.Attributes attributes, Locator locator,
			DocumentDetails resultLinkDetails) {
		// db4 and db5
		// link tag
		if ("link".equals(localName)) {
			String atributeVal = attributes.getValue("linkend");

			// linkend attribute
			if (atributeVal != null) {
				// add new Link in resultLinkDetails
				resultLinkDetails.addInternalLink(new Link(atributeVal, documentURL,  locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		// xref tag
		if ("xref".equals(localName)) {
			String atributeVal = attributes.getValue("linkend");

			// linkend attribute
			if (atributeVal != null) {
				// add new Link in resultLinkDetails
				resultLinkDetails.addInternalLink(new Link(atributeVal, documentURL, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));

			} else {
				// xlink:href for db5
				//the value is in format:"#value"; 
				atributeVal = attributes.getValue(XLINK_NAMESPACE, "href").substring(1);
				if (atributeVal != null) {
					// add new Link in resultLinkDetails
					resultLinkDetails.addInternalLink(new Link(atributeVal, documentURL, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
				}
			}
		}

	}


}
