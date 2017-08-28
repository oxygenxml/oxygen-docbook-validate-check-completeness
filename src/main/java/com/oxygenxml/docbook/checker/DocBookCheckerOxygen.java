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
 * DocBook checker used in oxygen.
 * @author intern4
 *
 */
public class DocBookCheckerOxygen {
		
	public DocBookCheckerOxygen(OxygenSourceDescription sourceDescription, OxygenInteractor oxygenInteractor, Component component) {
		DocBookCheckerDialog docBookChecker = new DocBookCheckerDialog(sourceDescription, oxygenInteractor,  component, new OxygenProblemReporter(),new OxygenStatusReporter(),
				new OxygenFileChooserCreator(), new OxygenParserCreator(), new ContentPersisterImpl(), new OxygenTranslator());
	}
}
