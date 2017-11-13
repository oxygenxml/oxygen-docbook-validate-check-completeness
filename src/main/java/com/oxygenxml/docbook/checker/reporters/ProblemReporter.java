package com.oxygenxml.docbook.checker.reporters;

import java.net.URL;

import com.oxygenxml.docbook.checker.parser.AssemblyTopicId;
import com.oxygenxml.docbook.checker.parser.ConditionDetails;
import com.oxygenxml.docbook.checker.parser.Id;
import com.oxygenxml.docbook.checker.parser.Link;

/**
 * Interface for report and clear broken links and exceptions.
 * @author Cosmin Duna
 *
 */
public interface ProblemReporter {
	
	/**
	 * Report the broken link found.
	 * @param brokenLink The broken link.
	 * @param ex The exception found.
	 * @param tabKey The associated tab.
	 */
	void reportBrokenLinks(Link brokenLink, Exception ex, String tabKey);
	

	/**
	 * Report the assembly topic with problem.
	 * @param assemblyTopic The topic.
	 * @param ex The exception found.
	 * @param tabKey The associated tab.
	 */
	 void reportAssemblyTopic( AssemblyTopicId assemblyTopic ,  Exception ex,  String tabKey);
	
	/**
	 * Report the duplicate id found.
	 * @param duplicateId The duplicate id.
	 * @param message Message to be report.
	 * @param tabKey The associated tab.
	 */
	void reportDupicateId(Id duplicateId, String message, String tabKey);
	
	/**
	 * Report the exception found.
	 * @param ex The exception found.
	 * @param tabKey The associated tab.
	 * @param The document with problem.
	 */
	void reportException(Exception ex, String tabKey, URL document);

	/**
	 * Report the exception found.
	 * @param ex The exception found.
	 * @param tabKey The associated tab.
	 */
	void reportException(Exception ex, String tabKey);
	
	/**
	 * Report a undefined condition.
	 * @param conditionDetails The conditions with details found.
	 * @param tabKey The associated tab.
	 */
	void reportUndefinedConditions(ConditionDetails conditionDetails, String tabKey);
	
	/**
	 * Clear the problems reported in given tabKey.
	 * @param tabKey The tabKey.
	 */
	public void clearReportedProblems(String tabKey);

	
	
}


