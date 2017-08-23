package com.oxygenxml.docbook.checker.persister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import com.oxygenxml.docbook.checker.CheckerInteractor;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.options.WSOptionsStorage;

/**
 * Use WSOptionStorage for save content from GUI and load saved content
 * 
 * 
 * @author intern4
 *
 */
public class ContentPersisterImpl implements ContentPersister {

	@Override
	public void saveState(CheckerInteractor interactor) {
		WSOptionsStorage optionsStorage = PluginWorkspaceProvider.getPluginWorkspace().getOptionsStorage();

		// save state of checkCurrentFile radioButton
		optionsStorage.setOption(OptionKeys.CHECK_CURRENT_RESOURCE, String.valueOf(interactor.isCheckCurrentResource()));

		// save state of configureConditionsSet radioButton
		optionsStorage.setOption(OptionKeys.CONFIG_CONDITION_SET_MANUALLY, String.valueOf(interactor.isUseManuallyConfiguredConditionsSet()));

		// save state of check using profiling conditions CheckBox
		optionsStorage.setOption(OptionKeys.USE_PROFILING, String.valueOf(interactor.isUsingProfile()));
		
		// save state of checkExternalLinks CheckBox
		optionsStorage.setOption(OptionKeys.CHECK_EXTERNAL_RESOURCES, String.valueOf(interactor.isSelectedCheckExternal()));

		// save state of checkImages CheckBox
		optionsStorage.setOption(OptionKeys.CHECK_BROKEN_IMAGES, String.valueOf(interactor.isSelectedCheckImages()));

		// save state of checkInternalLink CheckBox
		optionsStorage.setOption(OptionKeys.CHECK_INTERNAL_LINKS, String.valueOf(interactor.isSelectedCheckInternal()));

		// save file table rows
		// --join list and save the result
		optionsStorage.setOption(OptionKeys.RESOURCES_TO_CHECK, join(";", interactor.getOtherResourcesToCheck()));

		// save file table rows
		// --join Map in format: attribute--value##attribute--value and save the result
		LinkedHashMap<String, LinkedHashSet<String>> tableRows = interactor.getDefinedConditions();
		List<String> newList = new ArrayList<String>();

		Iterator<String> iter = tableRows.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Set<String> value = tableRows.get(key);
			newList.add(key + "--" + join(";", value));
		}
		optionsStorage.setOption(OptionKeys.CONDITIONS_USED_TO_CHECK, join("##", newList));

	}

	private String join(String delimiter, Collection<String> otherResourcesToCheck) {
		 StringJoiner joiner = new StringJoiner(delimiter);
     for (CharSequence cs: otherResourcesToCheck) {
         joiner.add(cs);
     }
     return joiner.toString();
	}

	@Override
	public void loadState(CheckerInteractor interactor) {
		WSOptionsStorage optionsStorage = PluginWorkspaceProvider.getPluginWorkspace().getOptionsStorage();
		String value;

		// set checkCurrent radioButton or checkOther radioButton
		value = optionsStorage.getOption(OptionKeys.CHECK_CURRENT_RESOURCE, null);
		interactor.setCheckCurrentResource(Boolean.valueOf(value));

		// set checkUsingProfilingConditions checkButton
		value = optionsStorage.getOption(OptionKeys.USE_PROFILING, null);
		interactor.setUseProfiligConditions(Boolean.valueOf(value));
		
		// set configConditionsSet radioButton or checkAllCombination radioButton
		value = optionsStorage.getOption(OptionKeys.CONFIG_CONDITION_SET_MANUALLY, null);
		interactor.setUseManuallyConfiguredConditionsSet(Boolean.valueOf(value));


		// set checkExternalLinks checkButton
		value = optionsStorage.getOption(OptionKeys.CHECK_EXTERNAL_RESOURCES, null);
		interactor.setCheckExternal(Boolean.valueOf(value));

		// set checkImages checkButton
		value = optionsStorage.getOption(OptionKeys.CHECK_BROKEN_IMAGES, null);
		interactor.setCheckImages(Boolean.valueOf(value));
		// set checkInternalLinks checkButton
		value = optionsStorage.getOption(OptionKeys.CHECK_INTERNAL_LINKS, null);
		interactor.setCheckInternal(Boolean.valueOf(value));

		// set rows in file table
		value = optionsStorage.getOption(OptionKeys.RESOURCES_TO_CHECK, "");
		if (!value.isEmpty()) {
			// split value in list with Strings
			List<String> rowList = new ArrayList<String>(Arrays.asList(value.split(";")));
			interactor.setResourcesToCheck(rowList);
		}

		// set rows in conditions table
		value = optionsStorage.getOption(OptionKeys.CONDITIONS_USED_TO_CHECK, "");
		//rows was saved in format: attribute--value##attribute--value 
		if (!value.isEmpty()) {
			LinkedHashMap<String, String> conditions = new LinkedHashMap<String, String>();

			// split value String in list with Strings
			List<String> rowsList = new ArrayList<String>(Arrays.asList(value.split("##")));

			for (int i = 0; i < rowsList.size(); i++) {
				List<String> rowElementList = new ArrayList<String>(Arrays.asList(rowsList.get(i).split("--")));
				
				//put condition in map
				conditions.put(rowElementList.get(0), rowElementList.get(1));
			}
			//set defined conditions
			interactor.setDefinedConditions(conditions);
		
		}

	}
}