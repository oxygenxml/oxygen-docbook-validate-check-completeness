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
	
	private String source;
	private String currentUrl;
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

	
	
	public void addUrl(String url){
		selectedUrls.add(url);
	}
	
	
}
