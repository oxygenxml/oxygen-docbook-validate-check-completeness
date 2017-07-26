package com.oxygenxml.docbookChecker;

import com.oxygenxml.docbookChecker.view.CheckerFrame;

public class SettingsImpl implements Settings {

	private CheckerFrame checkerFrame;
 	
	public SettingsImpl(CheckerFrame checkerFrame) {
		this.checkerFrame = checkerFrame;
	}
	
	@Override
	public boolean isSetCheckExternal() {
		return checkerFrame.getCheckExternalLinksCBox().isSelected();
	}

	@Override
	public boolean isSetCheckInternal() {
		return checkerFrame.getCheckInternalLinksCbox().isSelected();
	}

	@Override
	public boolean isSetCheckImages() {
		return checkerFrame.getCheckImagesCBox().isSelected();
	}

}
