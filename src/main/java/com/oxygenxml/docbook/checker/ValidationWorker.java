package com.oxygenxml.docbook.checker;

import java.util.List;

import javax.swing.SwingWorker;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.docbook.checker.gui.ProgressMonitorReporter;
import com.oxygenxml.docbook.checker.parser.ParserCreator;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.StatusReporter;
import com.oxygenxml.docbook.checker.translator.OxygenTranslator;
import com.oxygenxml.docbook.checker.validator.LinksChecker;
import com.oxygenxml.docbook.checker.validator.LinksCheckerImp;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

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
				linkChecker.check(parserCreator, new ProfilingConditionsInformationsImpl(), urls, interactor, problemReporter, statusReporter, this, new OxygenTranslator());
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