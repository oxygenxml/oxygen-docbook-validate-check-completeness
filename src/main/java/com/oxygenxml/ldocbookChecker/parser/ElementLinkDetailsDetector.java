package com.oxygenxml.ldocbookChecker.parser;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Locator;

import com.oxygenxml.docbookChecker.CheckerInteractor;

/**
 * Link details detector from a element.
 * @author intern4
 *
 */
public class ElementLinkDetailsDetector {

	private CheckerInteractor interactor;

	private final String namespace = "http://www.w3.org/1999/xlink";

	/**
	 * Constructor
	 * @param interactor User settings.
	 */
	public ElementLinkDetailsDetector(CheckerInteractor interactor) {

		this.interactor = interactor;

	}

	/**
	 * Detect linkDetails when element start 
	 * @param localName Local name of element.
	 * @param attributes	The attributes of element.
	 * @param locator		The locator or element.
	 * @param elementConditions Profile conditions on this element; <code>null</code> if user don't use profile conditions.
	 * @param resultLinkDetails Object to store found linkDetails
	 */
	public void startElement(String localName, org.xml.sax.Attributes attributes,
			Locator locator, boolean isFilter , LinkDetails resultLinkDetails) {

		//Search for external links
		if (interactor.isSelectedCheckExternal() && !isFilter) {
			findExternalLink(localName, attributes, locator, resultLinkDetails);
		}
		
		//Search for internal links
		if (interactor.isSelectedCheckImages() && !isFilter ) {
			findImgLink(localName, attributes, locator, resultLinkDetails);
		}

		//Search for IDs and internal links.
		if (interactor.isSelectedCheckInternal()) {
			if(!isFilter){
				findInternalLink(localName, attributes, locator, resultLinkDetails);
			}	
			findParaIds(localName, attributes, locator, isFilter, resultLinkDetails);
		}
	}

	/**
	 * Search for external links
	 * @param localName
	 * @param attributes
	 * @param locator
	 * @param elementConditions
	 * @param resultLinkDetails
	 */
	private void findExternalLink(String localName, org.xml.sax.Attributes attributes, Locator locator, 
			LinkDetails resultLinkDetails) {
		String atributeVal;
		// db5
		// link tag
		if ("link".equals(localName)) {

			atributeVal = attributes.getValue(namespace, "href");

			// attribute href
			if (atributeVal != null) {
				// add a new Link in resultLinkDetails
				resultLinkDetails.getExternalLinks().add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		// db4
		if ("ulink".equals(localName)) {
			atributeVal = attributes.getValue("url");

			if (atributeVal != null) {
				// add a new Link in resultLinkDetails
				resultLinkDetails.getExternalLinks().add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}
	}

	/**
	 * Search for image links
	 * @param localName
	 * @param attributes
	 * @param locator
	 * @param elementConditions
	 * @param resultLinkDetails
	 */
	private void findImgLink(String localName, org.xml.sax.Attributes attributes, Locator locator,	LinkDetails resultLinkDetails) {
		// db5
		if ("imagedata".equals(localName)) {
			String atributeVal = attributes.getValue("fileref");

			if (atributeVal != null) {
				// add a new Link in resultLinkDetails
				resultLinkDetails.getImgLinks().add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));

			}
		}

		// db4
		if ("inlinegraphic".equals(localName) || "graphic".equals(localName)) {
			String atributeVal = attributes.getValue("fileref");

			if (atributeVal != null) {
				// add new Link in resultLinkDetails
				resultLinkDetails.getImgLinks().add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}
	}

	/**
	 * Search for IDs
	 * @param localName
	 * @param attributes
	 * @param locator
	 * @param elementConditions
	 * @param resultLinkDetails
	 */
	private void findParaIds(String localName, org.xml.sax.Attributes attributes, Locator locator, boolean isFilter,
			LinkDetails resultLinkDetails) {
		// db5
		String atributeVal = attributes.getValue("xml:id");

		if (atributeVal == null) {
			// db4
			atributeVal = attributes.getValue("id");
		}
		if (atributeVal != null) {
			Id newId = new Id(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber(), isFilter);

			// add new ID in resultLinkDetails
			resultLinkDetails.getParaIds().add(newId);
		}
	}

	/**
	 * Search for internal links
	 * @param localName
	 * @param attributes
	 * @param locator
	 * @param elementConditions
	 * @param resultLinkDetails
	 */
	private void findInternalLink(String localName, org.xml.sax.Attributes attributes, Locator locator,
			LinkDetails resultLinkDetails) {
		// db4 and db5
		// link tag
		if ("link".equals(localName)) {
			String atributeVal = attributes.getValue("linkend");

			// linkend attribute
			if (atributeVal != null) {
				// add new Link in resultLinkDetails
				resultLinkDetails.getInternalLinks().add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		// xref tag
		if ("xref".equals(localName)) {
			String atributeVal = attributes.getValue("linkend");

			// linkend attribute
			if (atributeVal != null) {
				// add new Link in resultLinkDetails
				resultLinkDetails.getInternalLinks().add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));

			} else {
				// xlink:href for db5
				atributeVal = attributes.getValue(namespace, "href");
				if (atributeVal != null) {
					// add new Link in resultLinkDetails
					resultLinkDetails.getInternalLinks().add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
				}
			}
		}

	}


}
