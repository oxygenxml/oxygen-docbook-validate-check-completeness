package com.oxygenxml.ldocbookChecker.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Detect profile conditions on a element.
 * @author intern4
 *
 */
public class ElementConditionsDetector {
	 
	
	
	/**
	 * Detect profile conditions when element start.
	 * @param localName  Local name of element.
	 * @param attributes	Attributes of element.
	 * @param allowedConditions Allowed conditions to be detected
	 * @param conditionsStack Stack to store found conditions
	 */
		public void startElement(String localName, org.xml.sax.Attributes attributes,  Map<String, Set<String>> allowedConditions, Stack<Map<String, Set<String>>> conditionsStack) {
			
			// attribute localName
			String attribLocalName = "";

			int i = 0;

			// conditions on this element
			Map<String, Set<String>> elementConditions = new HashMap<String, Set<String>>();

			// local name of attribute with index 0
			attribLocalName = attributes.getLocalName(i);
			while (attribLocalName != null) {

				// check if attribute is a allowed profile condition
				if (allowedConditions.keySet().contains(attribLocalName)) {

					// take value
					String[] value = attributes.getValue(i).split(";");
					Set<String> valueSet = new HashSet<String>();
					
					for (int j = 0; j < value.length; j++) {
						valueSet.add(value[j]);
					}

					// add condition in elementConditions map
					elementConditions.put(attribLocalName, valueSet);
					
				} 
				// get local name of next attribute
				i++;
				attribLocalName = attributes.getLocalName(i);
			}

			// add elementConditions in stack
			conditionsStack.push(elementConditions);

		}
		
		
		/**
		 * Pop conditions from stack. 
		 * @param conditions
		 */
		public void endElement(Stack<Map<String, Set<String>>> conditions){
			conditions.pop();
		}

}
