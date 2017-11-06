package com.oxygenxml.docbook.checker.hierarchy.report;

import java.net.URL;

/**
 * Identifier for HierarchyReportStorageTreeNode 
 * @author intern4
 *
 */
public class HierarchyReportStorageTreeNodeId {

	/**
	 * The url of the document.
	 */
	private URL documentUrl;
	
	/**
	 * Name of used condition set.
	 */
	private String conditionSet;

	/**
	 * Constructor.
	 * @param documentUrl documentUrl.
	 * @param conditionSet condition set name.
	 */
	public HierarchyReportStorageTreeNodeId(URL documentUrl, String conditionSet) {
		this.documentUrl = documentUrl;
		this.conditionSet = conditionSet;
	}

	//Getters
	public URL getDocumentUrl() {
		return documentUrl;
	}

	public String getConditionSet() {
		return conditionSet;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HierarchyReportStorageTreeNodeId){
			HierarchyReportStorageTreeNodeId nodeId = (HierarchyReportStorageTreeNodeId)obj;
			return this.documentUrl.equals(nodeId.documentUrl) && conditionSet.equals(nodeId.conditionSet);
		}else{
			return super.equals(obj);
		}
	}
	
	
	@Override
	public String toString() {
		return  documentUrl + " " + conditionSet;
	}

	@Override
	public int hashCode() {
		return (getDocumentUrl().toString()+conditionSet).hashCode();
	}
}
