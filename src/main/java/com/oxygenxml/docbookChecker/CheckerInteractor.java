package com.oxygenxml.docbookChecker;

import java.util.List;
/**
 * GUI interactor
 * @author intern4
 *
 */
public interface CheckerInteractor {
		/**
		 * Get the state of checkCurrentFile radioButton
		 * @return <code>true</code> is selected, <code>false</code>>if it's not.
		 */
		public boolean isSelectedCheckCurrent();
		
		/**
		 * Get a list with files table rows.
		 * @return the list
		 */
		public List<String> getTableRows();
		
		/**
		 * Get the state of checkExternalLinks checkButton
		 * @return <code>true</code> is selected, <code>false</code>>if it's not.
		 */
		public boolean isSelectedCheckExternal();
		
		/**
		 * Get the state of checkImages checkButton
		 * @return <code>true</code> is selected, <code>false</code>>if it's not.
		 */
		public boolean isSelectedCheckImages();
		
		/**
		 * Get the state of checkInternal checkButton
		 * @return <code>true</code> is selected, <code>false</code>>if it's not.
		 */
		public boolean isSelectedCheckInternal();
		
		/**
		 * Do click on checkCurrentFile radioButton
		 */
		public void doClickOnCheckCurrentLink();
		
		/**
		 * Do click on checkOtherFiles radioButton
		 */
		public void doClickOnCheckOtherLink();
		
		/**
		 * Set the state of checkExternalLinks checkButton
		 * @return <code>true</code> is selected, <code>false</code>>if it's not.
		 */
		public void setCheckExternal(boolean state);
		
		/**
		 * Set the state of checkImages checkButton
		 * @return <code>true</code> is selected, <code>false</code>>if it's not.
		 */
		public void setCheckImages(boolean state);
	
		/**
		 * Set the state of checkInternalLinks checkButton
		 * @return <code>true</code> is selected, <code>false</code>>if it's not.
		 */
		public void setCheckInternal(boolean state);
		
		/**
		 * Set rows in Files table
		 * @param rows List with rows
		 */
		public void setRowsInFilesTable(List<String> rows);
		
		
}
