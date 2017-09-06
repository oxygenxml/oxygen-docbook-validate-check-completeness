package com.oxygenxml.profiling;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Interface for access oxygen profile conditions.
 * @author intern4
 *
 */
public interface ProfilingConditionsInformations {

	/**
	 *  Docbook4 or Docbook5 document types(common part).
	 */
	public String DOCBOOK = "DocBook*";
	/**
	 * Docbook4 document type.
	 */
	public String DOCBOOK4 = "DocBook 4";
	
	/**
	 * Docbook5 document type.
	 */
	public String DOCBOOK5 = "DocBook 5";
	
	
	/**
	 *Get all profiling conditional attributes names. 
	 * @param documentType  the type of xml document: 
	 * ProfilingInformation.DOCBOOK4, ProfilingInformation.DOCBOOK5 or other.
	 *
	 * @return a Set with attributes names.
	 */
	public Set<String> getProfileConditionAttributesNames(String documentType);
	
	/**
	 *Get all profile conditions(attribute name and values). 
	 * @param documentType  the type of xml document: 
	 * ProfilingInformation.DOCBOOK4, ProfilingInformation.DOCBOOK5 or other.
	 *
	 * @return a Map with attribute name(key) and set with values(value).
	 */
	public LinkedHashMap<String, LinkedHashSet<String>> getProfileConditions(String documentType);	
	/**
	 * Get all existence conditions sets
	 * @param documentType  the type of xml document: 
	 * ProfilingInformation.DOCBOOK4, ProfilingInformation.DOCBOOK5 or other.
 	 * @return the list of sets
	 */
	public LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> getConditionsSets(String documentType);

	/**
	 * Get profile conditions from the documents linked at the given URLs according to given document type.
	 * @param url The URL.
	 * @param docType The document type.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public LinkedHashMap<String, LinkedHashSet<String>> getConditionsFromDocs(String url, String docType) throws ParserConfigurationException, SAXException, IOException; 


	/**
	 * Get all existence condition sets names.
	 * @param documentType  the type of xml document: 
	 * ProfilingInformation.DOCBOOK4, ProfilingInformation.DOCBOOK5 or other.
	 * @return A set with names.
	 */
	public Set<String> getConditionSetsNames(String documentType);

}
