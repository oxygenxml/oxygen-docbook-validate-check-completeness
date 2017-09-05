package com.oxygenxml.docbook.checker;

import javax.swing.JFrame;

/** 
 * Application interactor.
 * @author intern4
 *
 */
public interface ApplicationInteractor {

	/**
	 * Set the application when operation is in progress or not.
	 * @param state <code>true</code> if the operation is in progress, <code>false</code>otherwise.
	 */
	public void setOperationInProgress(boolean state);

	/**
	 * Get the frame of application.
	 * @return the frame.
	 */
	public JFrame getOxygenFrame(); 

}
