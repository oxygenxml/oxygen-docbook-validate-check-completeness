package com.oxygenxml.docbookChecker;
/**
 * Interface for GUI settings.
 * @author intern4
 *
 */
public interface Settings {
	
	/**
	 * Get the state of checkExternalLink checkBox
	 * @return
	 */
	public boolean isCheckExternal();
	
	/**
	 * Set the state of checkExternalLink in a variable.
	 * @param checkExternal
	 */
	public void setCheckExternal(boolean checkExternal);

}
