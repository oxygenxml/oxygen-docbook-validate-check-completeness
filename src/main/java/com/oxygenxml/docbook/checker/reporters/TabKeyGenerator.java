package com.oxygenxml.docbook.checker.reporters;

import java.net.URL;

import com.oxygenxml.docbook.checker.translator.OxygenTranslator;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

/**
 * Generate the tab key for result manager.
 * @author intern4
 *
 */
public class TabKeyGenerator {
	
	/**
	 * Private constructor.
	 */
	 private TabKeyGenerator() {
	    throw new IllegalStateException("Utility class");
	  }
	
	
	/**
	 * Generate the tab key.
	 * @return The tab Key
	 */
	public static String generate(){
		Translator translator = new OxygenTranslator();
		return translator.getTranslation(Tags.FRAME_TITLE);
	}
	
	/**
	 * Generate the tab key according to given currentFileURL and currentConditionSet.
	 * @param currentFileURL The URL of file in String format.
	 * @param currentConditionSet	The current conditions set.
	 * @return The tabKey.
	 */
	public static String generate(URL currentFileURL, String currentConditionSet) {
		Translator translator = new OxygenTranslator();
		String appName =  translator.getTranslation(Tags.FRAME_TITLE);
		
		String currentTab;
		// get the file name
		String currentFileName = currentFileURL.getFile();
		currentFileName = currentFileName.substring(currentFileName.lastIndexOf("/")+1);

		// determine the name of the current tab
		if (currentConditionSet == null || currentConditionSet.isEmpty() ) {
			currentTab = appName +" - " + currentFileName;

		} else {
			currentTab = appName+" - \"" + currentConditionSet + "\" - " + currentFileName;
		}

		return currentTab;
	}
}
