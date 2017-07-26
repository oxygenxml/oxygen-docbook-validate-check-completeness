package com.oxygenxml.ldocbookChecker.parser;

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

	
	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public LinkType getType() {
		return type;
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

	@Override
	public String toString() {
		return "Link [ref=" + ref + ", documentUrl=" + documentUrl + ", line=" + line + ", column=" + column + "]";
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
