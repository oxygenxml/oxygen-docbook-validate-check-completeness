package com.oxygenxml.docbook.checker.parser;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * Assembled file(topic) with Id
 * @author Cosmin Duna
 *
 */
public class AssemblyTopicId {
	/**
	 * The id of file found.
	 */
	private String id;
	
	/**
	 * The reference of file found. 
	 */
	private String ref;
	
	/**
	 * Location(line) of the reference.
	 */
	private int line;

	/**
	 * Location(column) of the reference.
	 */
	private int column;
	
	/**
	 * The URL of the document when link was found.
	 */
	private String linkFoundDocumentUrl;
	
	/**
	 * If the id is filter by conditions.
	 */
	private boolean isFilterByConditions = false;
	
	/**
	 * Logger
	 */
	 private static final Logger logger = Logger.getLogger(AssemblyTopicId.class);
	
	/**
	 * Constructor
	 * 
	 * @param id The id.
	 * @param ref The name of file.
	 * @param documentLinkFound The document URL where the assembly file was found.
	 * @param isFilter <code>true</code> if it's filter, <code>false</code> otherwise.
	 * @param line 	The number of line.
	 * @param column The number of column.
	 */
	public AssemblyTopicId(String id, String ref, String documentLinkFound, boolean isFilter, int line, int column) {
		this.id = id;
		this.ref = ref;
		this.linkFoundDocumentUrl = documentLinkFound;
		this.isFilterByConditions = isFilter;
		this.line = line;
		this.column = column;
	}

	// Getters
	public String getId() {
		return id;
	}

	public String getName(){
		return ref;
	}
	
	public String getLinkFoundDocumentUrl() {
		return linkFoundDocumentUrl;
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

	/**
	 * Get absolute location
	 * 
	 * @return the absolutLocation.
	 */
	public URL getAbsoluteLocation() {
		URL toReturn = null;

		try {
			toReturn = new URL(ref);
		} catch (MalformedURLException e) {
			try {
				toReturn = new URL(new URL(linkFoundDocumentUrl), ref);
			} catch (MalformedURLException e2) {
				logger.debug(e2.getMessage(), e2);
			}
		}
		return toReturn;
	}

}
