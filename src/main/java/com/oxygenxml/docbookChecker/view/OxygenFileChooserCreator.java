package com.oxygenxml.docbookChecker.view;

import java.io.File;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;

public class OxygenFileChooserCreator implements FileChooserCreator {

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



//	public static void main(String[] args) {
//		InputUrlDialog instance = InputUrlDialog.getInstance();
//		
//		instance.setSize(new Dimension(800, 600));
//		instance.setVisible(true);
//	}

}
