package com.oxygenxml.profiling;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.ldocbookChecker.parser.OxygenParserCreator;
/**
 * Worker for get profile conditions from the documents at the given. 
 * @author intern4
 *
 */
public class ProfileConditionsFromDocsWorker  extends SwingWorker<Map<String, Set<String>>, Void>{

	private List<String> urls;

	private ProfileConditionsFromDocsWorkerReporter workerReporter;

	private ProblemReporter problemReporter;
	
	
	public ProfileConditionsFromDocsWorker(List<String> urls, ProfileConditionsFromDocsWorkerReporter reporter, ProblemReporter problemReporter) {
		this.urls = urls;
		this.workerReporter = reporter;
		this.problemReporter = problemReporter;
	}

	@Override
	protected Map<String,Set<String>> doInBackground() throws Exception {
		ProfilingConditionsInformations finder = new ProfilingConditionsInformationsImpl();
		return finder.getConditionsFromDocs(urls);
	}

	
	@Override
	protected void done() {
		super.done();
		try {
			workerReporter.reportProfileConditionsFromDocsWorkerFinish(get());
		} catch (InterruptedException e) {
			problemReporter.reportException(e);
		} catch (ExecutionException e) {
			problemReporter.reportException(e);
		}
	}
}
