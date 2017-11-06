package com.oxygenxml.docbook.checker.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

import org.apache.log4j.Logger;

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
	 * The type of link.
	 */
	private LinkType linkType;
	
	/**
	 * The URL of the parsed document.
	 */
	private URL startDocumentURL;

	/**
	 * The location according to start document.
	 */
	private Stack<URL> locationStack;
	
	/**
	 * Location(line) of the reference.
	 */
	private int line;

	/**
	 * Location(column) of the reference.
	 */
	private int column;

	/**
	 * Logger
	 */
	 private static final Logger logger = Logger.getLogger(Link.class);

	/**
	 * Constructor
	 * 
	 * @param ref The reference found.
	 * @param linkType The type of link.
	 * @param documentUrl The documentUrl
	 * @param locationStack Stack with location of the link according to start document.
	 * @param line 	The number of line.
	 * @param column The number of column.
	 */
	public Link(String ref, LinkType linkType, URL documentUrl, Stack<URL> locationStack, int line, int column) {
		this.ref = ref;
		this.linkType = linkType;
		this.startDocumentURL = documentUrl;
		this.locationStack = locationStack;
		this.line = line;
		this.column = column;
	}

	// Getters
	public String getRef() {
		return ref;
	}

	public LinkType getLinkType(){
		return linkType;
	}
	
	public URL getStartDocumentURL() {
		return startDocumentURL;
	}
	

	public Stack<URL> getLocationStack(){
		return locationStack;
	}
 
	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	/**
	 * Get the URL of document where link was found.
	 * @return The URL
	 */
	public URL getDocumentURL() {
		return locationStack.peek();
	}

	
	/**
	 * Get absolute location
	 * 
	 * @return the absolutLocation.
	 */
	public URL getAbsoluteLocation() {
		URL toReturn = null;
		if(!ref.isEmpty()){
			try {
				toReturn = new URL(ref);
			} catch (MalformedURLException e) {
				try {
					toReturn = new URL(getDocumentURL(), ref);
				} catch (MalformedURLException e2) {
					logger.debug(e2.getMessage(), e2);
				}
			}
		}
		return toReturn;
	}

	
	@Override
	public boolean equals(Object obj) {
		boolean toReturn; 
		if(obj instanceof Link){
			Link link = (Link) obj;
			toReturn =  this.ref.equals(link.getRef());
		}
		else{
			toReturn = super.equals(obj);
		}
		return toReturn;
	}

	@Override
	public int hashCode() {
		return ref.hashCode();
	}

	
}
