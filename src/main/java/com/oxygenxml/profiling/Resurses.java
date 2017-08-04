package com.oxygenxml.profiling;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Resurses {
	//TODO to use
	private Map<String, Set<String>> documentsConditions = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> allConditions = new HashMap<String, Set<String>>();
	private Map<String, Map<String, Set<String>>> allContitionsSets = new HashMap<String, Map<String,Set<String>>>();
	
	//getters and setters
	public Map<String, Set<String>> getDocumentsConditions() {
		return documentsConditions;
	}
	public Map<String, Set<String>> getAllConditions() {
		return allConditions;
	}
	public Map<String, Map<String, Set<String>>> getAllContitionsSets() {
		return allContitionsSets;
	}
	public void setDocumentsConditions(Map<String, Set<String>> documentsConditions) {
		this.documentsConditions = documentsConditions;
	}
	public void setAllConditions(Map<String, Set<String>> allConditions) {
		this.allConditions = allConditions;
	}
	public void setAllContitionsSets(Map<String, Map<String, Set<String>>> allContitionsSets) {
		this.allContitionsSets = allContitionsSets;
	} 

	
}
