package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;

import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.validator.DocumentChecker;
import com.oxygenxml.docbook.checker.validator.DocumentCheckerImp;

/**
 * JUnit for test external links
 * 
 * @author intern4
 *
 */
public class ExternalLinksCheckerTest {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		URL urlDb4 = new File("test-samples/broken-external-link/testdb4.xml").toURI().toURL();
		URL urlDb5 = new File("test-samples/broken-external-link/testdb5.xml").toURI().toURL();


		DocumentChecker externalLinkChecker = new DocumentCheckerImp();

		//Problem reporters
		ProblemReporterImpl problemReporterDB4 = new ProblemReporterImpl();
		ProblemReporterImpl problemReporterDB5 = new ProblemReporterImpl();
		
		List<URL> urls = new ArrayList<URL>();
		urls.add(urlDb4);
		
		//start check
		externalLinkChecker.check(new PlainParserCreator(), new PlainProfilingConditionsInformations(), urls,  new PlainCheckerInteractorImpl(false, new LinkedHashMap<String, String>() ), problemReporterDB4, new StatusReporterImpl(), new PlainWorkerReporter(), new TranslatorImpl());
		
		urls.clear();
		urls.add(urlDb5);
		externalLinkChecker.check(new PlainParserCreator(), new PlainProfilingConditionsInformations(), urls,  new PlainCheckerInteractorImpl(false, new LinkedHashMap<String, String>()), problemReporterDB5, new StatusReporterImpl(), new PlainWorkerReporter(), new TranslatorImpl());

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

		// Position of link
		assertEquals(9, foundLinkDb4.getLine());
		assertEquals(22, foundLinkDb5.getLine());
	}

}
