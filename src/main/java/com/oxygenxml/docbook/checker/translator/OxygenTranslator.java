package com.oxygenxml.docbook.checker.translator;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

/**
 * Implement internationalization using PluginResourceBundle
 * @author Cosmin Duna
 *
 */
public class OxygenTranslator implements Translator {

	/**
	 * Get the translation of given key using PluginResourceBundle.
	 * @param key The key.
	 */
	@Override
	public String getTranslation(String key) {
		return ((StandalonePluginWorkspace)PluginWorkspaceProvider.getPluginWorkspace()).getResourceBundle().getMessage(key);
	}
	
}
