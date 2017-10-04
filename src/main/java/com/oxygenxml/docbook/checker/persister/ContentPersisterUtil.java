package com.oxygenxml.docbook.checker.persister;

import java.util.Collection;
import java.util.Iterator;

public class ContentPersisterUtil {

	/**
	 * Returns a string containing the string representation of each of parts, using the given separator between each.
	 * @param delimiter The separator.
	 * @param resurces 
	 * @return
	 */
	public static String join(String delimiter, Collection<String> resurces) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> iterator = resurces.iterator();
		while (iterator.hasNext()) {
			String cs = iterator.next();
			sb.append(cs);
			if(iterator.hasNext()){
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}
}
