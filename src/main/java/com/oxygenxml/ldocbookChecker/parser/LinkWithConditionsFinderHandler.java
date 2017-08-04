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
import com.oxygenxml.profiling.ProfileConditionsFinder;
import com.oxygenxml.profiling.ProfilingInformation;

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
	
	Map<String, Set<String>> conditionsFromGui ;

	private final String namespace = "http://www.w3.org/1999/xlink";

	// save tag and conditions before id attribute
	private Map<String, Map<String, Set<String>>> conditions = new HashMap<String, Map<String, Set<String>>>();

	/**
	 * Constructor
	 * 
	 * @param url
	 *          the parsed url
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public LinkWithConditionsFinderHandler(ParserCreator parserCreator, CheckerInteractor interactor)
			throws ParserConfigurationException, SAXException, IOException {
		this.interactor = interactor;
		
	// conditions for view;
			conditionsFromGui = interactor.getConditionsTableRows();
	}

	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (interactor.isSelectedCheckExternal()) {
			findExternalLink(localName, attributes);
		}

		if (interactor.isSelectedCheckImages()) {
			findImgLink(localName, attributes);
		}

		if (interactor.isSelectedCheckInternal()) {
			findParaIdsWithConditions(localName, attributes);
			findInternalLink(localName, attributes);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		// remove conditions for localName tag when this is closing.
		conditions.remove(localName);
	}

	/**
	 * Find paragraph IDs and their conditions.
	 * 
	 * @param localName
	 * @param qName
	 * @param attributes
	 */
	private void findParaIdsWithConditions(String localName, Attributes attributes) {
		// attribute localName
		String attribLocalName = "";

		// value of ID attribute
		String atributeIDValue = "";

		// line number of ID attribute
		int idLine = 0;

		// column number of ID attribute
		int idColumn = 0;

		int i = 0;

		// id attribute was found in this tag
		boolean idFound = false;


		// conditions on this tag
		Map<String, Set<String>> tagConditions = new HashMap<String, Set<String>>();

		// local name of attribute with index 0
		attribLocalName = attributes.getLocalName(i);

		while (attribLocalName != null) {
			// check if attribute is a profile condition
			if (conditionsFromGui.keySet().contains(attribLocalName)) {

				// value
				String[] value = attributes.getValue(i).split(";");
				Set<String> valueSet = new HashSet<String>();

				for (int j = 0; j < value.length; j++) {
						valueSet.add(value[j]);
				}

					// add condition in tagConditions map
					tagConditions.put(attribLocalName, valueSet);
			}

			// check if attribute is a ID
			if ("xml:id".equals(attribLocalName) || "id".equals(attribLocalName)) {
				idFound = true;
				atributeIDValue = attributes.getValue(i);
				idLine = locator.getLineNumber();
				idColumn = locator.getColumnNumber();
			}

			// get local name of next attribute
			i++;
			attribLocalName = attributes.getLocalName(i);
		}

		if (!tagConditions.isEmpty()) {
			conditions.put(localName, tagConditions);
		}

		if (idFound) {
			// add new ID in IDsSet
			Id newId = new Id(atributeIDValue, locator.getSystemId(), idLine, idColumn);
			newId.addConditions(conditions.values());
			System.out.println(newId.toString());
			paraIdsSet.add(newId);
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
				externalLinksSet
						.add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		// db4
		if ("ulink".equals(localName)) {
			atributeVal = attributes.getValue("url");

			if (atributeVal != null) {
				// add new Link in linksSet
				externalLinksSet
						.add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
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
				imgLinksSet
						.add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		// db4
		if ("inlinegraphic".equals(localName) || "graphic".equals(localName)) {
			String atributeVal = attributes.getValue("fileref");

			if (atributeVal != null) {
				// add new Link in linksSet
				imgLinksSet
						.add(new Link(atributeVal, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()));
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
		// attribute localName
		String attribLocalName = "";

		// value of ID attribute
		String atributeIDValue = "";

		// line number of ID attribute
		int idLine = 0;

		// column number of ID attribute
		int idColumn = 0;

		int i = 0;

		// internal link attribute was found in this tag
		boolean found = false;

		// conditions on this tag
		Map<String, Set<String>> tagConditions = new HashMap<String, Set<String>>();

		// local name of attribute with index 0
		attribLocalName = attributes.getLocalName(i);

		while (attribLocalName != null) {
			// check if attribute is a profile condition
			if (conditionsFromGui.keySet().contains(attribLocalName)) {

				// value
				String[] value = attributes.getValue(i).split(";");
				Set<String> valueSet = new HashSet<String>();

				for (int j = 0; j < value.length; j++) {
						valueSet.add(value[j]);
				}

					// add condition in tagConditions map
					tagConditions.put(attribLocalName, valueSet);
			}

			// check if attribute is a ID
			if ((("link".equals(localName) || "xref".equals(localName)) && "linkend".equals(attribLocalName))
					|| ("href".equals(attribLocalName) && "xref".equals(localName))) {
				found = true;
				atributeIDValue = attributes.getValue(i);
				idLine = locator.getLineNumber();
				idColumn = locator.getColumnNumber();
			}

			// get local name of next attribute
			i++;
			attribLocalName = attributes.getLocalName(i);
		}

		if (!tagConditions.isEmpty()) {
			conditions.put(localName, tagConditions);
		}

		if (found) {
			// add new ID in IDsSet
			Link newLink = new Link(atributeIDValue, locator.getSystemId(), idLine, idColumn);
			newLink.addConditions(conditions.values());
			System.out.println(newLink.toString());
			internalLinksSet.add(newLink);
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
