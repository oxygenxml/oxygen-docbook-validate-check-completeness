package com.oxygenxml.profiling;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.TabKeyGenerator;
/**
 * Worker for get profile conditions from the documents at the given. 
 * @author intern4
 *
 */
public class ProfileConditionsFromDocsWorker  extends SwingWorker<LinkedHashMap<String, LinkedHashSet<String>>, Void>{

	/**
	 * The documents URLs
	 */
	private List<String> urls;

	/**
	 * Worker reporter
	 */
	private ProfileConditionsFromDocsWorkerReporter workerReporter;

	/**
	 * Problem reporter
	 */
	private ProblemReporter problemReporter;

	/**
	 * The documents type
	 */
	private String docType;
	
	public ProfileConditionsFromDocsWorker(List<String> urls, ProfileConditionsFromDocsWorkerReporter reporter, ProblemReporter problemReporter, String docType) {
		this.urls = urls;
		this.workerReporter = reporter;
		this.problemReporter = problemReporter;
		this.docType = docType;
	}

	@Override
	protected LinkedHashMap<String, LinkedHashSet<String>> doInBackground(){
		ProfilingConditionsInformations finder = new ProfilingConditionsInformationsImpl();
		LinkedHashMap<String, LinkedHashSet<String>> toReturn = new LinkedHashMap<String, LinkedHashSet<String>>();

		int size = urls.size();
		for(int i = 0; i < size; i++){
			try {
				toReturn.putAll( finder.getConditionsFromDocs(urls.get(i), docType));
			} catch (ParserConfigurationException e) {
				problemReporter.reportException(e, TabKeyGenerator.generate(urls.get(i), ""), urls.get(i));
				return null;
			} catch (SAXException e) {
				problemReporter.reportException(e, TabKeyGenerator.generate(urls.get(i), ""), urls.get(i));
				return null;
			} catch (IOException e) {
				problemReporter.reportException(e, TabKeyGenerator.generate(urls.get(i), ""), urls.get(i));
				return null;
			}
		}
		return toReturn;
	}

	
	@Override
	protected void done() {
		super.done();
				try {
					workerReporter.reportProfileConditionsFromDocsWorkerFinish(get());
				} catch (InterruptedException e) {
				} catch (ExecutionException e) {
				}
	}
}
