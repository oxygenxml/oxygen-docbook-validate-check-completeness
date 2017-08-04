package com.oxygenxml.profiling;

import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.events.EndElement;

import jdk.internal.org.xml.sax.SAXException;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ProfileConditionsAttributesNamesFinderHandler extends DefaultHandler {

	Set<String> toReturn = new HashSet<String>();
	String type;
	String attributeName;
	boolean bConditionInfo = false;
	boolean bNameNextTag = false;
	boolean bTypeNextTag = false;
	boolean bReadName = false;
	boolean bReadType = false;
	
	public ProfileConditionsAttributesNamesFinderHandler(String documentType) {
		type = documentType;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws org.xml.sax.SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (bNameNextTag && "String".equals(localName) ) {
			bNameNextTag = false;
			bReadName = true;	
		}
		else if ( bTypeNextTag && "String".equals(localName) ) {
			bTypeNextTag = false;
			bReadType = true;		
		} 
		else {
			if ("profileConditionInfo".equals(localName)) {
				bConditionInfo = true;
			}
			if (bConditionInfo && "attributeName".equals(attributes.getValue("name"))) {
				bNameNextTag = true;
			}
			if (!type.equals(ProfilingInformation.ALLTYPES) && bConditionInfo && "documentTypePattern".equals(attributes.getValue("name"))) {
				bTypeNextTag = true;
			}
		}
	}

	@Override
  public void endElement (String uri, String localName, String qName){
		if ("profileConditionInfo".equals(localName)) {
			bConditionInfo = false;
			bNameNextTag = false;
			bReadName = false;
		}
	}

	@Override
	public void characters (char ch[], int start, int length){
		if (bReadName) {
			attributeName = new String(ch, start, length);
			bReadName = false;
			if(type.equals(ProfilingInformation.ALLTYPES)){
				toReturn.add(attributeName);
			}
		}else if (bReadType) {
				String readType = new String(ch, start, length);
				if(readType.contains(type)){
					toReturn.add(attributeName);
				}
				bReadType = false;
				bConditionInfo = false;
		} 
	}
	
	public Set<String> getResuts(){
		return toReturn;
	}
}
