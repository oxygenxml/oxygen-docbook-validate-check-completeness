package com.oxygenxml.profiling;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.oxygenxml.ldocbookChecker.parser.OxygenParserCreator;

public class ProfileDocsToCheckWorker  extends SwingWorker<Map<String, Set<String>>, Void>{

	private List<String> urls;

	private ProfilingInformationWorkerReporter reporter;

	
	public ProfileDocsToCheckWorker(List<String> urls, ProfilingInformationWorkerReporter reporter) {
		this.urls = urls;
		this.reporter = reporter;
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
			// TODO raporteaza la interfata
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
