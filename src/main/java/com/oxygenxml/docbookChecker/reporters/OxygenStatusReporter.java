package com.oxygenxml.docbookChecker.reporters;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.options.WSOptionsStorage;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
/**
 * Report status in oxygen using PluginWorkspace
 * @author intern4
 *
 */
public class OxygenStatusReporter implements StatusReporter {

	@Override
	public void reportStatus(String message) {
		PluginWorkspaceProvider.getPluginWorkspace().showStatusMessage(message);
		
	}

}
