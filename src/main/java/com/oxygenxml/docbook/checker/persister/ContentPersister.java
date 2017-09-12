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
	 * @param frame Checker interactor
	 */
	public void saveState(CheckerInteractor interactor);
	
	/**
	 * Load content before start the dialog.
	 * @param frame Checker interactor
	 */
	public void loadState(CheckerInteractor interactor);
}
