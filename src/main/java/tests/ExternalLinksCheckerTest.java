package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.oxygenxml.docbookChecker.reporters.ProblemReporterImpl;
import com.oxygenxml.docbookChecker.reporters.StatusReporterImpl;
import com.oxygenxml.docbookChecker.PlainCheckerInterctorImpl;
import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinkType;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.PlainParserCreator;

/**
 * Junit for test external links
 * 
 * @author intern4
 *
 */
public class ExternalLinksCheckerTest {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		java.net.URL urlDb4 = new File("test-samples/broken-external-link/testdb4.xml").toURI().toURL();
		java.net.URL urlDb5 = new File("test-samples/broken-external-link/testdb5.xml").toURI().toURL();


		LinksChecker externalLinkChecker = new LinksCheckerImp();

		//Problem reporters
		ProblemReporterImpl problemReporterDB4 = new ProblemReporterImpl();
		ProblemReporterImpl problemReporterDB5 = new ProblemReporterImpl();
		
		//start check
		externalLinkChecker.check(new PlainParserCreator(), urlDb4.toString(),  new PlainCheckerInterctorImpl(false, null), problemReporterDB4, new StatusReporterImpl());
		externalLinkChecker.check(new PlainParserCreator(), urlDb5.toString(),  new PlainCheckerInterctorImpl(false, null), problemReporterDB5, new StatusReporterImpl());

		// Sets with broken links.
		List<Link> brokenLinkDb4 = problemReporterDB4.getBrokenLinks();
		List<Link> brokenLinkDb5 = problemReporterDB5.getBrokenLinks();

		System.out.println(brokenLinkDb4.toString());
		
		// Number of broken links.
		assertEquals("Should be a broken link.", 1, brokenLinkDb4.size());
		assertEquals("Should be a broken link.", 1, brokenLinkDb5.size());

		Iterator<Link> iterDb4 = brokenLinkDb4.iterator();
		Link foundLinkDb4 = iterDb4.next();

		Iterator<Link> iterDb5 = brokenLinkDb5.iterator();
		Link foundLinkDb5 = iterDb5.next();

		// First broken link founded
		assertEquals("http://www.google2.com", foundLinkDb4.getRef());
		assertEquals("http://www.google2.com", foundLinkDb5.getRef());

		// Link type
		assertEquals(LinkType.EXTERNAL, foundLinkDb4.getType());
		assertEquals(LinkType.EXTERNAL, foundLinkDb5.getType());

		// Position of link
		assertEquals(9, foundLinkDb4.getLine());
		assertEquals(22, foundLinkDb5.getLine());
	}

}
