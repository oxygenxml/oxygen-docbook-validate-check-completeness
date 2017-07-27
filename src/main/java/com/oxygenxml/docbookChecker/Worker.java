package com.oxygenxml.docbookChecker;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;
import com.oxygenxml.ldocbookChecker.parser.Id;
import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.ParserCreator;
import com.oxygenxml.ldocbookChecker.parser.LinkDetails;

/**
 * 
 * Worker responsible with background process.
 *
 */
public class Worker extends SwingWorker<Void, ProblemReporter> {

	private List<String> urls;

	private LinksChecker linkChecker;

	private ParserCreator parserCreator;

	private Settings settings;

	private ProblemReporter problemReporter;

	private StatusReporter statusReporter;

	/**
	 * Constructor
	 * 
	 * @param url
	 * @param brokenLinkReporter
	 * @param allowedHostNames
	 * 
	 */
	public Worker(List<String> urls, Settings settings, ParserCreator parserCreator, ProblemReporter problemReporter,
			StatusReporter statusReporter) {
		this.urls = urls;
		this.settings = settings;
		linkChecker = new LinksCheckerImp();
		this.parserCreator = parserCreator;
		this.problemReporter = problemReporter;
		this.statusReporter = statusReporter;
	}

	@Override
	public Void doInBackground() {
		if (!urls.isEmpty()) {
			for (int i = 0; i < urls.size(); i++) {
				// start check
				linkChecker.check(parserCreator, urls.get(i), settings, problemReporter, statusReporter);
			}
		}
		return null;
	}

	@Override
	protected void done() {
	}

}
