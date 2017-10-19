package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Test;

import com.oxygenxml.docbook.checker.validator.DocumentChecker;
import com.oxygenxml.docbook.checker.validator.DocumentCheckerImp;

/**
 * JUnit for profile conditions attributes that are undefined in preferences.
 * @author intern4
 *
 */
public class UndefinedConditionsTest {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4
		URL urlDb4 = new File("test-samples/undefined-conditions/testdb4.xml").toURI().toURL();

		DocumentChecker linkChecker = new DocumentCheckerImp();

		// Problem reporters
		ProblemReporterImpl problemReporterDB4 = new ProblemReporterImpl();

		List<URL> urls = new ArrayList<URL>();
		urls.add(urlDb4);
	  
		//start check
		linkChecker.check(new PlainParserCreator(), new PlainProfilingConditionsInformations(), urls, new PlainCheckerInteractorImpl(false, null), problemReporterDB4, new StatusReporterImpl(), new PlainWorkerReporter(),  new TranslatorImpl());

		// Map with undefinedConditions.
		LinkedHashMap<String,LinkedHashSet<String>> undefinedConditions = problemReporterDB4.getUndefinedConditions();

		// Number of broken links.
		assertEquals("Should be 2 undefined conditions attributes.", 2, undefinedConditions.size());

		assertEquals("Should be one value for this attribute.", 1, undefinedConditions.get("os").size());
		
		
		assertTrue(undefinedConditions.containsKey("os"));
		
		assertTrue(undefinedConditions.get("os").contains("abcd"));
		
		assertTrue(undefinedConditions.get("arch").contains("undefined"));
			
	}

}
