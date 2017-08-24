package com.oxygenxml.docbook.checker.reporters;

import org.apache.commons.io.FilenameUtils;

public class TabKey {
	private static final String appName = "DocBook Checker";
	
	public static String generate(String currentFileURL, String currentConditionSet) {
		String currentTab;
		// get the current url file name;
		String currentFileName = FilenameUtils.getName(currentFileURL);

		// determine the name of current tab
		if (currentConditionSet.isEmpty() ) {
			currentTab = appName +" - " + currentFileName;

		} else {
			currentTab = appName+" - \"" + currentConditionSet + "\" - " + currentFileName;
		}

		return currentTab;
	}
}
