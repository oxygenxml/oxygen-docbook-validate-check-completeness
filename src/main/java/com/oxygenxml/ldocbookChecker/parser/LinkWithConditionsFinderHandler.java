package com.oxygenxml.ldocbookChecker.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
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
public class LinkWithConditionsFinderHandler extends DefaultHandler {

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

	Map<String, Set<String>> conditionsFromGui;

	// save tag and conditions before id attribute
	private Map<String, Map<String, Set<String>>> currentConditions = new HashMap<String, Map<String, Set<String>>>();

	/**
	 * Constructor
	 * 
	 * @param url
	 *          the parsed url
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public LinkWithConditionsFinderHandler(CheckerInteractor interactor, Map<String, Set<String>> userConditions)
			throws ParserConfigurationException, SAXException, IOException {
		this.interactor = interactor;

		// conditions for view;
		conditionsFromGui = userConditions;
	}

	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		findLinks(localName, attributes, interactor);
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		// remove conditions for localName tag when this is closing.
		currentConditions.remove(localName);
	}


/**
 * Find links.
 * @param localName
 * @param attributes
 * @param interactor 
 */
	public void findLinks(String localName, org.xml.sax.Attributes attributes, CheckerInteractor interactor) {
		// attribute localName
		String attribLocalName = "";
		// value of ID attribute
		String atributeValue = "";

		// line number of attribute
		int idLine = 0;
		// column number of attribute
		int idColumn = 0;

		int i = 0;

		// internal link attribute was found in this tag
		boolean foundInternal = false;
		// external link attribute was found in this tag
		boolean foundExternal = false;
		// image link attribute was found in this tag
		boolean foundImage = false;
		// ID attribute was found in this tag
		boolean foundID = false;

		// conditions on this tag
		Map<String, Set<String>> tagConditions = new HashMap<String, Set<String>>();

		// local name of attribute with index 0
		attribLocalName = attributes.getLocalName(i);
		while (attribLocalName != null) {

			// check if attribute is a profile condition
			if (conditionsFromGui.keySet().contains(attribLocalName)) {

				// take value
				String[] value = attributes.getValue(i).split(";");
				Set<String> valueSet = new HashSet<String>();

				for (int j = 0; j < value.length; j++) {
					valueSet.add(value[j]);
				}

				// add condition in tagConditions map
				tagConditions.put(attribLocalName, valueSet);
				
				//check if attribute is a link or id
			} else {
				if (interactor.isSelectedCheckExternal()) {
					foundExternal = isExternalLink(localName, attribLocalName);
				}
				if (interactor.isSelectedCheckInternal()) {
					foundInternal = isInternalLink(localName, attribLocalName);
					foundID = isParaIds(localName, attribLocalName);
				}
				if (interactor.isSelectedCheckImages()) {
					foundImage = isImgLink(localName, attribLocalName);
				}

				if (foundInternal || foundExternal || foundImage || foundID) {
					atributeValue = attributes.getValue(i);
					idLine = locator.getLineNumber();
					idColumn = locator.getColumnNumber();
				}
			}
			// get local name of next attribute
			i++;
			attribLocalName = attributes.getLocalName(i);
		}

		// add tagConditions at Map with currentConditions
		if (!tagConditions.isEmpty()) {
			currentConditions.put(localName, tagConditions);
		}

		//test if was found a link 
		if (foundInternal || foundExternal || foundImage) {
			Link newLink = new Link(atributeValue, locator.getSystemId(), idLine, idColumn);
			newLink.addConditions(currentConditions.values());
			System.out.println(newLink.toString());
			if (foundInternal) {
				internalLinksSet.add(newLink);
			} else if (foundExternal) {
				externalLinksSet.add(newLink);
			} else if (foundImage) {
				imgLinksSet.add(newLink);
			}

			//test if was found a ID
		} else if (foundID) {
			Id newId = new Id(atributeValue, locator.getSystemId(), idLine, idColumn);
			newId.setConditions(currentConditions.values());
			System.out.println(newId.toString());
			paraIdsSet.add(newId);
		}

	}

	
	/**
	 * Check if a attribute is a paragraph IDs
	 * 
	 * @param localName
	 * @param attributeName localNameOfAtribute to check
	 * @return <code>true</code> if it's a Id
	 */
	public boolean isParaIds(String localName, String attributeName) {
		// db5 and db4
		if ("xml:id".equals(attributeName) || "id".equals(attributeName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check if a attribute is a external link
	 * 
	 * @param localName
	 * @param attributeName localNameOfAtribute to check
	 * @return <code>true</code> if it's a external link
	 */
	public boolean isExternalLink(String localName, String attributeName) {
		boolean found = false;
		// db5
		// link tag
		if ("link".equals(localName) && "href".equals(attributeName)) {
			found = true;
		}

		// db4
		if ("ulink".equals(localName) && "url".equals(attributeName)) {
			found = true;
		}
		return found;
	}

	/**
	 * Check if a attribute is a image link
	 * 
	 * @param localName
	 * @param attributeName localNameOfAtribute to check
	 * @return <code>true</code> if it's a image link
	 */
	public boolean isImgLink(String localName, String attributeName) {
		boolean found = false;
		// db5
		if ("imagedata".equals(localName) && "fileref".equals(attributeName)) {
			found = true;
		}

		// db4
		if (("inlinegraphic".equals(localName) || "graphic".equals(localName)) && "fileref".equals(attributeName)) {
			found = true;
		}

		return found;
	}

	/**
	 * Check if a attribute is a internal link
	 * 
	 * @param localName
	 * @param attributeName localNameOfAtribute to check
	 * @return <code>true</code> if it's a internal link
	 */
	public boolean isInternalLink(String localName, String attribLocalName) {
		boolean found = false;
		if (("link".equals(localName) || "xref".equals(localName)) && "linkend".equals(attribLocalName)) {
			found = true;
		}

		if ("href".equals(attribLocalName) && "xref".equals(localName)) {
			found = true;
		}
		return found;
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
