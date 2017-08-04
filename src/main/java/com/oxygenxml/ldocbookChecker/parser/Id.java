package com.oxygenxml.ldocbookChecker.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Id {
	

	/**
	 * The id founded.
	 */
	private String id;

	/**
	 * The URL of the parsed document.
	 */
	private String documentUrl;

	/**
	 * Location(line) of the id.
	 */
	private int line;

	/**
	 * Location(column) of the id.
	 */
	private int column;

	private Map<String, Set<String>> conditions = new HashMap<String, Set<String>>();
	
	private boolean isFilterByConditions = false;
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param documentUrl
	 * @param line
	 * @param column
	 */
	public Id(String id, String documentUrl, int line, int column) {
		this.id = id;
		this.documentUrl = documentUrl;
		this.line = line;
		this.column = column;
	}

	// Getters
	public String getId() {
		return id;
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

	public void addConditions(Collection<Map<String, Set<String>>> collection){
		Iterator<Map<String, Set<String>>> i = collection.iterator();
		while (i.hasNext())
		  {
			Map<String, Set<String>> conditions = (Map<String, Set<String>>) i.next();
			this.conditions.putAll(conditions);
		  }
	}
	
	public boolean isFilterByConditions() {
		return isFilterByConditions;
	}

	public void setFilterByConditions(boolean isFilterByConditions) {
		this.isFilterByConditions = isFilterByConditions;
	}

	public Map<String, Set<String>> getConditions(){
		return conditions;
	}

	@Override
	public String toString() {
		return "Id [id=" + id + ", documentUrl=" + documentUrl + ", line=" + line + ", column=" + column + ", conditions="
				+ conditions + ", isFilterByConditions=" + isFilterByConditions + "]";
	}

	
	/**
	 * Test if ID is filter.
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
	
	


}
