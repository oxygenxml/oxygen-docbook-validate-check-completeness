package tests;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import com.oxygenxml.docbook.checker.CheckerInteractor;

/**
 * Implement used in JUnits
 * @author intern4
 *
 */
public class PlainCheckerInteractorImpl implements CheckerInteractor {
	
	/**
	 * Profile conditions from table. 
	 */
	LinkedHashMap<String, String> condTable ;
	
	/**
	 * Check using profiling conditions.
	 */
	boolean checkUsingProfiling;
		
	/**
	 * Constructor.
	 * @param checkUsingProfiling State for checkUsingProfiling checkBox
	 * @param condTable	Profile conditions used for check.
	 */
	public PlainCheckerInteractorImpl(boolean checkUsingProfiling, LinkedHashMap<String, String> condTable) {
		this.checkUsingProfiling = checkUsingProfiling;
		this.condTable = condTable;
	}
	
	@Override
	public boolean isCheckCurrentResource() {
		return true;
	}

	@Override
	public List<String> getOtherFilesToCheck() {
		return null;
	}

	@Override
	public boolean isCheckExternal() {
		return true;
	}

	@Override
	public boolean isCheckImages() {
		return true;
	}

	@Override
	public boolean isCheckInternal() {
		return true;
	}
	@Override
	public void setCheckExternal(boolean state) {
		
	}

	@Override
	public void setCheckImages(boolean state) {
		
	}

	@Override
	public void setCheckInternal(boolean state) {
	}

	@Override
	public LinkedHashMap<String, LinkedHashSet<String>> getDefinedConditions() {
		LinkedHashMap<String, LinkedHashSet<String>>  toReturn = new LinkedHashMap<String, LinkedHashSet<String>>();
		Iterator<String> iterKey = condTable.keySet().iterator();
		
		while(iterKey.hasNext()){
			String key = iterKey.next();
			
			LinkedHashSet<String> valueSet = new LinkedHashSet<String>(Arrays.asList(condTable.get(key).split(",")));
			toReturn.put(key, valueSet);
		}
		
		
		return toReturn;
	}


	@Override
	public void setCheckCurrentResource(boolean checkCurrent) {
	}

	@Override
	public void setOtherFilesToCheck(List<String> resources) {
	}

	@Override
	public boolean isUsingProfile() {
		return checkUsingProfiling;
	}

	@Override
	public boolean isUseManuallyConfiguredConditionsSet() {
		return true;
	}

	@Override
	public void setUseManuallyConfiguredConditionsSet(boolean useManuallyConfiguredConditionsSet) {
	}

	@Override
	public void setDefinedConditions(LinkedHashMap<String, String> conditions) {
		this.condTable = conditions;
	}

	@Override
	public void setUseProfiligConditions(boolean state) {
	}

	@Override
	public boolean isReporteUndefinedConditions() {
		return true;
	}

	@Override
	public void setReporteUndefinedConditions(boolean state) {
		
	}

	@Override
	public String getDocumentType() {
		return "DocBook 5";
	}

	@Override
	public void setDocumentType(String documentType) {
		
	}

	@Override
	public List<String> getAllDocumentTypes() {
		return null;
	}

	@Override
	public void setAllDocumentTypes(List<String> documentTypes) {
		
	}

	@Override
	public boolean isGenerateHierarchyReport() {
		return false;
	}

	@Override
	public void setGenerateHierarchyReport(boolean state) {
		
	}


}
