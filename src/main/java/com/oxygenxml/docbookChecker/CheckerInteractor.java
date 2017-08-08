package com.oxygenxml.docbookChecker;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * GUI interactor.
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
		public List<String> getFilesTableRows();
		
		/**
		 * Get a map with rows from conditions table. 
		 */
		public Map<String, Set<String>> getConditionsTableRows();
		
		/**
		 * Get the state of check using profiling conditions checkBox.  
		 * @return <code>true</code> is selected, <code>false</code>>if it's not.
		 */
		public boolean isSelectedCheckProfile();

		/**
		 * Get the state of configure conditions set radioButton
		 * @return <code>true</code> is selected, <code>false</code>>if it's not.
		 */
		public boolean isSelectedConfigConditionsSet();
		
		
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
		 * Do click on configure Conditions Set radioButton
		 */
		public void doClickOnConfigConditionSet();
		
		/**
		 * Do click on checkAllConbinationsOfConditionsSet radioButton
		 */
		public void doClickOnCheckAllConbinations();
		
		/**
		 * Set the state of UseProfilingConditions checkButton
		 * @return <code>true</code> is selected, <code>false</code>>if it's not.
		 */
		public void setUseProfiligConditions(boolean state);
		
		/**
		 * Do click on UseProfilingConditions checkButton
		 */
		public void doClickOnUseProfilingConditions();
		
		
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
		
		/**
		 * Set rows in conditions table
		 * @param rows List with rows
		 */
		public void setRowsInConditionsTable(List<String[]> rows);

		
}
