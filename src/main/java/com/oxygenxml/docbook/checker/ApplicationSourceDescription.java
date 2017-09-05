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
public class OxygenSourceDescription {
	public static final String CONTEXTUAL = "contextual";
	public static final String TOOLBAR = "toolbar";
	public static final String PROJECT_MANAGER = "project";

	/**
	 * Source of action that start the validation. It can be CONTEXTUAL, TOOLBAR
	 * OR PROJECT_MANAGER
	 */
	private String source;
	/**
	 * The URL of current file opened in Oxygen
	 */
	private String currentUrl;

	/**
	 * The parent frame.
	 */
	private JFrame parrentFrame;
	
	/**
	 * The URLs of selected files in project manager.
	 */
	private List<String> selectedFilesInProject = new ArrayList<String>();

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
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

	public JFrame getParrentFrame() {
		return parrentFrame;
	}

	public void setParrentFrame(JFrame parrentFrame) {
		this.parrentFrame = parrentFrame;
	}
	
	

}
