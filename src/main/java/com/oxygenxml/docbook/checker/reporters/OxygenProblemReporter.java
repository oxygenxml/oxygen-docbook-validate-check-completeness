package com.oxygenxml.docbook.checker.reporters;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.SwingUtilities;

import com.oxygenxml.docbook.checker.parser.Link;

import ro.sync.document.DocumentPositionedInfo;
import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.results.ResultsManager;
import ro.sync.exml.workspace.api.results.ResultsManager.ResultType;

/**
 * Report broken link and exception in oxygen using Result Manager
 * 
 * @author intern4
 *
 */
public class OxygenProblemReporter implements ProblemReporter {

	private ResultsManager resultManager = PluginWorkspaceProvider.getPluginWorkspace().getResultsManager();

	@Override
	public void reportBrokenLinks(final Link brokenLink, final String tabKey) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					// informations that will be added
					DocumentPositionedInfo result = new DocumentPositionedInfo(DocumentPositionedInfo.SEVERITY_WARN,
							brokenLink.getException().getMessage(), brokenLink.getDocumentURL(), brokenLink.getLine(),
							brokenLink.getColumn());

					// add broken links in given tabKey
					resultManager.addResult(tabKey, result, ResultType.PROBLEM, true, true);

				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void reportException(final Exception ex, final String tabKey, final String document) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					DocumentPositionedInfo result = new DocumentPositionedInfo(DocumentPositionedInfo.SEVERITY_ERROR,
							ex.getMessage(), document);
					resultManager.addResult(tabKey, result, ResultType.PROBLEM, true, true);

				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
		}
	}

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
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reportUndefinedConditions(String attribute, String value, final String tabKey) {
		final String message = "Profile condition: \"" + attribute + " : " + value + "\" isn't defined in preferences .";
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					DocumentPositionedInfo result = new DocumentPositionedInfo(DocumentPositionedInfo.SEVERITY_WARN, message);
					resultManager.addResult(tabKey, result, ResultType.PROBLEM, true, true);
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
		}
	}
}
