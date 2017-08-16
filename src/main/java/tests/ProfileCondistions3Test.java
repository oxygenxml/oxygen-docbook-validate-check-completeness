package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinkType;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.PlainParserCreator;

public class ProfileCondistions3Test {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4
		java.net.URL urlDb4 = new File("test-samples/condition-links/testdb4.xml").toURI().toURL();

		LinksChecker linkChecker = new LinksCheckerImp();

		// Problem reporters
		ProblemReporterImpl problemReporterDB4 = new ProblemReporterImpl();

		//profile conditions 
		LinkedHashMap<String, String> conditions = new LinkedHashMap<String, String>();
		conditions.put("os", "windows");

		conditions.put("arch", "i386");


		List<String> urls = new ArrayList<String>();
		urls.add(urlDb4.toString());
	  
		//start check
		linkChecker.check(new PlainParserCreator(), urls, new PlainCheckerInteractorImpl(true, conditions), problemReporterDB4, new StatusReporterImpl(), new PlainWorkerReporter(), new TranslatorImpl());

		
		// Sets with broken links.
		List<Link> brokenLinkDb4 = problemReporterDB4.getBrokenLinks();

		// Number of broken links.
		assertEquals("Should be one broken links.", 1, brokenLinkDb4.size());

		Iterator<Link> iterDb4 = brokenLinkDb4.iterator();
		Link foundLinkDb4 = iterDb4.next();

		// First broken link founded
		assertEquals("mypara", foundLinkDb4.getRef());

		// Link type
		assertEquals(LinkType.INTERNAL, foundLinkDb4.getType());

		// Position of link
		assertEquals(21, foundLinkDb4.getLine());

	}

}
