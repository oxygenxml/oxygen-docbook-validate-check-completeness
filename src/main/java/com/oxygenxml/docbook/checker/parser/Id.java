package com.oxygenxml.docbook.checker.parser;

public class Id {
	
	/**
	 * The id founded.
	 */
	private String id;
	
	/**
	 * If the id is filter by conditions.
	 */
	private boolean isFilterByConditions = false;
	/**
	 * Constructor
	 * 
	 * @param id The id.
	 * @param isFilter <code>true</code> if it's filter, <code>false</code> otherwise.
	 */
	public Id(String id, boolean isFilter) {
		this.id = id;
		this.isFilterByConditions = isFilter;
	}

	// Getters
	public String getId() {
		return id;
	}


	/**
	 * Return if the Id is filter by conditions
	 * @return <code>true</code> if it's filter, <code>false</code> otherwise.
	 */
	public boolean isFilterByConditions() {
		return isFilterByConditions;
	}


}
