package com.oxygenxml.docbook.checker;

import java.util.List;

import javax.swing.SwingWorker;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.docbook.checker.gui.ProgressMonitorReporter;
import com.oxygenxml.docbook.checker.parser.OxygenParserCreator;
import com.oxygenxml.docbook.checker.parser.ParserCreator;
import com.oxygenxml.docbook.checker.reporters.OxygenStatusReporter;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.StatusReporter;
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
	 * Used for validate links.
	 */
	private DocumentChecker linkChecker;

	/**
	 * Used for report problem(broken links and exception).
	 */
	private ProblemReporter problemReporter;


	/**
	 * Used for do the interaction with user.
	 */
	private CheckerInteractor interactor;
	
	/**
	 * Used for report progress and notes at progress monitor.
	 */
	private ProgressMonitorReporter progressMonitorReporter;

	/**
	 * Oxygen interactor
	 */
	private OxygenInteractor oxygenInteractor;
	

	public ValidationWorker(List<String> urls, OxygenInteractor oxygenInteractor, CheckerInteractor interactor, ProblemReporter problemReporter,
			 ProgressMonitorReporter progressMonitorReporter)  {
		this.urls = urls;
		this.oxygenInteractor = oxygenInteractor;
		this.interactor = interactor;
		this.linkChecker = new DocumentCheckerImp();
		this.problemReporter = problemReporter;
		this.progressMonitorReporter = progressMonitorReporter;
		
	}

	@Override
	public Void doInBackground() {
		if (!urls.isEmpty()) {
				// start the validation
				linkChecker.check(new OxygenParserCreator(), new ProfilingConditionsInformationsImpl(), urls, interactor, problemReporter,
						new OxygenStatusReporter(), this, new OxygenTranslator());
		}
		return null;
	}

	@Override
	protected void done() {
		//close the monitor 
		progressMonitorReporter.closeMonitor();
		oxygenInteractor.setButtonsEnable(true);
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

	@Override
	public boolean isSetIsCancelled() {
		return isCancelled();
	}

}
