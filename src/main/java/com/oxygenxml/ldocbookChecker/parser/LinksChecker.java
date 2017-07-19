package com.oxygenxml.ldocbookChecker.parser;

import java.net.URL;

public interface LinksChecker {

	/**
	 * Check the links from the content of a given url.
	 * @param url the url
	 * @return set with invalid links.
	 */
	public Links check(URL url);
}
