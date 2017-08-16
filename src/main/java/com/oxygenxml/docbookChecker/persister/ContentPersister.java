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
	public void saveState(CheckerInteractor frame);
	
	/**
	 * Load content before start the dialog.
	 */
	public void loadState(CheckerInteractor frame);
}
