package com.oxygenxml.docbook.checker;

import java.net.URL;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;

import com.oxygenxml.docbook.checker.gui.HierarchyReportDialog;
import com.oxygenxml.docbook.checker.gui.ProgressDialogInteractor;
import com.oxygenxml.docbook.checker.parser.OxygenParserCreator;
import com.oxygenxml.docbook.checker.reporters.OxygenStatusReporter;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.translator.OxygenTranslator;
import com.oxygenxml.docbook.checker.translator.Translator;
import com.oxygenxml.docbook.checker.validator.DocumentChecker;
import com.oxygenxml.docbook.checker.validator.DocumentCheckerImp;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

/**
 * 
 * Worker responsible with validation.
 *
 */
public class ValidationWorker extends SwingWorker<Void, String> implements ValidationWorkerInteractor {

	/**
	 * List with URLs that should be validated
	 */
	private List<URL> urls;

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
	 * Used for report progress and notes at progress dialog.
	 */
	private ProgressDialogInteractor progressDialogInteractor;

	/**
	 * Application interactor
	 */
	private ApplicationInteractor applicationInteractor;

	/**
	 * Constructor.
	 * 
	 * @param urls
	 *          List with URLs(String format) of file that should be validate.
	 * @param applicationInteractor
	 *          Application interactor.
	 * @param checkerInteractor
	 *          Checker interactor.
	 * @param problemReporter
	 *          Problem reporter.
	 * @param progressDialogInteractor
	 *          Progress dialog interactor.
	 */
	public ValidationWorker(List<URL> urls, ApplicationInteractor applicationInteractor,
			CheckerInteractor checkerInteractor, ProblemReporter problemReporter,
			ProgressDialogInteractor progressDialogInteractor) {
		this.urls = urls;
		this.applicationInteractor = applicationInteractor;
		this.checkerInteractor = checkerInteractor;
		this.linkChecker = new DocumentCheckerImp();
		this.problemReporter = problemReporter;
		this.progressDialogInteractor = progressDialogInteractor;

	}

	/**
	 * Validate the URLs. Note: this method is executed in a background thread.
	 */
	@Override
	public Void doInBackground() {
		if (!urls.isEmpty()) {
			final Translator translator = new OxygenTranslator();

			// start the validation
			final DefaultMutableTreeNode node = linkChecker.check(new OxygenParserCreator(),
					new ProfilingConditionsInformationsImpl(), urls, checkerInteractor, problemReporter,
					new OxygenStatusReporter(), this, translator);

			//open the hierarchy report dialog
			if (node != null) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						new HierarchyReportDialog(translator, applicationInteractor.getApplicationFrame(), node);
					}
				});
			}
		}
		return null;
	}

	/**
	 * Executed on the Event Dispatch Thread after the doInBackground method is
	 * finished.
	 * 
	 */
	@Override
	protected void done() {
		// close the monitor
		progressDialogInteractor.close();

		// Inform application that operation isn't being in progress.
		applicationInteractor.setOperationInProgress(false);
	}

	/**
	 * Report in the given note at ProgressMonitor using publish method.
	 */
	@Override
	public void reportNote(String note) {
		// Sends data chunks(note) to the process method
		publish(note);
	}

	/**
	 * Receives data chunks(notes) from the publish method asynchronously on the
	 * Event Dispatch Thread.
	 * 
	 * @param notes
	 *          to be process
	 */
	@Override
	protected void process(List<String> notes) {
		if (isCancelled()) {
			return;
		}
		// report the last note.
		progressDialogInteractor.setNote(notes.get(notes.size() - 1));
	}

}
