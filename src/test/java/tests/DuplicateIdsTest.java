package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;

import com.oxygenxml.docbook.checker.parser.Id;
import com.oxygenxml.docbook.checker.validator.DocumentChecker;
import com.oxygenxml.docbook.checker.validator.DocumentCheckerImp;

/**
 * JUnit for duplicate Ids validation.
 * @author intern4
 *
 */
public class DuplicateIdsTest {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		java.net.URL urlFile = new File("test-samples/duplicate-ids/assembly.xml").toURI().toURL();

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
		List<Id> duplicateIDs = problemReporter.getDuplicateIds();

		// Number of broken links
		assertEquals("Should be 2 broken links.", 2, duplicateIDs.size());

		
		Iterator<Id> iter = duplicateIDs.iterator();

		Id currentDuplicateID = iter.next();
		// first broken link
		assertEquals("myPara2", currentDuplicateID.getId());

		// Position of link
		assertEquals(10, currentDuplicateID.getLine());

		currentDuplicateID = iter.next();
		// second broken link
		assertEquals("myPara", currentDuplicateID.getId());
		// Position of link
		assertEquals(7, currentDuplicateID.getLine());

		assertEquals("file:/D:/docbook-validate-check-completeness/test-samples/duplicate-ids/t3.xml", currentDuplicateID.getLinkFoundDocumentUrl());
	
	}
}
