package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;

import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.validator.DocumentChecker;
import com.oxygenxml.docbook.checker.validator.DocumentCheckerImp;

public class AssemblyLinksTest {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		java.net.URL urlFile = new File("test-samples/assembly-files/assembly.xml").toURI().toURL();

		DocumentChecker linkChecker = new DocumentCheckerImp();

		// Problem reporters
		ProblemReporterImpl problemReporter = new ProblemReporterImpl();

		List<String> urls = new ArrayList<String>();
		urls.add(urlFile.toString());

		// profile conditions
		LinkedHashMap<String, String> conditions = new LinkedHashMap<String, String>();

		conditions.put("arch", "i386");

		// start check
		linkChecker.check(new PlainParserCreator(), new PlainProfilingConditionsInformations(), urls,
				new PlainCheckerInteractorImpl(true, conditions), problemReporter, new StatusReporterImpl(),
				new PlainWorkerReporter(), new TranslatorImpl());

		// Sets with broken links.
		List<Link> brokenLinks = problemReporter.getBrokenLinks();

		for (int i = 0; i < brokenLinks.size(); i++) {
			System.out.println("++++++++++++++"+brokenLinks.get(i).getRef());
			
		}
		// Number of broken links
		assertEquals("Should be 3 broken links.", 3, brokenLinks.size());

		
		Iterator<Link> iter = brokenLinks.iterator();

		Link foundLink = iter.next();
		// first broken link
		assertEquals("filterTopic", foundLink.getRef());

		// Position of link
		assertEquals(17, foundLink.getLine());

		foundLink = iter.next();
		// second broken link
		assertEquals("inexistentTopic", foundLink.getRef());
		// Position of link
		assertEquals(20, foundLink.getLine());

		foundLink = iter.next();
		// second broken link
		assertEquals("http://www.google2.com", foundLink.getRef());
		// Position of link
		assertEquals(7, foundLink.getLine());

	
	}
}
