package com.oxygenxml.docbookChecker.reporters;

import java.util.List;

import com.oxygenxml.ldocbookChecker.parser.Link;

import ro.sync.document.DocumentPositionedInfo;
import ro.sync.exml.workspace.api.PluginWorkspace;
import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.results.ResultsManager;
import ro.sync.exml.workspace.api.results.ResultsManager.ResultType;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
/**
 * Report broken link and exception in oxygen using Result Manager
 * @author intern4
 *
 */
public class OxygenProblemReporter implements ProblemReporter {
 
	private ResultsManager resultManager = PluginWorkspaceProvider.getPluginWorkspace().getResultsManager();
	private final String tabKey = "DocBook links check";

	@Override
	public void reportBrokenLinks(Link brokenLink) {
		DocumentPositionedInfo result = new DocumentPositionedInfo(DocumentPositionedInfo.SEVERITY_WARN,
				brokenLink.getException().getMessage(), brokenLink.getDocumentURL(), brokenLink.getLine(),
				brokenLink.getColumn());
		resultManager.addResult(tabKey, result, ResultType.PROBLEM, true, true);
	}

	@Override
	public void reportException(Exception ex) {
		DocumentPositionedInfo result = new DocumentPositionedInfo(DocumentPositionedInfo.SEVERITY_ERROR, ex.getMessage());
		resultManager.addResult(tabKey, result, ResultType.PROBLEM, true, true);
	}

	@Override
	public void clearReportedProblems() {
		List<DocumentPositionedInfo> resultsList = resultManager.getAllResults(tabKey);
		for (int i = 0; i < resultsList.size(); i++) {
			resultManager.removeResult(tabKey, resultsList.get(i));
		}

	}
}
