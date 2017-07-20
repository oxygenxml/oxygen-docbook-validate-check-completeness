package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.junit.Test;

import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.Results;

public class IncludedDocumentCheckerTest {
	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		java.net.URL urlDb5 = new File("D:/docbook-validate-check-completeness/test-samples/xi-include/db5/sampleXInclude.xml").toURI().toURL();
	
	  LinksChecker externalLinkChecker = new LinksCheckerImp();
	
	  //Sets with broken links
	  Results resultsDb5 = externalLinkChecker.check(urlDb5);
	  
	  //Number of broken links
	  assertEquals("Should be 2 broken links." ,2 , resultsDb5.getIncludedDocumentsLinks().size());
	  assertEquals("Should be a broken link." ,1 , resultsDb5.getExternalLinks().size());
	  assertEquals("Shouldn't be a broken link." ,0 , resultsDb5.getInternalLinks().size());
	  assertEquals("Shouldn be 3 broken links." ,3 , resultsDb5.getImgLinks().size());
	  
	
	  
	  Iterator<Link> iterDb5 = resultsDb5.getIncludedDocumentsLinks().iterator();
	  Link foundLinkDb5 = iterDb5.next();
	
	  //Position of link
	  assertEquals("section2.xml", foundLinkDb5.getRef());
	  assertEquals(6, foundLinkDb5.getLine());
	
	  foundLinkDb5 = iterDb5.next();
	  assertEquals("section3.xml", foundLinkDb5.getRef());
	  assertEquals(6, foundLinkDb5.getLine());
	  
	}
}
