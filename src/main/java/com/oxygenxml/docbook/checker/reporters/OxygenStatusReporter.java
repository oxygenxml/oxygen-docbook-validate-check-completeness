package com.oxygenxml.docbook.checker.reporters;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
/**
 * Report status in oxygen using PluginWorkspace
 * @author Cosmin Duna
 *
 */
public class OxygenStatusReporter implements StatusReporter {

	/**
	 * Logger
	 */
	 private static final Logger logger = LoggerFactory.getLogger(OxygenStatusReporter.class);
	
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
			logger.debug(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.debug(e.getMessage(), e);
			// Restore interrupted state...
	    Thread.currentThread().interrupt();
		}
		
	}

}
