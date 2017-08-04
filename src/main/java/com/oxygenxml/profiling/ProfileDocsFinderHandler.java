package com.oxygenxml.profiling;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.oxygenxml.ldocbookChecker.parser.ParserCreator;

public class ProfileDocsFinderHandler extends DefaultHandler {

	private Map<String, Set<String>> profilingsMap = new HashMap<String, Set<String>>();
	private Set<String> conditionAttributesNames = new HashSet<String>();

	public ProfileDocsFinderHandler(ParserCreator parserCreator) throws ParserConfigurationException, SAXException, IOException {
		ProfilingInformation profilingInformation = new ProfileConditionsFinder(parserCreator);
		conditionAttributesNames = profilingInformation.getProfileConditionAttributesNames(ProfilingInformation.DOCBOOK);
	}

	@Override
	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
			throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		findProfiligAtribute(localName, attributes);
	}

	//find profile attributes 
	public void findProfiligAtribute(String localName, org.xml.sax.Attributes attributes) {
		String string = "";
		int i = 0;

		while (string != null) {
			string = attributes.getLocalName(i);
			// check if attribute is a profile
			if (conditionAttributesNames.contains(string)) {
				// key
				String key = attributes.getLocalName(i);
				System.out.println("localName: " + key);

				// value
				String[] value = attributes.getValue(i).split(";");
				System.out.println("value: " + value.toString());
				Set<String> valueSet = new HashSet<String>();
				for (int j = 0; j < value.length; j++) {
					valueSet.add(value[j]);
				}

				// check if key already exist
				if (profilingsMap.containsKey(key)) {
					valueSet.addAll(profilingsMap.get(key));
					profilingsMap.put(key, valueSet);

				} else {
					profilingsMap.put(key, valueSet);
				}
			}

			i++;
		}

	}

	public Map<String, Set<String>> getProfilingMap() {
		return profilingsMap;
	}
}
