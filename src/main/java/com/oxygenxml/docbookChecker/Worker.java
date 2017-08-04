package com.oxygenxml.docbookChecker;

import java.util.List;

import javax.swing.SwingWorker;

import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;
import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.ParserCreator;

/**
 * 
 * Worker responsible with background process.
 *
 */
public class Worker extends SwingWorker<Void, ProblemReporter> {

	private List<String> urls;

	private LinksChecker linkChecker;

	private ParserCreator parserCreator;


	private ProblemReporter problemReporter;

	private StatusReporter statusReporter;

	private CheckerInteractor interactor;

	/**
	 * Constructor
	 * 
	 * @param url
	 * @param brokenLinkReporter
	 * @param allowedHostNames
	 * 
	 */
	public Worker(List<String> urls, CheckerInteractor interactor, ParserCreator parserCreator, ProblemReporter problemReporter,
			StatusReporter statusReporter) {
		this.urls = urls;
		this.interactor = interactor;
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
				linkChecker.check(parserCreator, urls.get(i), interactor, problemReporter, statusReporter);
			}
		}
		System.out.println("**************finish");
		return null;
	}

	@Override
	protected void done() {
	}

}
