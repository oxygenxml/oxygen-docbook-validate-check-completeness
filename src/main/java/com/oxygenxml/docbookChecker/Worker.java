package com.oxygenxml.docbookChecker;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.oxygenxml.docbookChecker.reporters.LinksReporter;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.ldocbookChecker.parser.Id;
import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.ParserCreator;
import com.oxygenxml.ldocbookChecker.parser.LinkDetails;
/**
 * 
 *Worker responsible with background process.
 *
 */
public class Worker  extends SwingWorker<Void , ProblemReporter> {
	
	private List<URL> urls;

	private LinksChecker linkChecker;
	
	private LinksReporter linkReporter;
	
	private ParserCreator parserCreator;
	
	private Settings settings;
	
	private ProblemReporter problemReporter;
	
	/**
	 * Constructor
	 * @param url
	 * @param brokenLinkReporter
	 * @param allowedHostNames
	
	 */
	public Worker(List<URL> urls, Settings settings, LinksReporter linkReporter, ParserCreator parserCreator, ProblemReporter problemReporter) {
		this.urls = urls;
		this.settings = settings;
		this.linkReporter = linkReporter;
		linkChecker = new LinksCheckerImp();
		this.parserCreator = parserCreator;
		this.problemReporter = problemReporter;
	}
	
	
	@Override
	public Void doInBackground() throws Exception {
		for(int i = 0; i < urls.size(); i++ ){
			//start check 
			linkChecker.check(parserCreator, urls.get(i), settings, problemReporter); 
		}
		return null;
	}
	
 


	@Override
	protected void done() {
		
		
	}



	
	
}

