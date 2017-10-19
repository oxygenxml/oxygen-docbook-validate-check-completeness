package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.validator.DocumentChecker;
import com.oxygenxml.docbook.checker.validator.DocumentCheckerImp;

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
		URL urlDb4 = new File("test-samples/broken-internal-links/testdb4.xml").toURI().toURL();
		URL urlDb5 = new File("test-samples/broken-internal-links/testdb5.xml").toURI().toURL();

		DocumentChecker linkChecker = new DocumentCheckerImp();

		// Problem reporters
		ProblemReporterImpl problemReporterDB4 = new ProblemReporterImpl();
		ProblemReporterImpl problemReporterDB5 = new ProblemReporterImpl();

		List<URL> urls = new ArrayList<URL>();
		urls.add(urlDb4);
	  
		//start check
		linkChecker.check(new PlainParserCreator(), new PlainProfilingConditionsInformations(),  urls, new PlainCheckerInteractorImpl(false, null), problemReporterDB4, new StatusReporterImpl(), new PlainWorkerReporter(),  new TranslatorImpl());

		urls.clear();
		urls.add(urlDb5);
		linkChecker.check(new PlainParserCreator(), new PlainProfilingConditionsInformations(), urls,  new PlainCheckerInteractorImpl(false, null), problemReporterDB5, new StatusReporterImpl(), new PlainWorkerReporter(),  new TranslatorImpl());

		// Sets with broken links.
		List<Link> brokenLinkDb4 = problemReporterDB4.getBrokenLinks();
		List<Link> brokenLinkDb5 = problemReporterDB5.getBrokenLinks();

		// Number of broken links.
		assertEquals("Should be 2 broken link.", 2, brokenLinkDb4.size());
		System.out.println("*******"+ brokenLinkDb5.toString());
		assertEquals("Should be 3 broken link.", 3, brokenLinkDb5.size());

		Iterator<Link> iterDb4 = brokenLinkDb4.iterator();
		Link foundLinkDb4 = iterDb4.next();

		Iterator<Link> iterDb5 = brokenLinkDb5.iterator();
		Link foundLinkDb5 = iterDb5.next();

		// First broken link founded
		assertEquals("myPara3", foundLinkDb4.getRef());
		assertEquals("myPara2", foundLinkDb5.getRef());

		// Position of link
		assertEquals(12, foundLinkDb4.getLine());
		assertEquals(24, foundLinkDb5.getLine());
	}

}
