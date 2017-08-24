package com.oxygenxml.docbook.checker.parser;

import java.util.HashSet;
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
public class ElementFilterDetector {

	private Stack<Boolean> filterByConditions = new Stack<Boolean>();

	private LinkedHashMap<String, LinkedHashSet<String>> allowedConditions;

	public ElementFilterDetector(LinkedHashMap<String, LinkedHashSet<String>> allowedConditions) {
		this.allowedConditions = allowedConditions;
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
	public Boolean startElement(String localName, org.xml.sax.Attributes attributes) {

		if (!filterByConditions.isEmpty() && filterByConditions.lastElement() == true) {
			filterByConditions.push(new Boolean(true));
			return true;
		} 
		else {
			// attribute localName
			String attribLocalName = "";
			
			for(int i = 0; i< attributes.getLength(); i++){
				
			// local name of attribute with index i
			 attribLocalName = attributes.getLocalName(i);

				// check if attribute is a allowed profile condition
				if (allowedConditions.keySet().contains(attribLocalName)) {
					
					// take value
					String[] value = attributes.getValue(i).split(";");
					
					boolean isFilter = true;

					for (int j = 0; j < value.length; j++) {
						if (allowedConditions.get(attribLocalName).contains(value[j])) {
							isFilter = false;
							break;
						}
					}

					if (isFilter) {
						filterByConditions.push(new Boolean(isFilter));
						return true;
					}
				}
			}

			// add elementConditions in stack
			filterByConditions.push(new Boolean(false));

			
			return false;
		}
	}

	/**
	 * Pop conditions from stack.
	 * 
	 * @param conditions
	 */
	public void endElement() {
		filterByConditions.pop();
	}


}
