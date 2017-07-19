package com.oxygenxml.ldocbookChecker.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

/**
 * Junit for ExternelLinkChecker
 * @author intern4
 *
 */
public class InternalLinksCheckerTest {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		java.net.URL urlDb4 = new File("D:/docbook-validate-check-completeness/test-samples/broken-image/testdb4.xml").toURI().toURL();
		java.net.URL urlDb5 = new File("D:/docbook-validate-check-completeness/test-samples/broken-image/testdb5.xml").toURI().toURL();
	
	  LinksChecker linkChecker = new LinksCheckerImp();
	
	  //Sets with broken links
	  Set<Link> brokenLinkDb4 = linkChecker.check(urlDb4).getImgLinks();
	  System.out.println("");
	  Set<Link> brokenLinkDb5 = linkChecker.check(urlDb5).getImgLinks();
	  
	  System.out.println("lista\n"+brokenLinkDb5+ "\n");
	  
	  assertEquals("Should be a broken link." ,2 , brokenLinkDb4.size());
	  assertEquals("Should be 2 broken link." ,2 , brokenLinkDb5.size());
	  
	  Iterator<Link> iterDb4 = brokenLinkDb4.iterator();
	  Link foundLinkDb4 = iterDb4.next();
	  
	  Iterator<Link> iterDb5 = brokenLinkDb5.iterator();
	  Link foundLinkDb5 = iterDb5.next();
	
	  System.out.println("gb4: "+ foundLinkDb4);
	  System.out.println("dg5: " +foundLinkDb5);
	  assertEquals("primul.png", foundLinkDb4.getRef());
	  assertEquals("primul.png", foundLinkDb5.getRef());
	  
	  assertEquals(10, foundLinkDb4.getLine());
	  assertEquals(28, foundLinkDb5.getLine());
	}

}
