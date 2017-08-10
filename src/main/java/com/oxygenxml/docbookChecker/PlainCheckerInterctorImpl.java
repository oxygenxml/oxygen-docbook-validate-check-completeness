package com.oxygenxml.docbookChecker;

import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Implement used in JUnits
 * @author intern4
 *
 */
public class PlainCheckerInterctorImpl implements CheckerInteractor {
	
	/**
	 * Profile conditions from table. 
	 */
	Map<String, Set<String>> condTable ;
	
	/**
	 * Check using profiling conditions.
	 */
	boolean checkUsingProfiling;
		
	/**
	 * Constructor.
	 * @param checkUsingProfiling State for checkUsingProfiling checkBox
	 * @param condTable	Profile conditions used for check.
	 */
	public PlainCheckerInterctorImpl(boolean checkUsingProfiling, Map<String, Set<String>> condTable) {
		this.checkUsingProfiling = checkUsingProfiling;
		this.condTable = condTable;
	}
	
	@Override
	public boolean isSelectedCheckCurrent() {
		return true;
	}

	@Override
	public List<String> getFilesTableRows() {
		return null;
	}

	@Override
	public boolean isSelectedCheckExternal() {
		return true;
	}

	@Override
	public boolean isSelectedCheckImages() {
		return true;
	}

	@Override
	public boolean isSelectedCheckInternal() {
		return true;
	}

	@Override
	public void doClickOnCheckCurrentLink() {
		
	}

	@Override
	public void doClickOnCheckOtherLink() {
	}

	@Override
	public void setCheckExternal(boolean state) {
		
	}

	@Override
	public void setCheckImages(boolean state) {
		
	}

	@Override
	public void setCheckInternal(boolean state) {
	}

	@Override
	public void setRowsInFilesTable(List<String> rows) {
		
	}

	@Override
	public boolean isSelectedCheckUsingProfile() {
		return checkUsingProfiling;
	}

	@Override
	public Map<String, Set<String>> getConditionsTableRows() {
		return condTable;
	}

	@Override
	public boolean isSelectedConfigConditionsSet() {
		return true;
	}

	@Override
	public void doClickOnConfigConditionSet() {
		
	}

	@Override
	public void doClickOnCheckAllConbinations() {
		
	}

	@Override
	public void setRowsInConditionsTable(List<String[]> rows) {
	}

	@Override
	public void setUseProfiligConditions(boolean state) {
	}

	@Override
	public void doClickOnUseProfilingConditions() {
		
	}

}
