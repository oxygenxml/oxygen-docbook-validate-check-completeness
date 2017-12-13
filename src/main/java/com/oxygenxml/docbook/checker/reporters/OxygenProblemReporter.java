package com.oxygenxml.docbook.checker.reporters;

import java.awt.GridBagConstraints;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.text.html.HTML.Tag;

import org.apache.log4j.Logger;

import com.oxygenxml.docbook.checker.parser.AssemblyTopicId;
import com.oxygenxml.docbook.checker.parser.ConditionDetails;
import com.oxygenxml.docbook.checker.parser.Id;
import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

import ro.sync.document.DocumentPositionedInfo;
import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.results.ResultsManager;
import ro.sync.exml.workspace.api.results.ResultsManager.ResultType;

/**
 * Report broken link and exception in oxygen using Result Manager
 * 
 * @author Cosmin Duna
 */
public class OxygenProblemReporter implements ProblemReporter {

  /**
   * Logger for logging.
   */
  private static final Logger logger = Logger.getLogger(OxygenProblemReporter.class.getName());
	 
	/**
	 * Result manager.
	 */
	private ResultsManager resultManager = PluginWorkspaceProvider.getPluginWorkspace().getResultsManager();

	/**
	 * Translator, used for internationalization.
	 */
  private Translator translator;

  /**
   * Constructor.
   * @param translator Translator, used for internationalization.
   */
	public OxygenProblemReporter(Translator translator) {
    this.translator = translator;
  }
	
	/**
	 * Report the brokenLink using resultManager.
	 * 
	 */
	@Override
	public void reportBrokenLinks(final Link brokenLink , final Exception ex, final String tabKey) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					// informations that will be added
					DocumentPositionedInfo result = new DocumentPositionedInfo(
					    DocumentPositionedInfo.SEVERITY_WARN,
							ex.getMessage(), 
							brokenLink.getDocumentURL().toString(), 
							brokenLink.getLine(),
							brokenLink.getColumn());

					// add broken links in given tabKey
					resultManager.addResult(tabKey, result, ResultType.PROBLEM, false, false);

				}
			});
		} catch (InvocationTargetException e) {
			logger.debug(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.debug(e.getMessage(), e);
		// Restore interrupted state...
	    Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * Report the assembledFile(topic) using resultManager.
	 * 
	 */
	@Override
	public void reportAssemblyTopic(final AssemblyTopicId assemblyTopic , final Exception ex, final String tabKey) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					// informations that will be added
					DocumentPositionedInfo result = new DocumentPositionedInfo(
					    DocumentPositionedInfo.SEVERITY_WARN,
							ex.getMessage(), 
							assemblyTopic.getLinkFoundDocumentUrl() , 
							assemblyTopic.getLine(),
							assemblyTopic.getColumn());

					// add broken links in given tabKey
					resultManager.addResult(tabKey, result, ResultType.PROBLEM, false, false);

				}
			});
		} catch (InvocationTargetException e) {
			logger.debug(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.debug(e.getMessage(), e);
			// Restore interrupted state...
	    Thread.currentThread().interrupt();
		}
	}


	/**
	 * Report the exception using Results Manager.
	 */
	@Override
	public void reportException(final Exception ex, final String tabKey, final URL document) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					DocumentPositionedInfo result = new DocumentPositionedInfo(
					    DocumentPositionedInfo.SEVERITY_ERROR,
							ex.getMessage(), 
							document.toString());
					
					resultManager.addResult(tabKey, result, ResultType.PROBLEM, false, false);

				}
			});
		} catch (InvocationTargetException e) {
				logger.debug(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.debug(e.getMessage(), e);
			// Restore interrupted state...
	    Thread.currentThread().interrupt();
		}
	}

	/**
	 * Clear the problems reported in given tabKey.
	 * @param tabKey The tabKey.
	 */
	@Override
	public void clearReportedProblems(final String tabKey) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					List<DocumentPositionedInfo> resultsList = resultManager.getAllResults(tabKey);
					int size = resultsList.size();
					for (int i = size - 1; i >= 0; i--) {
						resultManager.removeResult(tabKey, resultsList.get(i));
					}
				}
			});
		} catch (InvocationTargetException e) {
			logger.debug(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.debug(e.getMessage(), e);
			// Restore interrupted state...
	    Thread.currentThread().interrupt();
		}
	}

	/**
	 * Report the undefined conditions using resultManager.
	 */
	@Override
	public void reportUndefinedConditions(final ConditionDetails conditionDetails, final String tabKey) {
	  final String message = MessageFormat.format(translator.getTranslation(Tags.UNDEFINED_CONDITION_MESSAGE),
	      conditionDetails.getAttribute(), conditionDetails.getValue());
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					DocumentPositionedInfo result = new DocumentPositionedInfo(
					    DocumentPositionedInfo.SEVERITY_WARN, 
					    message, 
							conditionDetails.getDocumentUrl(), 
							conditionDetails.getLine(), 
							conditionDetails.getColumn());
					
					resultManager.addResult(tabKey, result, ResultType.PROBLEM, false, false);
				}
			});
		} catch (InvocationTargetException e) {
				logger.debug(e.getMessage(), e);
		} catch (InterruptedException e) {
				logger.debug(e.getMessage(), e);
				// Restore interrupted state...
		    Thread.currentThread().interrupt();
		}
	}

	
	/**
	 * Report the duplicate Id using resultManager.
	 * 
	 */
	@Override
	public void reportDupicateId(final Id duplicateId, final String message, final String tabKey) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					// informations that will be added
					DocumentPositionedInfo result = new DocumentPositionedInfo(
					    DocumentPositionedInfo.SEVERITY_WARN,
							message, 
							duplicateId.getLinkFoundDocumentUrl(), 
							duplicateId.getLine(),
							duplicateId.getColumn());

					// add broken links in given tabKey
					resultManager.addResult(tabKey, result, ResultType.PROBLEM, false, false);

				}
			});
		} catch (InvocationTargetException e) {
			logger.debug(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.debug(e.getMessage(), e);
			// Restore interrupted state...
	    Thread.currentThread().interrupt();
		}
	}

	/**
	 * Report the exception using resultManager.
	 */
	@Override
	public void reportException(final Exception ex, final String tabKey) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				DocumentPositionedInfo result = new DocumentPositionedInfo(
				    DocumentPositionedInfo.SEVERITY_ERROR,
						ex.getMessage());
				resultManager.addResult(tabKey, result, ResultType.PROBLEM, false, false);

			}
		});

	}
	
}
