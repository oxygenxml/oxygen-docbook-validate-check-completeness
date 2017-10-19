package tests;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import com.oxygenxml.docbook.checker.parser.AssemblyTopicId;
import com.oxygenxml.docbook.checker.parser.ConditionDetails;
import com.oxygenxml.docbook.checker.parser.Id;
import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;


/**
 * Save broken links and exceptions in lists and display in console.
 * Used in JUnits 
 * @author intern4
 *
 */
public class ProblemReporterImpl implements ProblemReporter{
	//list with broken links
	private List<Link> brokenLinks = new ArrayList<Link>();
	
	private List<Id> duplicateIds = new ArrayList<Id>();

	private List<AssemblyTopicId> topicsWithProblem = new ArrayList<AssemblyTopicId>();
	
	//list with exceptions
	private List<Exception> exceptions = new ArrayList<Exception>();
	
	//map with undefined conditions
	private LinkedHashMap<String, LinkedHashSet<String>> undefinedConditions = new LinkedHashMap<String, LinkedHashSet<String>>();
	
 	@Override
 	public void reportBrokenLinks(Link brokenLink, Exception ex,  String tabKey) {
 		System.out.println("************** broken link: "+ brokenLink.getRef());
		this.brokenLinks.add( brokenLink);
	}

	@Override
	public void reportException(Exception ex, String tabKey, URL document){
		exceptions.add(ex);
		System.out.println("******************ex: "+ ex.toString());
	}

	/**
	 * Get the list with broken links
	 * @return the list
	 */
	public List<Link> getBrokenLinks() {
		return brokenLinks;
	}

	/**
	 * Get the list with duplicate Ids
	 * @return the list
	 */
	public List<Id> getDuplicateIds() {
		return duplicateIds;
	}

	/**
	 * Get the list with topicsWithProblem
	 * @return the list
	 */
	public List<AssemblyTopicId> getTopicsWithProblem() {
		return topicsWithProblem;
	}
	
	/**
	 * get the list with exceptions
	 * @return the list
	 */
	public List<Exception> getExceptions() {
		return exceptions;
	}

	public LinkedHashMap<String, LinkedHashSet<String>> getUndefinedConditions(){
		return undefinedConditions;
	}
	
	@Override
	public void clearReportedProblems(String tabKey) {
	}


	@Override
	public void reportUndefinedConditions(ConditionDetails conditionDetails, String tabKey) {
		if(undefinedConditions.containsKey(conditionDetails.getAttribute())){
			undefinedConditions.get(conditionDetails.getAttribute()).add(conditionDetails.getValue());
		}
		else{
			LinkedHashSet<String> valueSet = new LinkedHashSet<String>();
			valueSet.add(conditionDetails.getValue());
			undefinedConditions.put(conditionDetails.getAttribute(), valueSet);
		}
		System.out.println("undefined condition: " + conditionDetails.getAttribute() +" " + conditionDetails.getValue());
		
	}

	@Override
	public void reportDupicateId(Id duplicateId, String message, String tabKey) {
		System.out.println("************** duplicate ID : "+ duplicateId.getId() + " on line: " + duplicateId.getLine());
		duplicateIds.add(duplicateId);
		
	}

	@Override
	public void reportAssemblyTopic(AssemblyTopicId assemblyTopic, Exception ex, String tabKey) {
		System.out.println("************** problem topic: "+ assemblyTopic.getId());
		topicsWithProblem.add(assemblyTopic);
		
	}

}
