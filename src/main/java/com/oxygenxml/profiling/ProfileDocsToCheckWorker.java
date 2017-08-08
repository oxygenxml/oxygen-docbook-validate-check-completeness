package com.oxygenxml.profiling;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.ldocbookChecker.parser.OxygenParserCreator;

public class ProfileDocsToCheckWorker  extends SwingWorker<Map<String, Set<String>>, Void>{

	private List<String> urls;

	private ProfilingInformationWorkerReporter reporter;

	private ProblemReporter problemReporter;
	
	
	public ProfileDocsToCheckWorker(List<String> urls, ProfilingInformationWorkerReporter reporter, ProblemReporter problemReporter) {
		this.urls = urls;
		this.reporter = reporter;
		this.problemReporter = problemReporter;
	}

	@Override
	protected Map<String,Set<String>> doInBackground() throws Exception {
		ProfilingInformation finder = new ProfileConditionsFinder(new OxygenParserCreator());
		return finder.getConditionsSet(urls);
	}

	
	@Override
	protected void done() {
		super.done();
		try {
			reporter.reportConditionsProfileDocsToCheckWorkerFinish(get());
		} catch (InterruptedException e) {
			problemReporter.reportException(e);
		} catch (ExecutionException e) {
			problemReporter.reportException(e);
		}
	}
}
