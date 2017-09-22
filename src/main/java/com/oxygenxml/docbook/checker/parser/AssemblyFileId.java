package com.oxygenxml.docbook.checker.parser;
/**
 * Assembly Id with file found
 * @author intern4
 *
 */
public class AssemblyFileId {
	/**
	 * The id of file found.
	 */
	private String id;
	
	/**
	 * The file name found. 
	 */
	private String name;
	
	/**
	 * If the id is filter by conditions.
	 */
	private boolean isFilterByConditions = false;
	/**
	 * Constructor
	 * 
	 * @param id The id.
	 * @param name The name of file.
	 * @param isFilter <code>true</code> if it's filter, <code>false</code> otherwise.
	 */
	public AssemblyFileId(String id, String name, boolean isFilter) {
		this.id = id;
		this.name = name;
		this.isFilterByConditions = isFilter;
	}

	// Getters
	public String getId() {
		return id;
	}

	public String getName(){
		return name;
	}

	/**
	 * Return if the Id is filter by conditions
	 * @return <code>true</code> if it's filter, <code>false</code> otherwise.
	 */
	public boolean isFilterByConditions() {
		return isFilterByConditions;
	}


}
