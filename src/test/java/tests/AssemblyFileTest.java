package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;

import com.oxygenxml.docbook.checker.parser.AssemblyTopicId;
import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.validator.DocumentChecker;
import com.oxygenxml.docbook.checker.validator.DocumentCheckerImp;

/**
 * JUnit for assembly file in DocBook.
 * @author intern4
 *
 */
public class AssemblyFileTest {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		URL urlFile = new File("test-samples/assembly-files/assembly.xml").toURI().toURL();

		DocumentChecker linkChecker = new DocumentCheckerImp();

		// Problem reporters
		ProblemReporterImpl problemReporter = new ProblemReporterImpl();

		List<URL> urls = new ArrayList<URL>();
		urls.add(urlFile);

		// profile conditions
		LinkedHashMap<String, String> conditions = new LinkedHashMap<String, String>();

		conditions.put("arch", "i386");

		// start check
		linkChecker.check(new PlainParserCreator(), new PlainProfilingConditionsInformations(), urls,
				new PlainCheckerInteractorImpl(true, conditions), problemReporter, new StatusReporterImpl(),
				new PlainWorkerReporter(), new TranslatorImpl());

		// Sets with broken links.
		List<Link> brokenLinks = problemReporter.getBrokenLinks();
		// Sets with broken links.
		List<AssemblyTopicId> topicsWithProblem = problemReporter.getTopicsWithProblem();

		// Number of broken links
		assertEquals("Should be 4 broken links.", 4, brokenLinks.size()+ topicsWithProblem.size());
		
		Iterator<Link> iter = brokenLinks.iterator();

		Link foundLink = iter.next();
		// broken link
		assertEquals("filterTopic", foundLink.getRef());

		// Position of link
		assertEquals(17, foundLink.getLine());

		foundLink = iter.next();
		//  broken link
		assertEquals("inexistentTopic", foundLink.getRef());
		// Position of link
		assertEquals(20, foundLink.getLine());

		foundLink = iter.next();
		//  broken link
		assertEquals("http://www.google2.com", foundLink.getRef());
		// Position of link
		assertEquals(7, foundLink.getLine());

		
		Iterator<AssemblyTopicId> iter2 = topicsWithProblem.iterator();
		AssemblyTopicId topic = iter2.next();
		//  broken link
		assertEquals("file:/D:/docbook-validate-check-completeness/test-samples/assembly-files/t4.xml", topic.getAbsoluteLocation().toString());
		// Position of link
		assertEquals(9, topic.getLine());
	}
}
