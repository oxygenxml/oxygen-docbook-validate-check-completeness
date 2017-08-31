package com.oxygenxml.docbook.checker.reporters;

import org.apache.commons.io.FilenameUtils;

/**
 * Generate the tab key for result manager.
 * @author intern4
 *
 */
public class TabKeyGenerator {
	private static final String appName = "DocBook Checker";
	
	/**
	 * Generate the tab key.
	 * @param currentFileURL The URL of file in String format.
	 * @param currentConditionSet	The current conditions set.
	 * @return
	 */
	public static String generate(String currentFileURL, String currentConditionSet) {
		String currentTab;
		// get the file name
		String currentFileName = FilenameUtils.getName(currentFileURL);

		// determine the name of the current tab
		if (currentConditionSet.isEmpty() ) {
			currentTab = appName +" - " + currentFileName;

		} else {
			currentTab = appName+" - \"" + currentConditionSet + "\" - " + currentFileName;
		}

		return currentTab;
	}
}
