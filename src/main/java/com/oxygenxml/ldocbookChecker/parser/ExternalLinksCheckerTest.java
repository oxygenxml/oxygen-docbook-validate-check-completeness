package com.oxygenxml.ldocbookChecker.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * Junit for test external links
 * @author intern4
 *
 */
public class ExternalLinksCheckerTest {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		java.net.URL urlDb4 = new File("D:/docbook-validate-check-completeness/test-samples/broken-external-link/testdb4.xml").toURI().toURL();
		java.net.URL urlDb5 = new File("D:/docbook-validate-check-completeness/test-samples/broken-external-link/testdb5.xml").toURI().toURL();
	
	  LinksChecker externalLinkChecker = new LinksCheckerImp();
	
	  //Sets with broken links
	  List<Link> brokenLinkDb4 = externalLinkChecker.check(urlDb4).getExternalLinks();
	  List<Link> brokenLinkDb5 = externalLinkChecker.check(urlDb5).getExternalLinks();
	  
	  assertEquals("Should be a broken link." ,1 , brokenLinkDb4.size());
	  assertEquals("Should be a broken link." ,1 , brokenLinkDb5.size());
	  
	  Iterator<Link> iterDb4 = brokenLinkDb4.iterator();
	  Link foundLinkDb4 = iterDb4.next();
	  
	  Iterator<Link> iterDb5 = brokenLinkDb5.iterator();
	  Link foundLinkDb5 = iterDb5.next();
	
	  
	  assertEquals("http://www.google2.com", foundLinkDb4.getRef());
	  assertEquals("http://www.google2.com", foundLinkDb5.getRef());
	  
	  assertEquals(9, foundLinkDb4.getLine());
	  assertEquals(22, foundLinkDb5.getLine());
	}

}
