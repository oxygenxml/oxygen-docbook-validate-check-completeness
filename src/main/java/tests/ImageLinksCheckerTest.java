package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.oxygenxml.docbookChecker.reporters.ProblemReporterImpl;
import com.oxygenxml.docbookChecker.reporters.StatusReporterImpl;
import com.oxygenxml.docbookChecker.PlainCheckerInterctorImpl;
import com.oxygenxml.docbookChecker.PlainWorkerReporter;
import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.PlainParserCreator;

/**
 * Junit for test images links
 * @author intern4
 *
 */
public class ImageLinksCheckerTest {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		java.net.URL urlDb4 = new File("test-samples/broken-image/testdb4.xml").toURI().toURL();
		java.net.URL urlDb5 = new File("test-samples/broken-image/testdb5.xml").toURI().toURL();
	
		
	  LinksChecker linkChecker = new LinksCheckerImp();
	
		//Problem reporters
	  ProblemReporterImpl problemReporterDB4 = new ProblemReporterImpl();
	  ProblemReporterImpl problemReporterDB5 = new ProblemReporterImpl();
		
		List<String> urls = new ArrayList<String>();
		urls.add(urlDb4.toString());
	  
		//start check
		linkChecker.check(new PlainParserCreator(), urls, new PlainCheckerInterctorImpl(false, null), problemReporterDB4, new StatusReporterImpl(), new PlainWorkerReporter());

		urls.clear();
		urls.add(urlDb5.toString());
		linkChecker.check(new PlainParserCreator(), urls,  new PlainCheckerInterctorImpl(false, null), problemReporterDB5, new StatusReporterImpl(), new PlainWorkerReporter());

		// Sets with broken links.
		List<Link> brokenLinkDb4 = problemReporterDB4.getBrokenLinks();
		List<Link> brokenLinkDb5 = problemReporterDB5.getBrokenLinks();

		System.out.println(brokenLinkDb4.toString());
	  
	  //Number of broken links.
	  assertEquals("Should be a broken link." ,2 , brokenLinkDb4.size());
	  assertEquals("Should be 2 broken link." ,2 , brokenLinkDb5.size());
	  
	  Iterator<Link> iterDb4 = brokenLinkDb4.iterator();
	  Link foundLinkDb4 = iterDb4.next();
	  
	  Iterator<Link> iterDb5 = brokenLinkDb5.iterator();
	  Link foundLinkDb5 = iterDb5.next();
	
	  //First broken link founded
	  assertEquals("primul.png", foundLinkDb4.getRef());
	  assertEquals("primul.png", foundLinkDb5.getRef());
	  
	  //Position of link
	  assertEquals(10, foundLinkDb4.getLine());
	  assertEquals(28, foundLinkDb5.getLine());
	}

}
