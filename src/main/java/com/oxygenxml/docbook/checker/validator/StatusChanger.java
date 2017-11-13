package com.oxygenxml.docbook.checker.validator;

/**
 * Changer for status variable.
 * @author Cosmin Duna
 *
 */
public interface StatusChanger {

	/**
	 * Change the status  with the given new status.
	 * @param newStatus
	 */
	public void changeStatus(String newStatus);
}
