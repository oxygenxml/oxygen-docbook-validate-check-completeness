package com.oxygenxml.ldocbookChecker.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
/**
 * JUnit for test internal links  
 * @author intern4
 *
 */
public class InternalLinksCheckerTest {
	
	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		java.net.URL urlDb4 = new File("D:/docbook-validate-check-completeness/test-samples/broken-internal-links/testdb4.xml").toURI().toURL();
		java.net.URL urlDb5 = new File("D:/docbook-validate-check-completeness/test-samples/broken-internal-links/testdb5.xml").toURI().toURL();
	
	  LinksChecker linkChecker = new LinksCheckerImp();
	
	  //Sets with broken links
	  List<Link> brokenLinkDb4 = linkChecker.check(urlDb4).getInternalLinks();
	  List<Link> brokenLinkDb5 = linkChecker.check(urlDb5).getInternalLinks();
	  
	  
	  assertEquals("Should be a broken link." ,2 , brokenLinkDb4.size());
	  assertEquals("Should be 3 broken link." ,3 , brokenLinkDb5.size());
	  
	  Iterator<Link> iterDb4 = brokenLinkDb4.iterator();
	  Link foundLinkDb4 = iterDb4.next();
	  
	  Iterator<Link> iterDb5 = brokenLinkDb5.iterator();
	  Link foundLinkDb5 = iterDb5.next();
	
	  assertEquals("myPara2", foundLinkDb4.getRef());
	  assertEquals("myPara2Db5", foundLinkDb5.getRef());
	  
	  assertEquals(12, foundLinkDb4.getLine());
	  assertEquals(23, foundLinkDb5.getLine());
	}

}
