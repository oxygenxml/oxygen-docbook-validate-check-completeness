package com.oxygenxml.docbook.checker.persister;

import java.util.Collection;
import java.util.StringJoiner;

public class ContentPersisterUtil {

	/**
	 * Returns a string containing the string representation of each of parts, using the given separator between each.
	 * @param delimiter The separator.
	 * @param resurces 
	 * @return
	 */
	public static String join(String delimiter, Collection<String> resurces) {
		StringJoiner joiner = new StringJoiner(delimiter);
		for (CharSequence cs : resurces) {
			joiner.add(cs);
		}
		return joiner.toString();
	}
}
