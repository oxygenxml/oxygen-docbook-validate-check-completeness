package com.oxygenxml.docbookChecker;

import java.util.List;
/**
 * Implement used in Units
 * @author intern4
 *
 */
public class PlainCheckerInterctorImpl implements CheckerInteractor {

	@Override
	public boolean isSelectedCheckCurrent() {
		return true;
	}

	@Override
	public List<String> getTableRows() {
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

}
