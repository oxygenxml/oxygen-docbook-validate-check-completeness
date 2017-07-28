package com.oxygenxml.docbookChecker.view;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Create standard JFileChooser and set settings.
 * 
 * @author intern4
 *
 */
public class SwingFileChooserCreator implements FileChooserCreator {

	@Override
	public File[] createFileChooser(String title, String aproveButtonName) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("D:\\docbook-validate-check-completeness\\test-samples"));
		chooser.setMultiSelectionEnabled(true);
		chooser.setDialogTitle(title);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setApproveButtonText(aproveButtonName);

		chooser.addChoosableFileFilter(new FileNameExtensionFilter("xml files (*.xml)", "xml"));
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File[] returnedFiles = chooser.getSelectedFiles();
			for(int i = 0; i < returnedFiles.length; i++){
				returnedFiles[i] = new File("file://"+ returnedFiles[i].toString());
			}
			return returnedFiles;
			
		} else
			return null;
	}

}
