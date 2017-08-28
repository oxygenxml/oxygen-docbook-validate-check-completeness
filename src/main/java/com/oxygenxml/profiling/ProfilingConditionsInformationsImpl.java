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
				// check the documentType
				if (conditions[i].getDocumentTypePattern().contains(documentType) ) {
					toReturn.add(conditions[i].getAttributeName());
				} else if (documentType.equals(ProfilingConditionsInformations.ALLTYPES)) {
					toReturn.add(conditions[i].getAttributeName());
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
			//return a empty map
			return toReturn;
		}

		else {
			// iterate over conditions
			for (int i = 0; i < conditions.length; i++) {
				// check the documentType
				if(documentType.equals(ProfilingConditionsInformations.DOCBOOK4) || documentType.equals(ProfilingConditionsInformations.DOCBOOK5)){
					if (documentType.equals(conditions[i].getDocumentTypePattern()) || 
							ProfilingConditionsInformations.DOCBOOK.equals(conditions[i].getDocumentTypePattern())) {
						addConditionInfoInMap(conditions[i], toReturn);
					} 
		 
				}else if (conditions[i].getDocumentTypePattern().contains(documentType)) {
					addConditionInfoInMap(conditions[i], toReturn);
				} else if (documentType.equals(ProfilingConditionsInformations.ALLTYPES)) {
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
		map.put(profileCondition.getAttributeName(), value);
	}

	@Override
	public LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> getConditionsSets(String documentType){
	// Map to return
		LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> toReturn = new LinkedHashMap<String, LinkedHashMap<String,LinkedHashSet<String>>>();

			// get a vector with profile conditions sets
			ProfileConditionsSetInfoPO[] conditionsSets = (ProfileConditionsSetInfoPO[]) PluginWorkspaceProvider.getPluginWorkspace()
					.getGlobalObjectProperty("profiling.conditions.set.list");

			if (conditionsSets == null) {
				//return a empty map
				return toReturn;
			}

			else {
				// iterate over conditions
				for (int i = 0; i < conditionsSets.length; i++) {
					// check the documentType
					if (conditionsSets[i].getDocumentTypePattern().contains(documentType)) {
						addConditionsSetInfoInMap(conditionsSets[i], toReturn);
					} else if (documentType.equals(ProfilingConditionsInformations.ALLTYPES)) {
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
	public LinkedHashMap<String, LinkedHashSet<String>> getConditionsFromDocs(String url) throws ParserConfigurationException, SAXException, IOException{
		ProfileDocsFinder finder = new ProfileDocsFinder();
		return finder.gatherProfilingConditions(url);
	}

	@Override
	public Set<String> getConditionSetsNames(String documentType) {
				// Set to return
				 Set<String> toReturn = new HashSet<String>();

				// get a vector with profile conditions sets
				ProfileConditionsSetInfoPO[] conditionsSets = (ProfileConditionsSetInfoPO[]) PluginWorkspaceProvider.getPluginWorkspace()
						.getGlobalObjectProperty("profiling.conditions.set.list");

				if (conditionsSets == null) {
					//return a empty set
					return toReturn;
				}

				else {
					// iterate over conditions
					for (int i = 0; i < conditionsSets.length; i++) {
						// check the documentType
						if (conditionsSets[i].getDocumentTypePattern().contains(documentType)) {
							toReturn.add("\""+conditionsSets[i].getConditionSetName()+"\"");
						} else if (documentType.equals(ProfilingConditionsInformations.ALLTYPES)) {
							toReturn.add(conditionsSets[i].getConditionSetName());
						}
					}
				}
				return toReturn;
		}


}
