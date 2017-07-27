package com.oxygenxml.docbookChecker.reporters;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.options.WSOptionsStorage;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

public class OxygenStatusReporter implements StatusReporter {

	@Override
	public void reportStatus(String message) {
		PluginWorkspaceProvider.getPluginWorkspace().showStatusMessage(message);
		
		WSOptionsStorage optionsStorage = PluginWorkspaceProvider.getPluginWorkspace().getOptionsStorage();
	
		
		 ((StandalonePluginWorkspace)PluginWorkspaceProvider.getPluginWorkspace()).getResourceBundle().getMessage(Tags.CHECK_FILE_KEY);
	}

}
