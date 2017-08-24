package com.oxygenxml.profiling;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Detect profile conditions on a element.
 * 
 * @author intern4
 *
 */
public class AllConditionsDetector {

	private Set<String> definedAttributesNames;

	private LinkedHashMap<String, LinkedHashSet<String>> allConditions = new LinkedHashMap<String, LinkedHashSet<String>>();
	
	public AllConditionsDetector(ProfilingConditionsInformations profilingInformation) {
		definedAttributesNames = profilingInformation.getProfileConditionAttributesNames(ProfilingConditionsInformations.ALL_DOCBOOKS);
	}

	/**
	 * Detect profile conditions when element start.
	 * 
	 * @param localName
	 *          Local name of element.
	 * @param attributes
	 *          Attributes of element.
	 * @param allowedConditions
	 *          Allowed conditions to be detected
	 * @param conditionsStack
	 *          Stack to store found conditions
	 */
	public void startElement(String localName, org.xml.sax.Attributes attributes) {

			// attribute localName
			String attribLocalName = "";
			
			for(int i = 0; i< attributes.getLength(); i++){
				
			// local name of attribute with index i
			 attribLocalName = attributes.getLocalName(i);
				// check if attribute is a allowed profile condition
				if (definedAttributesNames.contains(attribLocalName)) {
					
					// take value
					String[] value = attributes.getValue(i).split(";");
					
					LinkedHashSet<String> setValues = new LinkedHashSet<String>();
					

					for (int j = 0; j < value.length; j++) {
						setValues.add(value[j]);
					}

					// check if key already exist
					if (allConditions.containsKey(attribLocalName)) {
						setValues.addAll(allConditions.get(attribLocalName));
						allConditions.put(attribLocalName, setValues);

					} else {
						allConditions.put(attribLocalName, setValues);
					}
					
				}
			}
	}


	public  LinkedHashMap<String, LinkedHashSet<String>> getAllConditionFromDocument(){
		return allConditions;
	}

}
