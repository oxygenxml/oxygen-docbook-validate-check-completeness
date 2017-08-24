package tests;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

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
	
	//list with exceptions
	private List<Exception> exceptions = new ArrayList<Exception>();
	
	//map with undefined conditions
	private LinkedHashMap<String, LinkedHashSet<String>> undefinedConditions = new LinkedHashMap<String, LinkedHashSet<String>>();
	
 	@Override
 	public void reportBrokenLinks(Link brokenLink, String tabKey) {
		this.brokenLinks.add( brokenLink);
	}

	@Override
	public void reportException(Exception ex, String tabKey, String document){
		exceptions.add(ex);
		System.out.println(ex.toString());
	}

	/**
	 * get the list with broken links
	 * @return the list
	 */
	public List<Link> getBrokenLinks() {
		return brokenLinks;
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
		brokenLinks = new ArrayList<Link>();
		exceptions = new ArrayList<Exception>();
	}


	@Override
	public void reportUndefinedConditions(String attribute, String value, String tabKey) {
		if(undefinedConditions.containsKey(attribute)){
			undefinedConditions.get(attribute).add(value);
		}
		else{
			LinkedHashSet<String> valueSet = new LinkedHashSet<String>();
			valueSet.add(value);
			undefinedConditions.put(attribute, valueSet);
		}
		System.out.println("undefined condition: " + attribute +" " + value);
		
	}

}
