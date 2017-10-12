package com.oxygenxml.docbook.checker;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Interactor for checker.
 * @author intern4
 *
 */
public interface CheckerInteractor {
	
	/**
	 * Check if only the current opened XML document should be validated.
	 * @return <code>true</code> if check only the current opened XML document, <code>false</code>otherwise
	 */
	public boolean isCheckCurrentResource();
	/**
	 * Set if should validate the current opened resource or should be add other resources.
	 * @param <code>true</code> if only the current resource should be validated, <code>false</code>if should add resources for validation. 
	 */
	public void setCheckCurrentResource(boolean checkCurrent);

	/**
	 * Set files for validation.  
	 * @param rows List with resources
	 */
	public void setOtherFilesToCheck(List<String> resources);
	/**
	 * Get a list with files that should be validated.
	 * @return The list with files or an empty list.
	 */
	public List<String> getOtherFilesToCheck();

	/**
	 * Get the document type.
	 * @return the document type
	 */
	public String getDocumentType();
	
	/**
	 *Set the document type.
	 *@param documentType The document type to set.
	 */
	public void setDocumentType(String documentType);

	/**
	 * Get all document types
	 * @return A list with document types.
	 */
	public List<String> getAllDocumentTypes();
	
	/**
	 * Set all document types
	 * @param documentTypes Document types to be set.
	 */
	public void setAllDocumentTypes(List<String> documentTypes);
	
	/**
	 * Determine if should be used profile conditions in validation.  
	 * @return <code>true</code> profile conditions should be used, <code>false</code>>otherwise.
	 */
	public boolean isUsingProfile();
	/**
	 * Set if profile conditions should be used in validation.
	 * @return <code>true</code> conditions should be used, <code>false</code>>otherwise.
	 */
	public void setUseProfiligConditions(boolean state);
	
	/**
	 * Determine if should be used the manually configured condition set.
	 * @return <code>true</code> if should be used the manually configured condition set, <code>false</code>>otherwise.
	 */
	public boolean isUseManuallyConfiguredConditionsSet();
	
	/**
	 * Set if should be used the manually configured condition set
	 * @param useManuallyConfiguredConditionsSet <code>true</code> to use the manually configured conditions set, <code>false</code>otherwise. 
	 */
	public void setUseManuallyConfiguredConditionsSet(boolean useManuallyConfiguredConditionsSet);

	/**
	 * Get the current defined conditions as a map containing as keys the attribute names and as as values the attribute values to include when profiling. 
	 *@return A LinkedHashMap with conditions
	 */
	public LinkedHashMap<String, LinkedHashSet<String>> getDefinedConditions();
	
	/**
	 * Set the current defined conditions as a map containing as keys the attribute names and as as values the attribute values to include when profiling. 
	 *@param conditions The conditions to be set.
	 */
	public void setDefinedConditions(LinkedHashMap<String, String> conditions);
	
	/**
	 * Determine if should be reported undefined conditions.
	 * @return <code>true</code> if undefined conditions should be reported, <code>false</code> otherwise.
	 */
	public boolean isReporteUndefinedConditions();
	
	/**
	 * Set if should be reported undefined conditions.
	 * @return <code>true</code> if undefined conditions should be reported, <code>false</code> otherwise.
	 */
	public void setReporteUndefinedConditions(boolean state);
	
	/**
	 * Determine if external links should be checked.
	 * @return <code>true</code> if external links should be checked, <code>false</code> otherwise.
	 */
	public boolean isCheckExternal();
	/**
	 * Set if external links should be checked.
	 * @param state <code>true</code> if external links should be checked, <code>false</code> otherwise.
	 */	
	public void setCheckExternal(boolean state);

	/**
	 * Determine if images should be checked.
	 * @return <code>true</code> if images should be checked, <code>false</code> otherwise.
	 */
	public boolean isCheckImages();
	/**
	 * Set if images should be checked.
	 * @param state <code>true</code> if images should be checked, <code>false</code> otherwise.
	 */	
	public void setCheckImages(boolean state);
	
	/**
	 * Determine if internal links should be checked.
	 * @return <code>true</code> if internal links should be checked, <code>false</code> otherwise.
	 */
	public boolean isCheckInternal();
	/**
	 * Set if internal links should be checked.
	 * @param state <code>true</code> if internal links should be checked, <code>false</code> otherwise.
	 */	
	public void setCheckInternal(boolean state);
	
	/**
	 * Determine if hierarchy reports should be generated.
	 * @return <code>true</code> if hierarchy reports should be generated, <code>false</code> otherwise.
	 */
	boolean isGenerateHierarchyReport();
	/**
	 * Set if hierarchy reports should be generated.
	 * @param state <code>true</code> if hierarchy reports should be generated, <code>false</code> otherwise.
	 */	
	void setGenerateHierarchyReport(boolean state);

	
}
