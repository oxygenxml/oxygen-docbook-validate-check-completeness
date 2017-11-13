package com.oxygenxml.profiling;

/**
 * The DocBook types
 * @author Cosmin Duna
 *
 */
public class DocBookTypes {

	/**
	 * Private constructor.
	 */
	private DocBookTypes() {
		throw new IllegalStateException("Utility class");
	}
	
	/**
	 *  Docbook4 or Docbook5 document types(common part).
	 */
	public static final String DOCBOOK = "DocBook*";
	/**
	 * Docbook4 document type.
	 */
	public static final String DOCBOOK4 = "DocBook 4";
	
	/**
	 * Docbook5 document type.
	 */
	public static final String DOCBOOK5 = "DocBook 5";
}
