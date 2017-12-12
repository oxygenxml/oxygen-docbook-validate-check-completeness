package com.oxygenxml.docbook.checker.persister;

import com.oxygenxml.docbook.checker.CheckerInteractor;

/**
 * Save the state of the options.
 * 
 * @author Cosmin Duna
 */
public interface ContentPersister {

	/**
	 * Save content before close the dialog.
	 * @param interactor Checker interactor
	 */
	 void saveState(CheckerInteractor interactor);
	
	/**
	 * Load content before start the dialog.
	 * @param interactor Checker interactor
	 */
	 void loadState(CheckerInteractor interactor);
}
