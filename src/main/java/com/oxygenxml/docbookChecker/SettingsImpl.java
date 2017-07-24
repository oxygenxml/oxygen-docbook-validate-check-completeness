package com.oxygenxml.docbookChecker;

public class SettingsImpl implements Settings {
	private boolean externalCheck;

	@Override
	public boolean getCheckExternal() {
		return externalCheck;
	}

	@Override
	public void setCheckExternal(boolean checkExternal) {
		this.externalCheck = checkExternal;
	}
	
	
}
