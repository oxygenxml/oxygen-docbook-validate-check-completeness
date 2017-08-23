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
	 * @param id
	 * @param documentUrl
	 * @param line
	 * @param column
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

	
	public boolean isFilterByConditions() {
		return isFilterByConditions;
	}

	public void setFilterByConditions(boolean isFilterByConditions) {
		this.isFilterByConditions = isFilterByConditions;
	}


	@Override
	public String toString() {
		return "Id [id=" + id + ", documentUrl=" + documentUrl + ", line=" + line + ", column=" + column  + ", isFilterByConditions=" + isFilterByConditions + "]";
	}
	

}
