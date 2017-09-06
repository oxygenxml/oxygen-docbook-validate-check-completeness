package com.oxygenxml.docbook.checker.checkboxtree;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Used for convert a map that has value in <code>String</code> format in a map with value in <code>ConditionValue</code> format. 
 * @author intern4
 *
 */
public class ConditionValueUtil {
	/**
	 * Convert a given map that has value in <code>String</code> format in a map with value in <code>ConditionValue</code> format.
	 * @param toConvert The map to be converted.
	 * @return The converted map.
	 */
	public static LinkedHashMap<String, LinkedHashSet<LeafNode>> convert(LinkedHashMap<String, LinkedHashSet<String>> toConvert)
	{
		//map to return
		LinkedHashMap<String, LinkedHashSet<LeafNode>> toReturn = new LinkedHashMap<String, LinkedHashSet<LeafNode>>();
	
		//Iterate over keys
		Iterator<String> iterKey = toConvert.keySet().iterator();
		while(iterKey.hasNext()){
			String attribute = iterKey.next();
			
			LinkedHashSet<LeafNode> valueSet = new LinkedHashSet<LeafNode>();
			
			//Iterate over value of current key
			Iterator<String> iterValue = toConvert.get(attribute).iterator();
			while(iterValue.hasNext()){
				valueSet.add(new LeafNode(attribute, iterValue.next()));
			}
			
			toReturn.put(attribute, valueSet);
			
		}
		
		return toReturn;
	}
}
