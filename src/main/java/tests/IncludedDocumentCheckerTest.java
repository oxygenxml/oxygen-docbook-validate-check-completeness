package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.LinkDetails;

public class IncludedDocumentCheckerTest {
	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		java.net.URL urlDb5 = new File("D:/docbook-validate-check-completeness/test-samples/xi-include/db5/sampleXInclude.xml").toURI().toURL();
	
	  LinksChecker externalLinkChecker = new LinksCheckerImp();
	
	  //Sets with broken links
	  List<Link> resultsDb5 = externalLinkChecker.check(urlDb5, true);
	  
	  //Number of broken links
	  assertEquals("Should be 6 broken links." ,6 , resultsDb5.size());
	  
	
	  
	  Iterator<Link> iterDb5 = resultsDb5.iterator();

	  Link foundLinkDb5 = iterDb5.next();

	  while(!foundLinkDb5.getType().equals("Document")){
	  	foundLinkDb5 = iterDb5.next();
	  }
	  //Position of link
	  assertEquals("section2.xml", foundLinkDb5.getRef());
	  assertEquals(6, foundLinkDb5.getLine());
	
	  foundLinkDb5 = iterDb5.next();
	  while(!foundLinkDb5.getType().equals("Document")){
	  	foundLinkDb5 = iterDb5.next();
	  }
	  assertEquals("section3.xml", foundLinkDb5.getRef());
	  assertEquals(6, foundLinkDb5.getLine());
	  
	}
}
