package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.oxygenxml.docbookChecker.PlainSettingImpl;
import com.oxygenxml.docbookChecker.Settings;
import com.oxygenxml.docbookChecker.SettingsImpl;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.ProblemReporterImpl;
import com.oxygenxml.docbookChecker.reporters.StatusReporterImpl;
import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinkType;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.PlainParserCreator;

public class IncludedDocumentCheckerTest {
	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		java.net.URL urlDb5 = new File("test-samples/xi-include/db5/sampleXInclude.xml").toURI().toURL();

		LinksChecker linkChecker = new LinksCheckerImp();

		// Problem reporters
		ProblemReporterImpl problemReporterDB5 = new ProblemReporterImpl();

		// start check
		linkChecker.check(new PlainParserCreator(), urlDb5.toString(),  new PlainSettingImpl(), problemReporterDB5, new StatusReporterImpl());

		// Sets with broken links.
		List<Link> brokenLinkDb5 = problemReporterDB5.getBrokenLinks();

		// Number of broken links
		assertEquals("Should be 4 broken links.", 4, brokenLinkDb5.size());

		Iterator<Link> iterDb5 = brokenLinkDb5.iterator();

		Link foundLinkDb5 = iterDb5.next();
		// first broken link
		assertEquals("http://www.xmdsadsal2.com/", foundLinkDb5.getRef());
		// link type
		assertEquals(LinkType.EXTERNAL, foundLinkDb5.getType());
		// Position of link
		assertEquals(9, foundLinkDb5.getLine());

		foundLinkDb5 = iterDb5.next();
		// second broken link
		assertEquals("1.png", foundLinkDb5.getRef());
		// link type
		assertEquals(LinkType.IMAGE, foundLinkDb5.getType());
		// Position of link
		assertEquals(5, foundLinkDb5.getLine());

	}
}
