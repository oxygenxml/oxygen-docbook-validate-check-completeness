package com.oxygenxml.docbook.checker;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

/**
 * Description of source of action that open the checker dialog.
 * 
 * @author intern4
 *
 */
public class ApplicationSourceDescription {
	
	/**
	 * Type of source.
	 * @author intern4
	 *
	 */
	public enum Source {
		CONTEXTUAL, TOOLBAR, PROJECT_MANAGER
	}
	
	/**
	 * Source of action that start the validation. It can be CONTEXTUAL, TOOLBAR
	 * OR PROJECT_MANAGER
	 */
	private Source source;
	/**
	 * The URL of current file opened in Oxygen
	 */
	private String currentUrl;

	/**
	 * The URLs of selected files in project manager.
	 */
	private List<String> selectedFilesInProject = new ArrayList<String>();

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public List<String> getSelectedFilesInProject() {
		return selectedFilesInProject;
	}

	public void setSelectedFilesInProject(List<String> urls) {
		this.selectedFilesInProject = urls;
	}

	public String getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}
	
}