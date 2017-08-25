package com.oxygenxml.docbook.checker.parser;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Stack;

/**
 * Determine the state of a element (filter/ not filter).
 * 
 * @author intern4
 *
 */
public class ElementFilterDetector {

	/**
	 * Stack with elements state(filter/ not filter).
	 */
	private Stack<Boolean> filterByConditions = new Stack<Boolean>();

	/**
	 * Conditions allowed in document.
	 */
	private LinkedHashMap<String, LinkedHashSet<String>> allowedConditions;

	public ElementFilterDetector(LinkedHashMap<String, LinkedHashSet<String>> allowedConditions) {
		this.allowedConditions = allowedConditions;
	}

	/**
	 * Determine if element is filter by conditions.
	 * 
	 * @param localName
	 *          Local name of element.
	 * @param attributes
	 *          Attributes of element.
	 * @param allowedConditions
	 *          Allowed conditions to be detected
	 * @param conditionsStack
	 *          Stack to store found conditions
	 *@return <code>true</code> if element is filter, <code>false</code>otherwise
	 */
	public Boolean startElement(String localName, org.xml.sax.Attributes attributes) {

		// check if last element is filter
		if (!filterByConditions.isEmpty() && filterByConditions.lastElement() == true) {
		// add element state(filter) in stack
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

			// add element state in stack
			filterByConditions.push(new Boolean(false));

			
			return false;
		}
	}

	/**
	 * Pop element state from stack.
	 * 
	 * @param conditions
	 */
	public void endElement() {
		filterByConditions.pop();
	}


}
