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
 * JUnit for validation with profile conditions.
 * @author Cosmin Duna
 *
 */
public class ProfileCondistions3Test {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4
		URL urlDb4 = new File("test-samples/condition-links/testdb4.xml").toURI().toURL();

		DocumentChecker linkChecker = new DocumentCheckerImp();

		// Problem reporters
		ProblemReporterImpl problemReporterDB4 = new ProblemReporterImpl();

		//profile conditions 
		LinkedHashMap<String, String> conditions = new LinkedHashMap<String, String>();
		conditions.put("os", "windows");

		conditions.put("arch", "i386");


		List<URL> urls = new ArrayList<URL>();
		urls.add(urlDb4);
	  
		//start check
		linkChecker.check(new PlainParserCreator(), new PlainProfilingConditionsInformations(), urls, new PlainCheckerInteractorImpl(true, conditions), problemReporterDB4, new StatusReporterImpl(), new PlainWorkerReporter(), new TranslatorImpl());

		
		// Sets with broken links.
		List<Link> brokenLinkDb4 = problemReporterDB4.getBrokenLinks();

		// Number of broken links.
		assertEquals("Should be one broken links.", 1, brokenLinkDb4.size());

		Iterator<Link> iterDb4 = brokenLinkDb4.iterator();
		Link foundLinkDb4 = iterDb4.next();

		// First broken link founded
		assertEquals("mypara", foundLinkDb4.getRef());

		// Position of link
		assertEquals(21, foundLinkDb4.getLine());

	}

}
