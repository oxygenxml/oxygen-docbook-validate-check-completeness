package com.oxygenxml.profiling;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Interface for access oxygen profile conditions.
 * @author intern4
 *
 */
public interface ProfilingInformation {
	
	/**
	 * Type of information accessed.
	 */
	public String DOCS_TO_CHECK = "Docs_to_check";
	
	/**
	 * Type of information accessed.
	 */
	public String ATTRIBUTES = "Attributes";
	/**
	 * Type of information accessed.
	 */
	public String CONDITIONS = "Conditions";
	/**
	 * Type of information accessed.
	 */
	public String CONDITIONS_SETS = "ConditionsSets";
	
	/**
	 * Dita document xml type.
	 */
	public String DITA = "*DITA*";
	
	/**
	 * Docbook4 and Docbook5 document types.
	 */
	public String DOCBOOK = "DocBook";
	
	/**
	 * Docbook4 document type.
	 */
	public String DOCBOOK4 = "DocBook 4";
	
	/**
	 * Docbook5 document type.
	 */
	public String DOCBOOK5 = "DocBook 5";
	
	/**
	 * All document types 
	 */
	public String ALLTYPES = "ALLTYPES";
	
	/**
	 *Get all profiling conditional attributes names. 
	 * @param documentType  the type of xml document: ProfilingInformation.DITA, ProfilingInformation.DOCBOOK, 
	 * ProfilingInformation.DOCBOOK4, ProfilingInformation.DOCBOOK5 or ProfilingInformation.ALLTYPES.
	 *
	 * @return a Set with attributes names.
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public Set<String> getProfileConditionAttributesNames(String documentType) throws ParserConfigurationException, SAXException, IOException;
	
	/**
	 *Get all profile conditions(attribute name and values). 
	 * @param documentType  the type of xml document: ProfilingInformation.DITA, ProfilingInformation.DOCBOOK, 
	 * ProfilingInformation.DOCBOOK4, ProfilingInformation.DOCBOOK5 or ProfilingInformation.ALLTYPES.
	 *
	 * @return a Map with attribute name(key) and set with values(value).
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public Map<String, Set<String>> getProfileConditions(String documentType) throws ParserConfigurationException, SAXException, IOException;
	
	/**
	 * Get all existence conditions sets
	 * @param documentType  the type of xml document: ProfilingInformation.DITA, ProfilingInformation.DOCBOOK, 
	 * ProfilingInformation.DOCBOOK4, ProfilingInformation.DOCBOOK5 or ProfilingInformation.ALLTYPES.
 	 * @return the list of sets
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public Map<String, Map<String, Set<String>> > getConditionsSets(String documentType) throws ParserConfigurationException, SAXException, IOException;

	/**
	 * Get profile conditions sets from the documents linked at the given urls.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public Map<String, Set<String>> getConditionsSet(List<String> urls) throws ParserConfigurationException, SAXException, IOException;
}
