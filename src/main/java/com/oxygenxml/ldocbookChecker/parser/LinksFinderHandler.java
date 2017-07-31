package com.oxygenxml.ldocbookChecker.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.LocatorImpl;

import com.oxygenxml.docbookChecker.CheckerInteractor;

/**
 * Sax event handler.
 * 
 * @author intern4
 *
 */
public class LinksFinderHandler extends DefaultHandler {

	/**
	 * List of external founded links
	 */
	private List<Link> externalLinksSet = new ArrayList<Link>();

	/**
	 * List of internal founded links
	 */
	private List<Link> internalLinksSet = new ArrayList<Link>();

	/**
	 * List of links of image founded
	 */
	private List<Link> imgLinksSet = new ArrayList<Link>();

	/**
	 * List with paragraph IDs
	 */
	private List<Id> paraIdsSet = new ArrayList<Id>();

	private Locator locator = new LocatorImpl();

	private CheckerInteractor interactor;

	private final String namespace = "http://www.w3.org/1999/xlink";

	/**
	 * Constructor
	 * 
	 * @param url
	 *          the parsed url
	 */
	public LinksFinderHandler(CheckerInteractor interactor) {
		this.interactor = interactor;
	}

	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	@Override
	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
			throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		findProfiligAtribute(localName, attributes);
		if (interactor.isSelectedCheckExternal()) {
			findExternalLink(localName, attributes);
		}

		if (interactor.isSelectedCheckImages()) {
			findImgLink(localName, attributes);
		}

		if (interactor.isSelectedCheckInternal()) {
			findParaIds(localName, attributes);
			findInternalLink(localName, attributes);
		}
	}

	
	
	
	public void findProfiligAtribute(String localName, org.xml.sax.Attributes attributes){
		String string = "";
		int i = 0; 
		while(string  != null){
			System.out.println("");
			string = attributes.getQName(i);
			System.out.println("Qname: "+ string);
			
			string = attributes.getType(i);
			System.out.println("type: "+ string);

			string = attributes.getLocalName(i);
			System.out.println("localName: "+ string);
			
			string = attributes.getValue(i);
			System.out.println("value: "+ string);
			i++;
			}
		
	}
	
	
	/**
	 * Find external link
	 * 
	 * @param localName
	 *          element
	 * @param attributes
	 *          attributes
	 */
	public void findExternalLink(String localName, org.xml.sax.Attributes attributes) {
		String atributeVal;
		// db5
		// link tag
		if ("link".equals(localName)) {

			atributeVal = attributes.getValue(namespace, "href");

			// attribute href
			if (atributeVal != null) {
				// add new Link in linksSet
				externalLinksSet.add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		// db4
		if ("ulink".equals(localName)) {
			atributeVal = attributes.getValue("url");

			if (atributeVal != null) {
				// add new Link in linksSet
				externalLinksSet.add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}
	}

	/**
	 * Find image link
	 * 
	 * @param qName
	 *          element
	 * @param attributes
	 *          attributes
	 */
	public void findImgLink(String localName, org.xml.sax.Attributes attributes) {
		// db5
		if ("imagedata".equals(localName)) {
			String atributeVal = attributes.getValue("fileref");

			if (atributeVal != null) {
				// add new Link in linksSet
				imgLinksSet.add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		// db4
		if ("inlinegraphic".equals(localName) || "graphic".equals(localName)) {
			String atributeVal = attributes.getValue("fileref");

			if (atributeVal != null) {
				// add new Link in linksSet
				imgLinksSet.add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}
	}

	/**
	 * Find paragraph IDs
	 * 
	 * @param qName
	 *          element
	 * @param attributes
	 *          attributes
	 */
	public void findParaIds(String localName, org.xml.sax.Attributes attributes) {
		// db5
		String atributeVal = attributes.getValue("xml:id");
		if (atributeVal != null) {
			// add new ID in IDsSet
			paraIdsSet.add(new Id(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
		} else {
			// db4
			atributeVal = attributes.getValue("id");
			if (atributeVal != null) {
				// add new ID in IDsSet
				paraIdsSet.add(new Id(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}
	}

	/**
	 * Find internal link
	 * 
	 * @param qName
	 *          element
	 * @param attributes
	 *          attributes
	 */
	public void findInternalLink(String localName, org.xml.sax.Attributes attributes) {
		// db4 and db5
		// link tag
		if ("link".equals(localName)) {
			String atributeVal = attributes.getValue("linkend");

			// linkend attribute
			if (atributeVal != null) {
				// add new Link in linksSet
				internalLinksSet.add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		// xref tag
		if ("xref".equals(localName)) {
			String atributeVal = attributes.getValue("linkend");

			// linkend attribute
			if (atributeVal != null) {
				// add new Link in linksSet
				internalLinksSet.add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			} else {
				// xlink:href for db5
				atributeVal = attributes.getValue(namespace, "href");
				if (atributeVal != null) {
					// add new Link in linksSet
					internalLinksSet
							.add(new Link(atributeVal.substring(1), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
				}
			}
		}

	}

	/**
	 * Get founded results.
	 * 
	 * @return results
	 */
	public LinkDetails getResults() {
		return new LinkDetails(externalLinksSet, imgLinksSet, paraIdsSet, internalLinksSet);
	}

	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
		if (publicId != null) {
			return new InputSource(new ByteArrayInputStream(new byte[0]));
		} else {
			return null;
		}
	}

}
