package com.oxygenxml.profiling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ProfileConditionsSetsFinderHandler extends DefaultHandler {
	private Map<String, Set<String>> conditionsSet = new HashMap<String, Set<String>>(); 
	private Map<String, Map<String, Set<String>> > toReturn = new HashMap<String, Map<String, Set<String>>>();

	private String type;
	private String setName; 
	private String attributeName;
	private String readType;
	private Set<String> value = new HashSet<String>();

	private boolean bConditionsSetInfo = false;
	private boolean bNameNextTag = false;
	private boolean bTypeNextTag = false;
	private boolean bValueNextTag = false;
	private boolean bSetNameNextTag = false;
	
	private boolean bReadName = false;
	private boolean bReadSetName = false;
	private boolean bReadType = false;
	private boolean bReadValue = false;

	public ProfileConditionsSetsFinderHandler(String documentType) {
			type = documentType;
		}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws org.xml.sax.SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (bTypeNextTag) {
			bTypeNextTag = false;
			bReadType = true;
		} 
		else if(bNameNextTag) {
			bNameNextTag = false;
			bReadName = true;
		} 
		else if (bValueNextTag ) {
			bReadValue = true;
		}
		else if(bSetNameNextTag){
			bReadSetName = true;
			bSetNameNextTag = false;
		}
		else {
			if ("profileConditionsSetInfo".equals(localName)) {
				bConditionsSetInfo = true;
			}
			if (bConditionsSetInfo && "entry".equals(localName) ) {
				bNameNextTag = true;
			}
			if (bConditionsSetInfo && "String-array".equals(localName) ) {
				bValueNextTag = true;
			}
			if(bConditionsSetInfo && "documentTypePattern".equals(attributes.getValue("name")) ){
				bTypeNextTag = true;
			}
			if(bConditionsSetInfo && "conditionSetName".equals(attributes.getValue("name"))){
				bSetNameNextTag = true;
			}
			
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		
		if(bValueNextTag && "String-array".equals(localName)){
			System.out.println("ADAUGA SET " + attributeName + " " + value.toString()  );
			conditionsSet.put(attributeName, value);
			value = new HashSet<String>();
			bValueNextTag = false;
		}
		
		if ("profileConditionsSetInfo".equals(localName)) {
			if (type.equals(ProfilingInformation.ALLTYPES) || readType.contains(type)) {
				System.out.println("\nadauga**********map: " + conditionsSet.toString());
				toReturn.put(setName, conditionsSet);
			}
			conditionsSet = new HashMap<String, Set<String>>();
			bConditionsSetInfo = false;
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
		
		}	else if(bReadSetName){
			setName = new String(ch, start, length);
			bReadSetName = false;
		}
		
	
	}

	public Map<String, Map<String, Set<String>> > getResuts() {
		return toReturn;
	}

}
