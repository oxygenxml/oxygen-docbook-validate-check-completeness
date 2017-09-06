package com.oxygenxml.docbook.checker.reporters;

import org.apache.commons.io.FilenameUtils;

/**
 * Generate the tab key for result manager.
 * @author intern4
 *
 */
public class TabKeyGenerator {
	private static final String APP_NAME = "DocBook Checker";
	
	/**
	 * Generate the tab key.
	 * @param currentFileURL The URL of file in String format.
	 * @param currentConditionSet	The current conditions set.
	 * @return The tabKey.
	 */
	public static String generate(String currentFileURL, String currentConditionSet) {
		String currentTab;
		// get the file name
		String currentFileName = FilenameUtils.getName(currentFileURL);

		// determine the name of the current tab
		if (currentConditionSet.isEmpty() ) {
			currentTab = APP_NAME +" - " + currentFileName;

		} else {
			currentTab = APP_NAME+" - \"" + currentConditionSet + "\" - " + currentFileName;
		}

		return currentTab;
	}
}
