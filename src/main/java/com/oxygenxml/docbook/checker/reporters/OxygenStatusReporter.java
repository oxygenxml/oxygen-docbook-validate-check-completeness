package com.oxygenxml.docbook.checker.reporters;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
/**
 * Report status in oxygen using PluginWorkspace
 * @author intern4
 *
 */
public class OxygenStatusReporter implements StatusReporter {

	
	@Override
	public void reportStatus(final String message) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					PluginWorkspaceProvider.getPluginWorkspace().showStatusMessage(message);
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
