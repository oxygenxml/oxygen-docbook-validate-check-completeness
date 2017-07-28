package com.oxygenxml.docbookChecker.persister;

import com.oxygenxml.docbookChecker.CheckerInteractor;

/**
 * Used for save and persist  
 * @author intern4
 *
 */
public interface ContentPersister {

	/**
	 * Save content before close the dialog.
	 */
	public void saveContent(CheckerInteractor frame);
	
	/**
	 * Set saved content before start the dialog.
	 */
	public void setSavedContent(CheckerInteractor frame);
}
