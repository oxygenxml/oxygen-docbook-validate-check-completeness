package com.oxygenxml.docbook.checker.reporters;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
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
