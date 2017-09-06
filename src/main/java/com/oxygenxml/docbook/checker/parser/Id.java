package com.oxygenxml.docbook.checker.parser;

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
	
	private boolean isFilterByConditions = false;
	/**
	 * Constructor
	 * 
	 * @param id The id.
	 * @param documentUrl The documentUrl
	 * @param line 	The number of line.
	 * @param column The number of column.
	 * @param isFilter <code>true</code> if it's filter, <code>false</code> otherwise.
	 */
	public Id(String id, String documentUrl, int line, int column, boolean isFilter) {
		this.id = id;
		this.documentUrl = documentUrl;
		this.line = line;
		this.column = column;
		this.isFilterByConditions = isFilter;
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

	/**
	 * Return if the Id is filter by conditions
	 * @return <code>true</code> if it's filter, <code>false</code> otherwise.
	 */
	public boolean isFilterByConditions() {
		return isFilterByConditions;
	}


}
