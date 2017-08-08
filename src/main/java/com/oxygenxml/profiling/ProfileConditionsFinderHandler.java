package com.oxygenxml.profiling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler for find conditions.
 * @author intern4
 *
 */
public class ProfileConditionsFinderHandler extends DefaultHandler {
	private Map<String, Set<String>> toReturn = new HashMap<String, Set<String>>();
	
	private String type;
	private String attributeName;
	private String readType;
	private Set<String> value = new HashSet<String>();
	
	
	private boolean bConditionInfo = false;
	private boolean bNameNextTag = false;
	private boolean bTypeNextTag = false;
	private boolean bValueNextTag = false;
	private boolean bReadName = false;
	private boolean bReadType = false;
	private boolean bReadValue = false;

	public ProfileConditionsFinderHandler(String documentType) {
		type = documentType;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws org.xml.sax.SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (bNameNextTag && "String".equals(localName)) {
			bNameNextTag = false;
			bReadName = true;
		} else if (bTypeNextTag && "String".equals(localName)) {
			bTypeNextTag = false;
			bReadType = true;
		} else if (bValueNextTag && "String".equals(localName)) {
			bValueNextTag = false;
			bReadValue = true;
		} else {
			if ("profileConditionInfo".equals(localName)) {
				bConditionInfo = true;
			}
			if (bConditionInfo && "attributeName".equals(attributes.getValue("name"))) {
				bNameNextTag = true;
			}
			if (bConditionInfo && "value".equals(attributes.getValue("name"))) {
				bValueNextTag = true;
			}
			if (!type.equals(ProfilingInformation.ALLTYPES) && bConditionInfo
					&& "documentTypePattern".equals(attributes.getValue("name"))) {
				bTypeNextTag = true;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		if ("profileConditionInfo".equals(localName)) {
			if(type.equals(ProfilingInformation.ALLTYPES) || readType.contains(type) ){
				System.out.println("\nadauga**********val: " + value );
				System.out.println(toReturn.put(attributeName, value));
			}
			System.out.println("atrb: " + attributeName);
			System.out.println("value: " + value.toString());
			System.out.println("readType: " + readType);
			System.out.println("paraTyPE: "+ type);
			value = new HashSet<String>();
			bConditionInfo = false;
			bNameNextTag = false;
			bReadName = false;
			bReadValue = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		if (bReadName) {
			attributeName = new String(ch, start, length);
			bReadName = false;
		
		} else if (bReadType) {
			readType = new String(ch, start, length);
			bReadType = false;
		
		} else if (bReadValue) {
			value.add(new String(ch, start, length));
			bReadValue = false;
		}
	}

	public Map<String, Set<String>> getResuts() {
		return toReturn;
	}

}
