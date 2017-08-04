package com.oxygenxml.docbookChecker.persister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oxygenxml.docbookChecker.translator.Tags;
import com.oxygenxml.docbookChecker.CheckerInteractor;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.options.WSOptionsStorage;

/**
 * Use WSOptionStorage for save content from GUI in system and Set saved content
 * in GUI.
 * 
 * 
 * @author intern4
 *
 */
public class ContentPersisterImpl implements ContentPersister {

	@Override
	public void saveContent(CheckerInteractor interactor) {
		WSOptionsStorage optionsStorage = PluginWorkspaceProvider.getPluginWorkspace().getOptionsStorage();

		// save state of checkCurrentFile radioButton
		if (interactor.isSelectedCheckCurrent()) {
			optionsStorage.setOption(Tags.CHECK_FILE_KEY, Tags.SET);
		} else {
			optionsStorage.setOption(Tags.CHECK_FILE_KEY, Tags.NOT_SET);
		}
		// save state of configureConditionsSet radioButton
		if (interactor.isSelectedConfigConditionsSet()) {
			optionsStorage.setOption(Tags.CONFIG_CONDITIONS_SET, Tags.SET);
		} else {
			optionsStorage.setOption(Tags.CONFIG_CONDITIONS_SET, Tags.NOT_SET);
		}

		// save state of check using profiling conditions CheckBox
		if (interactor.isSelectedCheckProfile()) {
			optionsStorage.setOption(Tags.USE_PROFLING_CBOX, Tags.SET);
		} else {
			optionsStorage.setOption(Tags.CHECK_EXTERNAL_KEY, Tags.NOT_SET);
		}
		// save state of checkExternalLinks CheckBox
		if (interactor.isSelectedCheckExternal()) {
			optionsStorage.setOption(Tags.CHECK_EXTERNAL_KEY, Tags.SET);
		} else {
			optionsStorage.setOption(Tags.CHECK_EXTERNAL_KEY, Tags.NOT_SET);
		}

		// save state of checkImages CheckBox
		if (interactor.isSelectedCheckImages()) {
			optionsStorage.setOption(Tags.CHECK_IMAGES_KEY, Tags.SET);
		} else {
			optionsStorage.setOption(Tags.CHECK_IMAGES_KEY, Tags.NOT_SET);
		}

		// save state of checkInternalLink CheckBox
		if (interactor.isSelectedCheckInternal()) {
			optionsStorage.setOption(Tags.CHECK_INTERNAL_KEY, Tags.SET);
		} else {
			optionsStorage.setOption(Tags.CHECK_INTERNAL_KEY, Tags.NOT_SET);
		}

		// save file table rows
		// --join list and save the result
		optionsStorage.setOption(Tags.FILE_TABLE_ROWS, String.join(";", interactor.getFilesTableRows()));

		// save file table rows
		// --join list and save the result
		Map<String, Set<String>> tableRows = interactor.getConditionsTableRows();
		Set<String> newSet = new HashSet<String>();

		Iterator<String> iter = tableRows.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Set<String> value = tableRows.get(key);
			newSet.add(key + "--" + String.join(";", value));
		}
		optionsStorage.setOption(Tags.CONDITIONS_TABLE_ROWS, String.join("##", newSet));

	}

	@Override
	public void setSavedContent(CheckerInteractor interactor) {
		WSOptionsStorage optionsStorage = PluginWorkspaceProvider.getPluginWorkspace().getOptionsStorage();
		String value;

		// set checkCurrent radioButton or checkOther radioButton
		value = optionsStorage.getOption(Tags.CHECK_FILE_KEY, Tags.SET);
		if (value.equals(Tags.SET)) {
			interactor.doClickOnCheckCurrentLink();
		} else {
			interactor.doClickOnCheckOtherLink();
		}

		// set configConditionsSet radioButton or checkAllCombination radioButton
		value = optionsStorage.getOption(Tags.CONFIG_CONDITIONS_SET, Tags.SET);
		if (value.equals(Tags.SET)) {
			interactor.doClickOnConfigConditionSet();
		} else {
			interactor.doClickOnCheckAllConbinations();
		}

		// set checkUsingProfilingConditions checkButton
		value = optionsStorage.getOption(Tags.USE_PROFLING_CBOX, Tags.SET);
		if (value.equals(Tags.SET)) {
			interactor.setUseProfiligConditions(true);
		} else {
			interactor.setUseProfiligConditions(false);
		}

		// set checkExternalLinks checkButton
		value = optionsStorage.getOption(Tags.CHECK_EXTERNAL_KEY, Tags.SET);
		if (value.equals(Tags.SET)) {
			interactor.setCheckExternal(true);
		} else {
			interactor.setCheckExternal(false);
		}

		// set checkImages checkButton
		value = optionsStorage.getOption(Tags.CHECK_IMAGES_KEY, Tags.SET);
		if (value.equals(Tags.SET)) {
			interactor.setCheckImages(true);
		} else {
			interactor.setCheckImages(false);
		}
		// set checkInternalLinks checkButton
		value = optionsStorage.getOption(Tags.CHECK_INTERNAL_KEY, Tags.SET);
		if (value.equals(Tags.SET)) {
			interactor.setCheckInternal(true);
		} else {
			interactor.setCheckInternal(false);
		}

		// set rows in file table
		value = optionsStorage.getOption(Tags.FILE_TABLE_ROWS, "");
		if (!value.isEmpty()) {
			// split value in list with Strings
			List<String> rowList = new ArrayList<String>(Arrays.asList(value.split(";")));

			interactor.setRowsInFilesTable(rowList);

		}

		// set rows in conditions table
		value = optionsStorage.getOption(Tags.CONDITIONS_TABLE_ROWS, "");
		//rows was saved in format: attribute--value##attribute--value 
		if (!value.isEmpty()) {
			List<String[]> rows = new ArrayList<String[]>();

			// split value String in list with Strings
			List<String> rowsList = new ArrayList<String>(Arrays.asList(value.split("##")));

			for (int i = 0; i < rowsList.size(); i++) {
				List<String> rowElementList = new ArrayList<String>(Arrays.asList(rowsList.get(i).split("--")));
				rows.add(new String[] { rowElementList.get(0), rowElementList.get(1) });
			}

			interactor.setRowsInConditionsTable(rows);
		}

	}
}