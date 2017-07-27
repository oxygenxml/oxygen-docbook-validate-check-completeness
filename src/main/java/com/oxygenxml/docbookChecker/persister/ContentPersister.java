package com.oxygenxml.docbookChecker.persister;

import com.oxygenxml.docbookChecker.view.CheckerFrame;

/**
 * Used for save and persist  
 * @author intern4
 *
 */
public interface ContentPersister {

	/**
	 * Save content before close the dialog.
	 */
	public void saveContent(CheckerFrame frame);
	
	/**
	 * Set saved content before start the dialog.
	 */
	public void setSavedContent(CheckerFrame frame);
}
