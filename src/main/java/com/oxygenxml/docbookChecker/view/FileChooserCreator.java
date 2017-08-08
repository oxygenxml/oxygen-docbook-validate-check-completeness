package com.oxygenxml.docbookChecker.view;

import java.io.File;

/**
 * Interface for create and display a FileChooser.
 * @author intern4
 *
 */
public interface FileChooserCreator {
	
	/**
	 * Create a fileChooser.
	 * @param title The chooser dialog title.
	 * @param aproveButtonName
	 * @return The chosen URLs as File vector or null if the user canceled the dialog
	 */
	public File[] createFileChooser(String title, String aproveButtonName);
}
