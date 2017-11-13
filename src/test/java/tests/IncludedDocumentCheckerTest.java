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
 * JUnit for xi-included document
 * @author Cosmin Duna
 *
 */
public class IncludedDocumentCheckerTest {
	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		URL urlDb5 = new File("test-samples/xi-include/db5/sampleXInclude.xml").toURI().toURL();

		DocumentChecker linkChecker = new DocumentCheckerImp();

		// Problem reporters
		ProblemReporterImpl problemReporterDB5 = new ProblemReporterImpl();

		List<URL> urls = new ArrayList<URL>();
		urls.add(urlDb5);
	  
		//start check
		linkChecker.check(new PlainParserCreator(), new PlainProfilingConditionsInformations(), urls,  new PlainCheckerInteractorImpl(false, null), problemReporterDB5, new StatusReporterImpl(), new PlainWorkerReporter(),  new TranslatorImpl());

		// Sets with broken links.
		List<Link> brokenLinkDb5 = problemReporterDB5.getBrokenLinks();

		// Number of broken links
		assertEquals("Should be 4 broken links.", 4, brokenLinkDb5.size());

		Iterator<Link> iterDb5 = brokenLinkDb5.iterator();

		Link foundLinkDb5 = iterDb5.next();
		// first broken link
		assertEquals("http://www.xmdsadsal2.com/", foundLinkDb5.getRef());

		// Position of link
		assertEquals(9, foundLinkDb5.getLine());

		foundLinkDb5 = iterDb5.next();
		// second broken link
		assertEquals("1.png", foundLinkDb5.getRef());
		// Position of link
		assertEquals(5, foundLinkDb5.getLine());

	}
}
