package com.oxygenxml.ldocbookChecker.parser;

import java.net.URL;
import java.util.List;

import com.oxygenxml.docbookChecker.Settings;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;

public interface LinksChecker {

	/**
	 * Checks for broken links.
	 * @param parserCreator parser creator for parse the given url.
	 * @param url	the url 
	 * @param settings settings for GUI
	 * @param problemReporter	problem reporter
	 */
	public void check(ParserCreator parserCreator, URL url, Settings settings, ProblemReporter problemReporter);
}
