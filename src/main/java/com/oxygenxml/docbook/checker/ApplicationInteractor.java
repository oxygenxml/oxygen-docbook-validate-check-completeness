package com.oxygenxml.docbook.checker;

import javax.swing.JFrame;

/** 
 * Application interactor.
 * @author Cosmin Duna
 *
 */
public interface ApplicationInteractor {

	/**
	 * Set the application when operation is in progress or not.
	 * @param isInProgress <code>true</code> if the operation is in progress, <code>false</code>otherwise.
	 */
	 void setOperationInProgress(boolean isInProgress);

	/**
	 * Get the frame of application.
	 * @return The frame.
	 */
	 JFrame getApplicationFrame(); 

}
