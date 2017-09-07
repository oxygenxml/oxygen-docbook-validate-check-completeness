package com.oxygenxml.docbook.checker.parser;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Link found
 * 
 * @author intern4
 *
 */
public class Link {

	/**
	 * The reference found.
	 */
	private String ref;

	/**
	 * The URL of the parsed document.
	 */
	private String documentUrl;

	/**
	 * The URL of the document when link was found.
	 */
	private String linkFoundDocumentUrl;

	
	/**
	 * Location(line) of the reference.
	 */
	private int line;

	/**
	 * Location(column) of the reference.
	 */
	private int column;


	/**
	 * Constructor
	 * 
	 * @param ref The reference found.
	 * @param documentUrl The documentUrl
	 * @param documentLinkFound The document URL where link was found.
	 * @param line 	The number of line.
	 * @param column The number of column.
	 */
	public Link(String ref, String documentUrl, String documentLinkFound, int line, int column) {
		this.ref = ref;
		this.documentUrl = documentUrl;
		this.linkFoundDocumentUrl = documentLinkFound;
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
				toReturn = new URL(new URL(documentUrl), ref);
			} catch (MalformedURLException e2) {
			
			}
		}
		return toReturn;
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
