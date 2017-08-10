package com.oxygenxml.profiling;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
				if (conditions[i].getDocumentTypePattern().contains(documentType)) {
					toReturn.add(conditions[i].getAttributeName());
				} else if (documentType.equals(ProfilingConditionsInformations.ALLTYPES)) {
					toReturn.add(conditions[i].getAttributeName());
				}
			}
		}
		return toReturn;
	}

	@Override
	public Map<String, Set<String>> getProfileConditions(String documentType) {
		// Map to return
		Map<String, Set<String>> toReturn = new HashMap<String, Set<String>>();

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
				if (conditions[i].getDocumentTypePattern().contains(documentType)) {
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
	private void addConditionInfoInMap(ProfileConditionInfoPO profileCondition, Map<String, Set<String>> map) {
		Set<String> value = new HashSet<String>();
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
	public Map<String, Map<String, Set<String>>> getConditionsSets(String documentType){
	// Map to return
			Map<String, Map<String, Set<String>> > toReturn = new HashMap<String, Map<String,Set<String>>>();

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
	private void addConditionsSetInfoInMap(ProfileConditionsSetInfoPO profileConditionsSet, Map<String, Map<String, Set<String>>> map) {
		// get conditions
		//TODO use Java reflection
//		profileConditionsSet.getClass().getDeclaredMethods()
		
		Method method;
		try {
			method = profileConditionsSet.getClass().getDeclaredMethod("getConditions");
			try {
				//get the name of set
				String name = profileConditionsSet.getConditionSetName();
				//get the conditions
				Object obj = method.invoke(profileConditionsSet);
				
				Map<String, Set<String>>value = new HashMap<String, Set<String>>();
				
				Iterator<String> iterKey = ((Map<String, String[]>)obj).keySet().iterator();
				while(iterKey.hasNext()){
					String key = iterKey.next();
					value.put(key, new HashSet<String>(Arrays.asList( ((Map<String, String[]>)obj).get(key) )) );
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
	public Map<String, Set<String>> getConditionsFromDocs(List<String> urls) throws ParserConfigurationException, SAXException, IOException{
		ProfileDocsFinder finder = new ProfileDocsFinder();
		Map<String, Set<String>> toReturn = new HashMap<String, Set<String>>();
		for (int i = 0; i < urls.size(); i++) {
			toReturn.putAll(finder.gatherProfilingConditions(urls.get(i)));
		}
		return toReturn;
	}


}
