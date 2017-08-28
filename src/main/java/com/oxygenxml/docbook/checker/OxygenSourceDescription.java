package com.oxygenxml.docbook.checker;

import java.util.ArrayList;
import java.util.List;

/**
 * Description of source of action that open the checker dialog.
 * @author intern4
 *
 */
public class OxygenSourceDescription {
	public static final String CONTEXTUAL = "contextual";
	public static final String TOOLBAR = "toolbar";
	public static final String PROJECT_MANAGER = "project";
	
	/**
	 * Source of action that start the validation.
	 * It can be CONTEXTUAL, TOOLBAR OR PROJECT_MANAGER
	 */
	private String source;
	/**
	 * The URL of current file opened in Oxygen
	 */
	private String currentUrl;
	
	/**
	 * The URLs of selected files in project manager.
	 */
	private List<String> selectedUrls = new ArrayList<String>();
	
	
	public String getSource() {
		return source;
	}
	public List<String> getSelectedUrls() {
		return selectedUrls;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public void setSelectedUrls(List<String> urls) {
		this.selectedUrls = urls;
	}
	
	public String getCurrentUrl() {
		return currentUrl;
	}
	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}

	
	
}
