package com.oxygenxml.profiling;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.oxygenxml.docbook.checker.parser.ParserCreator;
/**
 * Handler for find profile conditions from a document.
 * @author intern4
 *
 */
public class ProfileDocsFinderHandler extends DefaultHandler {

	private LinkedHashMap<String, LinkedHashSet<String>> profilingsMap = new LinkedHashMap<String, LinkedHashSet<String>>();
	private Set<String> conditionAttributesNames = new HashSet<String>();

	public ProfileDocsFinderHandler(ParserCreator parserCreator) throws ParserConfigurationException, SAXException, IOException {
		ProfilingConditionsInformations profilingInformation = new ProfilingConditionsInformationsImpl();
		conditionAttributesNames = profilingInformation.getProfileConditionAttributesNames(ProfilingConditionsInformations.ALL_DOCBOOKS);
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

				// value
				String[] value = attributes.getValue(i).split(";");
				LinkedHashSet<String> valueSet = new LinkedHashSet<String>();
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

	public LinkedHashMap<String, LinkedHashSet<String>> getProfilingMap() {
		return profilingsMap;
	}
}
