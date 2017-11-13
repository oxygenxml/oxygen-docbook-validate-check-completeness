package com.oxygenxml.profiling;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import ro.sync.ecss.conditions.ProfileConditionInfoPO;
import ro.sync.ecss.conditions.ProfileConditionValuePO;
import ro.sync.ecss.conditions.ProfileConditionsSetInfoPO;
import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
/**
 * Use GlobalObjectProperty to find profile conditions.
 * 
 * @author intern4
 *
 */
public class ProfilingConditionsInformationsImpl implements ProfilingConditionsInformations {

	
	/**
	 * Logger
	 * 
	 */
	 private static final Logger logger = Logger.getLogger(ProfilingConditionsInformationsImpl.class);
	
	/**
	 *Get all profiling conditional attributes names using GlobalObjectProperty. 
	 * @param documentType  the type of xml document: 
	 * ProfilingInformation.DOCBOOK4, ProfilingInformation.DOCBOOK5 or other.
	 *
	 * @return a Set with attributes names.
	 */
	@Override
	public Set<String> getProfileConditionAttributesNames(String documentType) {
		// Set to return
		Set<String> toReturn = new HashSet<String>();

		// get a vector with profileConditions
		ProfileConditionInfoPO[] conditions = (ProfileConditionInfoPO[]) PluginWorkspaceProvider.getPluginWorkspace()
				.getGlobalObjectProperty("profiling.conditions.list");

		if (conditions == null) {
			//return a empty set
			return toReturn;
		} else {
			// iterate over conditions
			for (int i = 0; i < conditions.length; i++) {
				String docTypePattern = conditions[i].getDocumentTypePattern();

				// if document type is DocBook 4 or DocBook 5 add in list the common
				// attributes name(DocBook*)
				if (docTypePattern != null && (docTypePattern.equals(documentType)
						|| ((documentType.equals(DocBookTypes.DOCBOOK4) || documentType.equals(DocBookTypes.DOCBOOK5))
								&& docTypePattern.equals(DocBookTypes.DOCBOOK)))) {
					toReturn.add(conditions[i].getAttributeName());
				}
			}
		}
		return toReturn;
	}

	/**
	 *Get all profile conditions(attribute name and values) using GlobalObjectProperty. 
	 * @param documentType  the type of xml document: 
	 * ProfilingInformation.DOCBOOK4, ProfilingInformation.DOCBOOK5 or other.
	 *
	 * @return a Map with attribute name(key) and set with values(value).
	 */
	@Override
	public LinkedHashMap<String, LinkedHashSet<String>> getProfileConditions(String documentType) {
		// Map to return
		LinkedHashMap<String, LinkedHashSet<String>> toReturn = new LinkedHashMap<String, LinkedHashSet<String>>();

		// get a vector with profileConditions
		ProfileConditionInfoPO[] conditions = (ProfileConditionInfoPO[]) PluginWorkspaceProvider.getPluginWorkspace()
				.getGlobalObjectProperty("profiling.conditions.list");

		if (conditions == null) {
			// return a empty map
			return toReturn;
		}

		else {
			// iterate over conditions
			int size =  conditions.length;
			for (int i = 0; i < size; i++) {
				String docTypePattern = conditions[i].getDocumentTypePattern();

				// if document type is DocBook 4 or DocBook 5 add in map the common
				// conditions (DocBook*)
				if (docTypePattern != null && (docTypePattern.equals(documentType)
						|| ((documentType.equals(DocBookTypes.DOCBOOK4) || documentType.equals(DocBookTypes.DOCBOOK5))
								&& docTypePattern.equals(DocBookTypes.DOCBOOK)))){
					addConditionInfoInMap(conditions[i], toReturn);
				}
			}
		}
		return toReturn;
	}

	/**
	 *  Add attribute name and values from a given ProfileConditionInfoPO in a given map.
	 * @param profileCondition
	 * @param map
	 */
	private void addConditionInfoInMap(ProfileConditionInfoPO profileCondition, LinkedHashMap<String, LinkedHashSet<String>> map) {
		LinkedHashSet<String> value = new LinkedHashSet<String>();
		// get values
		ProfileConditionValuePO[] values = profileCondition.getAllowedValues();

		for (int j = 0; j < values.length; j++) {
			// add value in a set
			value.add(values[j].getValue());
		}
		
		// put in map
		if(map.containsKey(profileCondition.getAttributeName())){
			map.get(profileCondition.getAttributeName()).addAll(value);
		}else{
			map.put(profileCondition.getAttributeName(), value);
		}
		
	}

	
	/**
	 * Get all existence conditions sets using GlobalObjectProperty
	 * @param documentType  the type of xml document: 
	 * ProfilingInformation.DOCBOOK4, ProfilingInformation.DOCBOOK5 or other.
 	 * @return the list of sets
	 */
	@Override
	public LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> getConditionsSets(String documentType){
		// Map to return
		LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> toReturn = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>>();

		// get a vector with profile conditions sets
		ProfileConditionsSetInfoPO[] conditionsSets = (ProfileConditionsSetInfoPO[]) PluginWorkspaceProvider
				.getPluginWorkspace().getGlobalObjectProperty("profiling.conditions.set.list");

		if (conditionsSets == null) {
			// return a empty map
			return toReturn;
		}

		else {
			// iterate over conditions
			for (int i = 0; i < conditionsSets.length; i++) {
				String docTypePattern = conditionsSets[i].getDocumentTypePattern();

				// check the documentType
				// if document type is DocBook 4 or DocBook 5 add in map the common
				// conditions sets(DocBook*)
				if (docTypePattern != null && (docTypePattern.equals(documentType)
						|| ((documentType.equals(DocBookTypes.DOCBOOK4) || documentType.equals(DocBookTypes.DOCBOOK5))
								&& docTypePattern.equals(DocBookTypes.DOCBOOK)))) {
					addConditionsSetInfoInMap(conditionsSets[i], toReturn);
				}
			}
		}
		return toReturn;
	}

/**
 * Add conditionsSet from a given ProfileConditionsSetInfoPO in a given map.
 * @param profileConditionsSet
 * @param map
 */
	private void addConditionsSetInfoInMap(ProfileConditionsSetInfoPO profileConditionsSet,
			LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> map) {

		Method method;
		try {
			method = profileConditionsSet.getClass().getDeclaredMethod("getConditions");
			// get the name of set
			String name = profileConditionsSet.getConditionSetName();
			// get the conditions
			Object obj = method.invoke(profileConditionsSet);

			LinkedHashMap<String, LinkedHashSet<String>> value = new LinkedHashMap<String, LinkedHashSet<String>>();

			Iterator<String> iterKey = ((LinkedHashMap<String, String[]>) obj).keySet().iterator();
			while (iterKey.hasNext()) {
				String key = iterKey.next();
				value.put(key, new LinkedHashSet<String>(Arrays.asList(((LinkedHashMap<String, String[]>) obj).get(key))));
			}

			// put in map
			map.put(name, value);
		} catch (IllegalAccessException e) {
			logger.debug(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.debug(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.debug(e.getMessage(), e);
		} catch (NoSuchMethodException e1) {
			logger.debug(e1.getMessage(), e1);
		} catch (SecurityException e1) {
			logger.debug(e1.getMessage(), e1);
		}

	}

	/**
	 * Get profile conditions from the documents linked at the given URLs according to given document type.
	 * @param url The URL.
	 * @param docType The document type.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	@Override
	public LinkedHashMap<String, LinkedHashSet<String>> getConditionsFromDocs(URL url, String docType) throws ParserConfigurationException, SAXException, IOException{
		ProfileDocsFinder finder = new ProfileDocsFinder();
		return finder.gatherProfilingConditions(url, getProfileConditionAttributesNames(docType));
	}

	
	/**
	 * Get all existence condition sets names using GlobalObjectProperty.
	 * @param documentType  the type of xml document: 
	 * ProfilingInformation.DOCBOOK4, ProfilingInformation.DOCBOOK5 or other.
	 * @return A set with names.
	 */
	@Override
	public Set<String> getConditionSetsNames(String documentType) {
		// Set to return
		Set<String> toReturn = new HashSet<String>();
		
		// get a vector with profile conditions sets
		ProfileConditionsSetInfoPO[] conditionsSets = (ProfileConditionsSetInfoPO[]) PluginWorkspaceProvider
				.getPluginWorkspace().getGlobalObjectProperty("profiling.conditions.set.list");

		if (conditionsSets == null) {
			// return a empty set
			return toReturn;
		}else {
			// iterate over conditions
			for (int i = 0; i < conditionsSets.length; i++) {
				String docTypePattern = conditionsSets[i].getDocumentTypePattern();

				// test in docType pattern isn't null.
				if (docTypePattern != null) {

					// if document type is DocBook 4 or DocBook 5 add in map the common
					// conditions (DocBook*)
				 if (docTypePattern != null && (docTypePattern.equals(documentType) || 
							((documentType.equals(DocBookTypes.DOCBOOK4)
							|| documentType.equals(DocBookTypes.DOCBOOK5))
							&& docTypePattern.equals(DocBookTypes.DOCBOOK)))) {

						toReturn.add(conditionsSets[i].getConditionSetName());
					}
				}
			}
		}
		return toReturn;
	}

}
