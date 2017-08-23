package com.oxygenxml.docbook.checker.persister;

import com.oxygenxml.docbook.checker.CheckerInteractor;

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
