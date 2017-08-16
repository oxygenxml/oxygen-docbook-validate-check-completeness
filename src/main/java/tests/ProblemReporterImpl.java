package tests;

import java.util.ArrayList;
import java.util.List;

import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.ldocbookChecker.parser.Link;

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
	
 	@Override
 	public void reportBrokenLinks(Link brokenLink, String tabKey) {
		this.brokenLinks.add( brokenLink);
		System.out.println(brokenLink.toString() );
	}

	@Override
	public void reportException(Exception ex, String tabKey) {
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

	@Override
	public void clearReportedProblems(String tabKey) {
		brokenLinks = new ArrayList<Link>();
		exceptions = new ArrayList<Exception>();
	}

}
