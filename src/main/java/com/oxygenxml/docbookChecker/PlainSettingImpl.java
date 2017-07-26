package com.oxygenxml.docbookChecker;

public class PlainSettingImpl implements Settings{

	@Override
	public boolean isSetCheckExternal() {
		return true;
	}

	@Override
	public boolean isSetCheckInternal() {
		return true;
	}

	@Override
	public boolean isSetCheckImages() {
		return true;
	}

}
