package com.oxygenxml.docbookChecker.reporters;

import java.util.List;

import com.oxygenxml.ldocbookChecker.parser.Link;

import ro.sync.document.DocumentPositionedInfo;
import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.results.ResultsManager;
import ro.sync.exml.workspace.api.results.ResultsManager.ResultType;
/**
 * Report broken link and exception in oxygen using Result Manager
 * @author intern4
 *
 */
public class OxygenProblemReporter implements ProblemReporter {
 
	private ResultsManager resultManager = PluginWorkspaceProvider.getPluginWorkspace().getResultsManager();
	
	@Override
	public void reportBrokenLinks(Link brokenLink, String tabKey) {
		//informations that will be added
		DocumentPositionedInfo result = new DocumentPositionedInfo(DocumentPositionedInfo.SEVERITY_WARN,
				brokenLink.getException().getMessage(), brokenLink.getDocumentURL(), brokenLink.getLine(),
				brokenLink.getColumn());
		
			//add broken links in given tabKey
			resultManager.addResult(tabKey, result, ResultType.PROBLEM, true, true);
	}

	@Override
	public void reportException(Exception ex, String tabKey) {
		//informations that will be added
		DocumentPositionedInfo result = new DocumentPositionedInfo(DocumentPositionedInfo.SEVERITY_ERROR, ex.getMessage());

		resultManager.addResult(tabKey + tabKey, result, ResultType.PROBLEM, true, true);
		
	}

	@Override
	public void clearReportedProblems(String tabKey) {
			List<DocumentPositionedInfo> resultsList = resultManager.getAllResults(tabKey);
			for (int i = 0; i < resultsList.size(); i++) {
				resultManager.removeResult(tabKey, resultsList.get(i));
			}
	}
}
