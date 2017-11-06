package com.oxygenxml.docbook.checker.persister;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ContentPersisterUtil {

  private ContentPersisterUtil() {
    throw new IllegalStateException("Utility class");
  }
	
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

	/**
	 * Returns a string containing the string representation of each of parts, using the given separator between each.
	 * @param delimiter The separator.
	 * @param resurces 
	 * @return
	 */
	public static String join(String delimiter, List<URL> resurces) {
		StringBuilder sb = new StringBuilder();
		Iterator<URL> iterator = resurces.iterator();
		while (iterator.hasNext()) {
			URL cs = iterator.next();
			sb.append(cs.toString());
			if(iterator.hasNext()){
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}
}
