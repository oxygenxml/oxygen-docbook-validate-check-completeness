package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.oxygenxml.docbookChecker.PlainCheckerInterctorImpl;
import com.oxygenxml.docbookChecker.reporters.ProblemReporterImpl;
import com.oxygenxml.docbookChecker.reporters.StatusReporterImpl;
import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.PlainParserCreator;

/**
 * JUnit for test internal links
 * 
 * @author intern4
 *
 */
public class InternalLinksCheckerTest {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		java.net.URL urlDb4 = new File("test-samples/broken-internal-links/testdb4.xml").toURI().toURL();
		java.net.URL urlDb5 = new File("test-samples/broken-internal-links/testdb5.xml").toURI().toURL();

		LinksChecker linkChecker = new LinksCheckerImp();

		// Problem reporters
		ProblemReporterImpl problemReporterDB4 = new ProblemReporterImpl();
		ProblemReporterImpl problemReporterDB5 = new ProblemReporterImpl();

		// start check
		linkChecker.check(new PlainParserCreator(), urlDb4.toString(),  new PlainCheckerInterctorImpl(), problemReporterDB4, new StatusReporterImpl());
		linkChecker.check(new PlainParserCreator(), urlDb5.toString(),  new PlainCheckerInterctorImpl(), problemReporterDB5, new StatusReporterImpl());

		// Sets with broken links.
		List<Link> brokenLinkDb4 = problemReporterDB4.getBrokenLinks();
		List<Link> brokenLinkDb5 = problemReporterDB5.getBrokenLinks();

		// Number of broken links.
		assertEquals("Should be a broken link.", 2, brokenLinkDb4.size());
		assertEquals("Should be 3 broken link.", 3, brokenLinkDb5.size());

		Iterator<Link> iterDb4 = brokenLinkDb4.iterator();
		Link foundLinkDb4 = iterDb4.next();

		Iterator<Link> iterDb5 = brokenLinkDb5.iterator();
		Link foundLinkDb5 = iterDb5.next();

		// First broken link founded
		assertEquals("myPara2", foundLinkDb4.getRef());
		assertEquals("myPara2Db5", foundLinkDb5.getRef());

		// Position of link
		assertEquals(12, foundLinkDb4.getLine());
		assertEquals(23, foundLinkDb5.getLine());
	}

}
