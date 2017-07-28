package com.oxygenxml.docbookChecker.view;

import java.io.File;

public interface FileChooserCreator {
	
	public File[] createFileChooser(String title, String aproveButtonName);
}
