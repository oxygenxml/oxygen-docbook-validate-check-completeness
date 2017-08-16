package com.oxygenxml.docbookChecker;

import java.util.List;

import javax.swing.SwingWorker;

import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;
import com.oxygenxml.docbookChecker.translator.OxygenTranslator;
import com.oxygenxml.docbookChecker.view.ProgressMonitorReporter;
import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.ParserCreator;

/**
 * 
 * Worker responsible with process of validation.
 *
 */
public class ValidationWorker extends SwingWorker<Void, String> implements WorkerReporter{

	/**
	 * List with URLs that should be validated
	 */
	private List<String> urls;

	/**
	 * Used for validate links.
	 */
	private LinksChecker linkChecker;

	/**
	 * Used for parse the content at the given URLs.
	 */
	private ParserCreator parserCreator;

	/**
	 * Used for report problem(broken links and exception).
	 */
	private ProblemReporter problemReporter;

	/**
	 * Used for report the status of process.
	 */
	private StatusReporter statusReporter;

	/**
	 * Used for do the interaction with user.
	 */
	private CheckerInteractor interactor;
	
	/**
	 * Used for report progress and notes at progress monitor.
	 */
	private ProgressMonitorReporter progressMonitorReporter;
	

	public ValidationWorker(List<String> urls, CheckerInteractor interactor, ParserCreator parserCreator, ProblemReporter problemReporter,
			StatusReporter statusReporter, ProgressMonitorReporter progressMonitorReporter)  {
		this.urls = urls;
		this.interactor = interactor;
		this.linkChecker = new LinksCheckerImp();
		this.parserCreator = parserCreator;
		this.problemReporter = problemReporter;
		this.statusReporter = statusReporter;
		this.progressMonitorReporter = progressMonitorReporter;
		
	}

	@Override
	public Void doInBackground() {
		if (!urls.isEmpty()) {
				// start the validation
				linkChecker.check(parserCreator, urls, interactor, problemReporter, statusReporter, this, new OxygenTranslator());
		}
		return null;
	}

	@Override
	protected void done() {
		//close the monitor 
		progressMonitorReporter.closeMonitor();
	}

	@Override
	public void reportProgress(int progress, boolean isFinalCicle) {
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
