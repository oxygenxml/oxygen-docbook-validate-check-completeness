package com.oxygenxml.docbookChecker.checkbox.tree;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Used for convert a map that has value in <code>String</code> format in a map with value in <code>ConditionValue</code> format. 
 * @author intern4
 *
 */
public class MapConvertor {
	/**
	 * Convert a given map that has value in <code>String</code> format in a map with value in <code>ConditionValue</code> format.
	 * @param toConvert
	 * @return
	 */
	public static LinkedHashMap<String, LinkedHashSet<ConditionValue>> convert(LinkedHashMap<String, LinkedHashSet<String>> toConvert){
		LinkedHashMap<String, LinkedHashSet<ConditionValue>> toReturn = new LinkedHashMap<String, LinkedHashSet<ConditionValue>>();
		
		Iterator<String> iterKey = toConvert.keySet().iterator();
		while(iterKey.hasNext()){
			String attribute = iterKey.next();
			
			LinkedHashSet<ConditionValue> valueSet = new LinkedHashSet<ConditionValue>();
			
			Iterator<String> iterValue = toConvert.get(attribute).iterator();
			while(iterValue.hasNext()){
				valueSet.add(new ConditionValue(attribute, iterValue.next()));
			}
			
			toReturn.put(attribute, valueSet);
			
		}
		
		return toReturn;
	}
}
