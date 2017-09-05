package com.oxygenxml.profiling;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

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

				// test in docType pattern isn't null.
				if (docTypePattern != null) {

					// check the documentType
					if (docTypePattern.equals(documentType)) {
						// add the attribute name
						toReturn.add(conditions[i].getAttributeName());
					}

					// if document type is DocBook 4 or DocBook 5 add in list the common
					// attributes name(DocBook*);
					else if ((documentType.equals(ProfilingConditionsInformations.DOCBOOK4)
							|| documentType.equals(ProfilingConditionsInformations.DOCBOOK5)
									&& docTypePattern.equals(ProfilingConditionsInformations.DOCBOOK))) {
						toReturn.add(conditions[i].getAttributeName());
					}
				}
			}
		}
		return toReturn;
	}

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
				
				//test in docType pattern isn't null.
				if(docTypePattern != null){
					
					if (docTypePattern.equals(documentType)) {
						// add conditions in map
						addConditionInfoInMap(conditions[i], toReturn);
					}

					// if document type is DocBook 4 or DocBook 5 add in map the common
					// conditions (DocBook*);
					else if ((documentType.equals(ProfilingConditionsInformations.DOCBOOK4)
							|| documentType.equals(ProfilingConditionsInformations.DOCBOOK5) )
							&& docTypePattern.equals(ProfilingConditionsInformations.DOCBOOK) ) {
						addConditionInfoInMap(conditions[i], toReturn);
					}
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
		map.put(profileCondition.getAttributeName(), value);
	}

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

				// test in docType pattern isn't null.
				if (docTypePattern != null) {
					
					// check the documentType
					if (docTypePattern.equals(documentType)) {
						// add conditions set in map
						addConditionsSetInfoInMap(conditionsSets[i], toReturn);
					}

					// if document type is DocBook 4 or DocBook 5 add in map the common
					// conditions sets(DocBook*);
					else if ((documentType.equals(ProfilingConditionsInformations.DOCBOOK4)
							|| documentType.equals(ProfilingConditionsInformations.DOCBOOK5))
							&& docTypePattern.equals(ProfilingConditionsInformations.DOCBOOK)) {
						addConditionsSetInfoInMap(conditionsSets[i], toReturn);
					}
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
	private void addConditionsSetInfoInMap(ProfileConditionsSetInfoPO profileConditionsSet, LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> map) {
		
		Method method;
		try {
			method = profileConditionsSet.getClass().getDeclaredMethod("getConditions");
			try {
				//get the name of set
				String name = profileConditionsSet.getConditionSetName();
				//get the conditions
				Object obj = method.invoke(profileConditionsSet);
				
				LinkedHashMap<String, LinkedHashSet<String>>value = new LinkedHashMap<String, LinkedHashSet<String>>();
				
				Iterator<String> iterKey = ((LinkedHashMap<String, String[]>)obj).keySet().iterator();
				while(iterKey.hasNext()){
					String key = iterKey.next();
					value.put(key, new LinkedHashSet<String>(Arrays.asList( ((LinkedHashMap<String, String[]>)obj).get(key) )) );
				}
				
				//put in map
				map.put(name,  value);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}

	}

	
	@Override
	public LinkedHashMap<String, LinkedHashSet<String>> getConditionsFromDocs(String url, String docType) throws ParserConfigurationException, SAXException, IOException{
		ProfileDocsFinder finder = new ProfileDocsFinder();
		return finder.gatherProfilingConditions(url, getProfileConditionAttributesNames(docType));
	}

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
		}

		else {
			// iterate over conditions
			for (int i = 0; i < conditionsSets.length; i++) {
				String docTypePattern = conditionsSets[i].getDocumentTypePattern();

				// test in docType pattern isn't null.
				if (docTypePattern != null) {

					// check the documentType
					if (docTypePattern.equals(documentType)) {
						toReturn.add(conditionsSets[i].getConditionSetName());
					}

					// if document type is DocBook 4 or DocBook 5 add in map the common
					// conditions (DocBook*);
					else if ((documentType.equals(ProfilingConditionsInformations.DOCBOOK4)
							|| documentType.equals(ProfilingConditionsInformations.DOCBOOK5))
							&& docTypePattern.equals(ProfilingConditionsInformations.DOCBOOK)) {

						toReturn.add(conditionsSets[i].getConditionSetName());
					}
				}
			}
		}
		return toReturn;
	}

}
