package com.oxygenxml.docbookChecker.translator;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

/**
 * Implement internationalization using PluginResourceBundle
 * @author intern4
 *
 */
public class TranslatorExtensionImpl implements Translator {

	@Override
	public String getTraslation(String key) {
		return ((StandalonePluginWorkspace)PluginWorkspaceProvider.getPluginWorkspace()).getResourceBundle().getMessage(key);
	}
	
}
