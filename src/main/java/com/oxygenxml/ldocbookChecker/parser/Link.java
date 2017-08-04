package com.oxygenxml.ldocbookChecker.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Link found
 * 
 * @author intern4
 *
 */
public class Link {

	/**
	 * The reference founded.
	 */
	private String ref;

	/**
	 * The URL of the parsed document.
	 */
	private String documentUrl;

	/**
	 * Location(line) of the reference.
	 */
	private int line;

	/**
	 * Location(column) of the reference.
	 */
	private int column;

	private Exception exception;
	
	private LinkType type;
	
	private Map<String, Set<String>> conditions = new HashMap<String, Set<String>>();

	/**
	 * Constructor
	 * 
	 * @param ref
	 * @param documentUrl
	 * @param line
	 * @param column
	 */
	public Link(String ref, String documentUrl, int line, int column) {
		this.ref = ref;
		this.documentUrl = documentUrl;
		this.line = line;
		this.column = column;
	}

	// Getters
	public String getRef() {
		return ref;
	}

	public String getDocumentURL() {
		return documentUrl;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	public Map<String, Set<String>> getConditions(){
		return conditions;
	}
	
	public LinkType getType() {
		return type;
	}
	
	public Exception getException() {
		return exception;
	}

	public void addConditions(Collection<Map<String, Set<String>>> collection){
		Iterator<Map<String, Set<String>>> i = collection.iterator();
		while (i.hasNext())
		{
			Map<String, Set<String>> conditions = (Map<String, Set<String>>) i.next();
			this.conditions.putAll(conditions);
		}
	}
	
	public void setException(Exception exception) {
		this.exception = exception;
	}


	public void setType(LinkType type) {
		this.type = type;
	}

	/**
	 * Get absolute location
	 * 
	 * @return the url
	 */
	public URL getAbsoluteLocation() {
		URL toReturn = null;

		try {
			toReturn = new URL(ref);
		} catch (MalformedURLException e) {

			try {
				toReturn = new URL(new URL(documentUrl), ref);
			} catch (MalformedURLException e2) {
				// return null
			}
		}
		return toReturn;
	}

	
	/**
	 * Test if link is filter.
	 * 
	 * @param guiConditions Map with conditions from GUI.
	 * @return
	 */
	public boolean isFilter(Map<String, Set<String>> guiConditions) {

			Iterator<String> iterKeyRight = conditions.keySet().iterator();
			
			// iterate by keys in link conditions 
			while (iterKeyRight.hasNext()) {
				String tagKey = (String) iterKeyRight.next();

				if (guiConditions.containsKey(tagKey)) {
					Iterator<String> iterSetTag = conditions.get(tagKey).iterator();
					boolean isFilter = true;

					// iterate by value in set values of currentKey 
					while (iterSetTag.hasNext()) {
						String valueTag = iterSetTag.next();

						// the element is not filter by this key if exists value declared in GUI.
						if (guiConditions.get(tagKey).contains(valueTag)) {
							isFilter = false;
						}
					}

					if (isFilter) {
						// the element is filter by this current key because wasn't found a correspondent value in GUI.
						// element is filter
						return true;
					}
				}

			}
		
		//element is not filter
		return false;
	}
	
	
	@Override
	public String toString() {
		return "Link [ref=" + ref + ", documentUrl=" + documentUrl + ", line=" + line + ", column=" + column
				+ ", exception=" + exception + ", type=" + type + ", conditions=" + conditions.toString() + "]";
	}

	@Override
	public boolean equals(Object obj) {
			Link link = (Link) obj;
			return this.ref.equals(link.getRef());
	}

	@Override
	public int hashCode() {
		return ref.hashCode();
	}
}
