package com.oxygenxml.docbook.checker.gui;

import java.io.File;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;

/**
 * Display a URL Path chooser.
 * @author intern4
 *
 */
public class OxygenFileChooserCreator implements FileChooser {

	@Override
	public File[] createFileChooser(String title, String aproveButtonName) {
		String chooser = PluginWorkspaceProvider.getPluginWorkspace().chooseURLPath(title, new String[] {"xml"}, "xml files", "" );
		//File[] chooser = PluginWorkspaceProvider.getPluginWorkspace().chooseFiles(new File(""),"Choose files", new String[] {"xml"}, "");		
		if(chooser !=null)
			return new File[]{new File(chooser)};
		else{
			return null;
		}
	}

}
