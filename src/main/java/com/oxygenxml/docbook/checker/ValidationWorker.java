package com.oxygenxml.docbook.checker;

import java.util.List;

import javax.swing.SwingWorker;

import com.oxygenxml.docbook.checker.gui.ProgressMonitorReporter;
import com.oxygenxml.docbook.checker.parser.OxygenParserCreator;
import com.oxygenxml.docbook.checker.reporters.OxygenStatusReporter;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.translator.OxygenTranslator;
import com.oxygenxml.docbook.checker.validator.DocumentChecker;
import com.oxygenxml.docbook.checker.validator.DocumentCheckerImp;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

/**
 * 
 * Worker responsible with validation.
 *
 */
public class ValidationWorker extends SwingWorker<Void, String> implements ValidationWorkerInteractor{

	/**
	 * List with URLs that should be validated
	 */
	private List<String> urls;

	/**
	 * Link checker, used for validate links.
	 */
	private DocumentChecker linkChecker;

	/**
	 * Used for report problem(broken links and exception).
	 */
	private ProblemReporter problemReporter;

	/**
	 * Used to do the interaction with user.
	 */
	private CheckerInteractor checkerInteractor;
	
	/**
	 * Used for report progress and notes at progress monitor.
	 */
	private ProgressMonitorReporter progressMonitorReporter;

	/**
	 * Application interactor
	 */
	private ApplicationInteractor applicationInteractor;
	
	/**
	 * Constructor. 
	 * 
	 * @param urls List with URLs(String format) of file that should be validate.
	 * @param applicationInteractor Application interactor.
	 * @param checkerInteractor	Checker interactor.
	 * @param problemReporter Problem reporter.
	 * @param progressMonitorReporter Progress monitor reporter.
	 */
	public ValidationWorker(List<String> urls, ApplicationInteractor applicationInteractor, CheckerInteractor checkerInteractor, 
			ProblemReporter problemReporter, ProgressMonitorReporter progressMonitorReporter)  {
		this.urls = urls;
		this.applicationInteractor = applicationInteractor;
		this.checkerInteractor = checkerInteractor;
		this.linkChecker = new DocumentCheckerImp();
		this.problemReporter = problemReporter;
		this.progressMonitorReporter = progressMonitorReporter;
		
	}

	
	/**
	 * Validate the URLs.
	 * Note: this method is executed in a background thread.
	 */
	@Override
	public Void doInBackground() {
		if (!urls.isEmpty()) {
				// start the validation
				linkChecker.check(new OxygenParserCreator(), new ProfilingConditionsInformationsImpl(), urls, checkerInteractor, problemReporter,
						new OxygenStatusReporter(), this, new OxygenTranslator());
		}
		return null;
	}

	
	/**
	 * Executed on the Event Dispatch Thread after the doInBackground method is finished.
	 * 
	 */
	@Override
	protected void done() {
		//close the monitor 
		progressMonitorReporter.closeMonitor();
		
		//Inform application that operation isn't being in progress. 
		applicationInteractor.setOperationInProgress(false);
	}

	
	/**
	 * Report the progress at progress monitor.
	 * @param The progress.
	 * @param isFinalCycle <true> if it's the final cycle of progress monitor, <code>false</code>otherwise.
	 * 
	 */
	@Override
	public void reportProgress(int progress, boolean isFinalCycle) {
		//if is final cycle and progress in 100
		if(progress == 100 && !isFinalCycle){
			//reset the progress
			setProgress(0);	
		}
		else{
			//set progress.
			setProgress(progress);
		}
	}

	/**
	 * Report in the given note at ProgressMonitor using publish method.
	 */
	@Override
	public void reportNote(String note) {
		//Sends data chunks(note) to the process method
		publish(note);
	}
	
	/**
	 * Receives data chunks(notes) from the publish method asynchronously on the Event Dispatch Thread.
	 * @param notes to be process
	 */
	@Override
	protected void process(List<String> notes) {
		if(isCancelled()) { return; }
		//report the last note.
		progressMonitorReporter.reportNote(notes.get(notes.size()-1));
	}
	
	
}
