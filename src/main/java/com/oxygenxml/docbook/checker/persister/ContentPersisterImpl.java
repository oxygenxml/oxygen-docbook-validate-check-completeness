package com.oxygenxml.docbook.checker.persister;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.docbook.checker.reporters.OxygenStatusReporter;

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

	/**
	 * Logger
	 */
	 private static final Logger logger = Logger.getLogger(OxygenStatusReporter.class);
	
	
	/**
	 * Save the content from dialog.
	 * @param frame Checker interactor
	 */
	@Override
	public void saveState(CheckerInteractor interactor) {
		WSOptionsStorage optionsStorage = PluginWorkspaceProvider.getPluginWorkspace().getOptionsStorage();

		// save state of checkCurrentFile radioButton
		optionsStorage.setOption(OptionKeys.CHECK_CURRENT_RESOURCE, String.valueOf(interactor.isCheckCurrentResource()));

		// save state of configureConditionsSet radioButton
		optionsStorage.setOption(OptionKeys.CONFIG_CONDITION_SET_MANUALLY,
				String.valueOf(interactor.isUseManuallyConfiguredConditionsSet()));

		// save state of check using profiling conditions CheckBox
		optionsStorage.setOption(OptionKeys.USE_PROFILING, String.valueOf(interactor.isUsingProfile()));

		// save state of checkExternalLinks CheckBox
		optionsStorage.setOption(OptionKeys.CHECK_EXTERNAL_RESOURCES, String.valueOf(interactor.isCheckExternal()));

		// save state of checkImages CheckBox
		optionsStorage.setOption(OptionKeys.CHECK_BROKEN_IMAGES, String.valueOf(interactor.isCheckImages()));

		// save state of checkInternalLink CheckBox
		optionsStorage.setOption(OptionKeys.CHECK_INTERNAL_LINKS, String.valueOf(interactor.isCheckInternal()));

		// save state of generateHierarchyReport CheckBox
		optionsStorage.setOption(OptionKeys.GENERATE_HIERARCHY_REPORT, String.valueOf(interactor.isGenerateHierarchyReport()));
		
		// save state of reportUndefinedConditions CheckBox
		optionsStorage.setOption(OptionKeys.REPORTE_UNDEFINED_CONDITIONS,
				String.valueOf(interactor.isReporteUndefinedConditions()));

		// save all document types
		optionsStorage.setOption(OptionKeys.DOCUMENT_TYPES, ContentPersisterUtil.join(";", interactor.getAllDocumentTypes()));

		//save selected document types
		optionsStorage.setOption(OptionKeys.SELECTED_DOCUMENT_TYPE, interactor.getDocumentType());
		
		// save file table rows
		// --join list and save the result
		optionsStorage.setOption(OptionKeys.RESOURCES_TO_CHECK, ContentPersisterUtil.join(";", interactor.getOtherFilesToCheck()));

		// save condition table rows
		// --join Map in format: attribute--value##attribute--value and save the
		// result
		LinkedHashMap<String, LinkedHashSet<String>> tableRows = interactor.getDefinedConditions();
		List<String> newList = new ArrayList<String>();

		Iterator<String> iter = tableRows.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Set<String> value = tableRows.get(key);
			newList.add(key + "--" + ContentPersisterUtil.join(";", value));
		}
		optionsStorage.setOption(OptionKeys.CONDITIONS_USED_TO_CHECK, ContentPersisterUtil.join("##", newList));

	}


	/**
	 * Load the content in dialog.
	 * @param frame Checker interactor
	 */
	@Override
	public void loadState(CheckerInteractor interactor) {
		WSOptionsStorage optionsStorage = PluginWorkspaceProvider.getPluginWorkspace().getOptionsStorage();
		String value;

		// set rows in file table
		value = optionsStorage.getOption(OptionKeys.RESOURCES_TO_CHECK, "");
		if (!value.isEmpty()) {
			// split value in list with Strings
			List<String> rowList = new ArrayList<String>(Arrays.asList(value.split(";")));
			
			List<URL> rowListURL = new ArrayList<URL>();
			int rowListSize= rowList.size();
			for (int i = 0; i < rowListSize; i++) {
				try {
					rowListURL.add(new URL(rowList.get(i)));
				} catch (MalformedURLException e) {
					logger.debug(e.getMessage(), e);
				}
			}
			interactor.setOtherFilesToCheck(rowListURL);
		}

		// set rows in conditions table
		value = optionsStorage.getOption(OptionKeys.CONDITIONS_USED_TO_CHECK, "");
		// rows was saved in format: attribute--value##attribute--value
		if (!value.isEmpty()) {
			LinkedHashMap<String, String> conditions = new LinkedHashMap<String, String>();

			// split value String in list with Strings
			List<String> rowsList = new ArrayList<String>(Arrays.asList(value.split("##")));

			int size = rowsList.size();
			for (int i = 0; i < size; i++) {
				List<String> rowElementList = new ArrayList<String>(Arrays.asList(rowsList.get(i).split("--")));

				// put condition in map
				conditions.put(rowElementList.get(0), rowElementList.get(1));
			}
			// set defined conditions
			interactor.setDefinedConditions(conditions);

		}
		
		// set the document types
		value = optionsStorage.getOption(OptionKeys.DOCUMENT_TYPES, "DocBook 4;DocBook 5");
		// split value in list with Strings
		List<String> docTypes = new ArrayList<String>(Arrays.asList(value.split(";")));
		interactor.setAllDocumentTypes(docTypes);

		//set selected document type
		value = optionsStorage.getOption(OptionKeys.SELECTED_DOCUMENT_TYPE, "DocBook 5");		
		interactor.setDocumentType(value);
				
		// set checkCurrent radioButton or checkOther radioButton
		value = optionsStorage.getOption(OptionKeys.CHECK_CURRENT_RESOURCE, "true");
		interactor.setCheckCurrentResource(Boolean.valueOf(value));

		// set checkUsingProfilingConditions checkBox
		value = optionsStorage.getOption(OptionKeys.USE_PROFILING, null);
		interactor.setUseProfiligConditions(Boolean.valueOf(value));

		// set configConditionsSet radioButton or checkAllCombination radioButton
		value = optionsStorage.getOption(OptionKeys.CONFIG_CONDITION_SET_MANUALLY, "true");
		interactor.setUseManuallyConfiguredConditionsSet(Boolean.valueOf(value));

		// set checkExternalLinks checkBox
		value = optionsStorage.getOption(OptionKeys.CHECK_EXTERNAL_RESOURCES, "true");
		interactor.setCheckExternal(Boolean.valueOf(value));

		// set checkImages checkBox
		value = optionsStorage.getOption(OptionKeys.CHECK_BROKEN_IMAGES, "true");
		interactor.setCheckImages(Boolean.valueOf(value));

		// set checkInternalLinks checkBox
		value = optionsStorage.getOption(OptionKeys.CHECK_INTERNAL_LINKS, "true");
		interactor.setCheckInternal(Boolean.valueOf(value));

		// set generateHierarchyReport CheckBox
		value = optionsStorage.getOption(OptionKeys.GENERATE_HIERARCHY_REPORT, "false");
		interactor.setGenerateHierarchyReport(Boolean.valueOf(value));
		
		// set reportUndefinedConditions checkBox
		value = optionsStorage.getOption(OptionKeys.REPORTE_UNDEFINED_CONDITIONS, "true");
		interactor.setReporteUndefinedConditions(Boolean.valueOf(value));

	}
}