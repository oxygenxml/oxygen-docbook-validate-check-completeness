package com.oxygenxml.profiling;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.oxygenxml.ldocbookChecker.parser.OxygenParserCreator;

public class ProfilingInformationWorker<T> extends SwingWorker<T, Void> {

	private String documentType;

	private String informationType;

	private ProfilingInformationWorkerReporter reporter;

	/**
	 * @param documentType
	 *          the type of xml document: ProfilingInformation.DITA,
	 *          ProfilingInformation.DOCBOOK, ProfilingInformation.DOCBOOK4,
	 *          ProfilingInformation.DOCBOOK5 or ProfilingInformation.ALLTYPES.
	 */

	public ProfilingInformationWorker(String informationType, String documentType,
			ProfilingInformationWorkerReporter reporter) {
		this.documentType = documentType;
		this.informationType = informationType;
		this.reporter = reporter;
	}

	@Override
	protected T doInBackground() throws Exception {
		ProfilingInformation profilingInformation = new ProfileConditionsFinder(new OxygenParserCreator());
		if (ProfilingInformation.CONDITIONS_SETS.equals(informationType)) {
			return (T) profilingInformation.getConditionsSets(documentType);
		} 
		else if (ProfilingInformation.CONDITIONS.equals(informationType)) {
			return (T) profilingInformation.getProfileConditions(documentType);
		} 
		else if (ProfilingInformation.CONDITIONS.equals(informationType)) {
			return (T) profilingInformation.getProfileConditionAttributesNames(documentType);
		}
		
		
		
		return null;
	}

	@Override
	protected void done() {
		super.done();
		try {
			if (ProfilingInformation.CONDITIONS_SETS.equals(informationType)) {
				reporter.reportConditionsSetsWorkerFinish((Map<String, Map<String, Set<String>>>) get());
			} 
			else if (ProfilingInformation.CONDITIONS.equals(informationType)) {
				reporter.reportConditionsWorkerFinish((Map<String, Set<String>>) get());
			}
			else if (ProfilingInformation.ATTRIBUTES.equals(informationType)) {
				reporter.reportConditionsAttributeNamesWorkerFinish((Set<String>)get());
			}
		} catch (InterruptedException e) {
			//TODO TRIMITE-LE LA GUI
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
