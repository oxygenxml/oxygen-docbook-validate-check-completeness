package com.oxygenxml.docbookChecker.persister;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.options.WSOptionsStorage;
//TODO delete this class
/**
 * * Use WSOptionStorage for manage the ResultsManager tabs keys.
 * @author intern4
 *
 */
public class ProblemTabsManager {
	private final String TABS = "Problems_Tabs";
	
	private WSOptionsStorage optionsStorage = PluginWorkspaceProvider.getPluginWorkspace().getOptionsStorage();
	
	/**
	 * Save a keyTab in WSOptionStorage.
	 * @param tabKey
	 */
	public void saveTab(String keyTab){
		String savedTabs = 	optionsStorage.getOption(TABS, "");
		String toSave;
		if(savedTabs.isEmpty()){
			toSave = keyTab;
		}
		else{
			if(savedTabs.contains(keyTab)){
				toSave = savedTabs;
			}
			else{
				toSave = savedTabs + ";" + keyTab;
			}
		}
		optionsStorage.setOption(TABS, toSave);
	}
	
	/**
	 * Load all keyTabs used by DocBook checker.
	 * @return
	 */
	public Set<String> loadTabs(){
		Set<String> toReturn ;
		
		//get the tabsKeys
		String savedTabs = optionsStorage.getOption(TABS, "");
		
		System.out.println("saved tabs: " + savedTabs);
		toReturn = new HashSet<String>(Arrays.asList(savedTabs.split(";")));
		
		//clear tabsKeys from  WSOptionStorage
		optionsStorage.setOption(TABS, "");
		
		//return the keys
		return toReturn;
	}
}
