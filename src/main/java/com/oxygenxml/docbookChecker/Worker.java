package com.oxygenxml.docbookChecker;

import java.util.List;

import javax.swing.SwingWorker;

import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;
import com.oxygenxml.docbookChecker.view.ProgressMonitorReporter;
import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.ParserCreator;

/**
 * 
 * Worker responsible with background process.
 *
 */
public class Worker extends SwingWorker<Void, String> implements WorkerReporter{

	private List<String> urls;

	private LinksChecker linkChecker;

	private ParserCreator parserCreator;


	private ProblemReporter problemReporter;

	private StatusReporter statusReporter;

	private CheckerInteractor interactor;
	
	private ProgressMonitorReporter progressMonitorReporter;
	

	/**
	 * Constructor
	 * 
	 * @param url
	 * @param brokenLinkReporter
	 * @param allowedHostNames
	 * 
	 */
	public Worker(List<String> urls, CheckerInteractor interactor, ParserCreator parserCreator, ProblemReporter problemReporter,
			StatusReporter statusReporter, ProgressMonitorReporter progressMonitorReporter)  {
		this.urls = urls;
		this.interactor = interactor;
		linkChecker = new LinksCheckerImp();
		this.parserCreator = parserCreator;
		this.problemReporter = problemReporter;
		this.statusReporter = statusReporter;
		this.progressMonitorReporter = progressMonitorReporter;
		
	}

	@Override
	public Void doInBackground() {
		if (!urls.isEmpty()) {
				// start check
				linkChecker.check(parserCreator, urls, interactor, problemReporter, statusReporter, this);
		}
		return null;
		
	}

	@Override
	protected void done() {
		progressMonitorReporter.closeMonitor();
	}

	@Override
	public void reporteProgress(int progress, boolean isFinalCicle) {
		if(progress == 100 && !isFinalCicle){
			setProgress(0);	
		}else{
			setProgress(progress);
		}
	}

	@Override
	public void reportInProcessElement(String element) {
		publish(element);
	}
	
	@Override
	protected void process(List<String> elements) {
		if(isCancelled()) { return; }
		//report the last element in process
		progressMonitorReporter.reportNote(elements.get(elements.size()-1));
	}

}
