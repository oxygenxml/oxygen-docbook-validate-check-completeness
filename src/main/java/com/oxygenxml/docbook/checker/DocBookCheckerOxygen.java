package com.oxygenxml.docbook.checker;

import java.awt.Component;

import com.oxygenxml.docbook.checker.gui.DocBookCheckerDialog;
import com.oxygenxml.docbook.checker.gui.OxygenFileChooserCreator;
import com.oxygenxml.docbook.checker.parser.OxygenParserCreator;
import com.oxygenxml.docbook.checker.persister.ContentPersisterImpl;
import com.oxygenxml.docbook.checker.reporters.OxygenProblemReporter;
import com.oxygenxml.docbook.checker.reporters.OxygenStatusReporter;
import com.oxygenxml.docbook.checker.translator.OxygenTranslator;
/**
 * Checker for Oxygen extension.
 * @author intern4
 *
 */
public class DocBookCheckerOxygen {
		
	public DocBookCheckerOxygen(String url, Component component) {
		 DocBookCheckerDialog checkerFrame = new DocBookCheckerDialog(url, component, new OxygenProblemReporter(),new OxygenStatusReporter(),
				new OxygenFileChooserCreator(), new OxygenParserCreator(), new ContentPersisterImpl(), new OxygenTranslator());
	}
}
