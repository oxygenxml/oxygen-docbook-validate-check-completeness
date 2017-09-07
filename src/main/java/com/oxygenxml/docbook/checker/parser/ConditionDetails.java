package com.oxygenxml.docbook.checker.parser;

/**
 * Found condition with details.
 * @author intern4
 *
 */
public class ConditionDetails {
	
	/**
	 * condition attribute.
	 */
	private String attribute; 
	
	/**
	 * condition value
	 */
	private String value;

	/**
	 * Location(line) of the condition.
	 */
	private int line;

	/**
	 * Location(column) of the condition.
	 */
	private int column;
	
	/**
	 * The URL of the document that contains the condition.
	 */
	private String documentUrl;

	/**
	 * Constructor
	 * @param attribute Condition attribute
	 * @param value		Condition value
	 * @param line	Location(line) of the condition
	 * @param column Location(column) of the condition.
	 * @param documentUrl The URL of the document that contains the condition
	 */
	public ConditionDetails(String attribute, String value, int line, int column, String documentUrl) {
		this.attribute = attribute;
		this.value = value;
		this.line = line;
		this.column = column;
		this.documentUrl = documentUrl;
	}

	
	//----Getters
	public String getAttribute() {
		return attribute;
	}

	public String getValue() {
		return value;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	
	@Override
	public int hashCode() {
		return (attribute+value+line+column+documentUrl).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return ( value.equals(((ConditionDetails)obj).value) &&  attribute.equals(((ConditionDetails)obj).attribute) 
				&& documentUrl.equals(((ConditionDetails)obj).documentUrl) && (line == ((ConditionDetails)obj).line) 
				&& (column == ((ConditionDetails)obj).column));
	}
	
	
	
}
