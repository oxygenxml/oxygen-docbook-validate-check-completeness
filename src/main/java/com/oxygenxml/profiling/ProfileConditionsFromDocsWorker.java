package com.oxygenxml.profiling;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.TabKeyGenerator;
/**
 * Worker for get profile conditions from the documents at the given. 
 * @author Cosmin Duna
 *
 */
public class ProfileConditionsFromDocsWorker  extends SwingWorker<LinkedHashMap<String, LinkedHashSet<String>>, Void>{

	/**
	 * The documents URLs
	 */
	private List<URL> urls;

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
	
	/**
	 * Logger
	 */
	 private static final Logger logger = LoggerFactory.getLogger(ProfileConditionsFromDocsWorker.class);
	
	/**
	 * Constructor
	 * @param urls List with URLs to parse.
	 * @param reporter Worker reporter,
	 * @param problemReporter Problem reporter
	 * @param docType The type of document.
	 */
	public ProfileConditionsFromDocsWorker(List<URL> urls, ProfileConditionsFromDocsWorkerReporter reporter, ProblemReporter problemReporter, String docType) {
		this.urls = urls;
		this.workerReporter = reporter;
		this.problemReporter = problemReporter;
		this.docType = docType;
	}

	/**
	 * Gather the conditions.
	 * Note: this method is executed in a background thread.
	 */
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

	
	/**
	 * Executed on the Event Dispatch Thread after the doInBackground method is finished.
	 * 
	 */
	@Override
	protected void done() {
		super.done();
				try {
					workerReporter.reportProfileConditionsFromDocsWorkerFinish(get());
				} catch (InterruptedException e) {
					logger.debug(e.getMessage(), e);
					// Restore interrupted state...
			    Thread.currentThread().interrupt();
				} catch (ExecutionException e) {
					logger.debug(e.getMessage(), e);
				}
	}
}
