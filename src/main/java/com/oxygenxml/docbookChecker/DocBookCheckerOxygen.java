package com.oxygenxml.docbookChecker;

import java.awt.Component;

import com.oxygenxml.docbookChecker.persister.ContentPersisterImpl;
import com.oxygenxml.docbookChecker.reporters.OxygenProblemReporter;
import com.oxygenxml.docbookChecker.reporters.OxygenStatusReporter;
import com.oxygenxml.docbookChecker.translator.OxygenTranslator;
import com.oxygenxml.docbookChecker.view.CheckerDialog;
import com.oxygenxml.docbookChecker.view.OxygenFileChooserCreator;
import com.oxygenxml.ldocbookChecker.parser.OxygenParserCreator;
/**
 * Checker for Oxygen extension.
 * @author intern4
 *
 */
public class DocBookCheckerOxygen {
		
	public DocBookCheckerOxygen(String url, Component component) {
		CheckerDialog checkerFrame = new CheckerDialog(url, component, new OxygenProblemReporter(),new OxygenStatusReporter(),
				new OxygenFileChooserCreator(), new OxygenParserCreator(), new ContentPersisterImpl(), new OxygenTranslator());
	}
}
