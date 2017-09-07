package com.oxygenxml.profiling;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.xml.sax.Locator;

import com.oxygenxml.docbook.checker.parser.ConditionDetails;


/**
 * Detect profile conditions on a element.
 * 
 * @author intern4
 *
 */
public class AllConditionsDetector {

	/**
	 * Attributes names of conditions defined in preferences
	 */
	private Set<String> definedAttributesNames;

	/**
	 * List with founded conditions
	 */
	private LinkedHashMap<String, LinkedHashSet<String>> allConditions = new LinkedHashMap<String, LinkedHashSet<String>>();
	
	/**
	 * Set with Condition with details
	 * 
	 */
	private LinkedHashSet<ConditionDetails> allConditionsWithDetails= new LinkedHashSet<ConditionDetails>();
	
	/**
	 * Constructor
	 * @param definedAttributesNames Defined conditions attributes names. 
	 */
	public AllConditionsDetector(Set<String> definedAttributesNames) {
		this.definedAttributesNames = definedAttributesNames;
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
	 * @param locator		The locator or element.
	 */
	public void startElement(String localName, org.xml.sax.Attributes attributes, Locator locator) {

			// attribute localName
			String attribLocalName = "";
			
			int size = attributes.getLength();
			for(int i = 0; i< size; i++){
				
			// local name of attribute with index i
			 attribLocalName = attributes.getLocalName(i);
				// check if attribute is a allowed profile condition
				if (definedAttributesNames.contains(attribLocalName)) {
					
					// take value
					String[] value = attributes.getValue(i).split(";");
					
					LinkedHashSet<String> setValues = new LinkedHashSet<String>();
					

					for (int j = 0; j < value.length; j++) {
						setValues.add(value[j]);
						if(locator != null){
							allConditionsWithDetails.add(new ConditionDetails(attribLocalName, value[j], locator.getLineNumber(),
									locator.getColumnNumber() , locator.getSystemId()) ) ;
						}
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
 
	
	/**
	 * Return all Conditions from document.
	 * @return A map with conditions.
	 */
	public  LinkedHashMap<String, LinkedHashSet<String>> getAllConditionFromDocument(){
		return allConditions;
	}

	/**
	 * Return all conditions from document with details
	 * @return A Set with conditions in {@link ConditionDetails} format.
	 */
	public LinkedHashSet<ConditionDetails> getAllConditionsWithDetailsFromDocument(){
	 return allConditionsWithDetails;
	}
}
