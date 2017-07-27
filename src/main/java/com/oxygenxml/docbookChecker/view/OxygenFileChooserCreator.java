package com.oxygenxml.docbookChecker.view;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFileChooser;

import ro.sync.exml.workspace.api.PluginWorkspace;
import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.ui.InputUrlDialog;

public class OxygenFileChooserCreator implements FileChooserCreator {

	@Override
	public File[] createFileChooser() {
		String chooser = PluginWorkspaceProvider.getPluginWorkspace().chooseURLPath("Choose files", new String[] {"xml"}, "Choose", "" );
		//File[] chooser = PluginWorkspaceProvider.getPluginWorkspace().chooseFiles(new File(""),"Choose files", new String[] {"xml"}, "");		
		return new File[]{new File(chooser)};
	}

//	public static void main(String[] args) {
//		InputUrlDialog instance = InputUrlDialog.getInstance();
//		
//		instance.setSize(new Dimension(800, 600));
//		instance.setVisible(true);
//	}

}
