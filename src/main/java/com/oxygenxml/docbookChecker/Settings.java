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
	public boolean isSetCheckExternal();

	/**
	 * Get the state of checkInternal checkBox
	 * @return
	 */
	public boolean isSetCheckInternal();
	/**
	 * Get the state of checkImages checkBox
	 * @return
	 */
	public boolean isSetCheckImages();

	
}
