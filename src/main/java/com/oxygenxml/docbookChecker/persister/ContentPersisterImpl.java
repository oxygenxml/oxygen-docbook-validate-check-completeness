package com.oxygenxml.docbookChecker.persister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbookChecker.reporters.Tags;
import com.oxygenxml.docbookChecker.view.CheckerFrame;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.options.WSOptionsStorage;

/**
 * Save content from GUI in system, before close the dialog. Set saved content
 * in GUI.
 * 
 * @author intern4
 *
 */
public class ContentPersisterImpl implements ContentPersister {

	@Override
	public void saveContent(CheckerFrame frame) {
		WSOptionsStorage optionsStorage = PluginWorkspaceProvider.getPluginWorkspace().getOptionsStorage();

		// save state of checkCurrentFile radioButton
		if (frame.getCheckCurrent().isSelected()) {
			optionsStorage.setOption(Tags.CHECK_FILE_KEY, Tags.SET);
		} else {
			optionsStorage.setOption(Tags.CHECK_FILE_KEY, Tags.NOT_SET);
		}

		// save state of checkExternalLinks CheckBox
		if (frame.getCheckExternalLinksCBox().isSelected()) {
			optionsStorage.setOption(Tags.CHECK_EXTERNAL_KEY, Tags.SET);
		} else {
			optionsStorage.setOption(Tags.CHECK_EXTERNAL_KEY, Tags.NOT_SET);
		}

		// save state of checkImages CheckBox
		if (frame.getCheckImagesCBox().isSelected()) {
			optionsStorage.setOption(Tags.CHECK_IMAGES_KEY, Tags.SET);
		} else {
			optionsStorage.setOption(Tags.CHECK_IMAGES_KEY, Tags.NOT_SET);
		}

		// save state of checkInternalLink CheckBox
		if (frame.getCheckInternalLinksCbox().isSelected()) {
			optionsStorage.setOption(Tags.CHECK_INTERNAL_KEY, Tags.SET);
		} else {
			optionsStorage.setOption(Tags.CHECK_INTERNAL_KEY, Tags.NOT_SET);
		}

		// save table
		DefaultTableModel tableModel = frame.getTablePanelCreator().getTableModel();
		List<String> tabelList = new ArrayList<String>();

		for (int i = 0; i < tableModel.getRowCount(); i++) {
			tabelList.add(tableModel.getValueAt(i, 0).toString());
		}
		optionsStorage.setOption(Tags.TABLE_ROWS, String.join(";", tabelList));

	}

	@Override
	public void setSavedContent(CheckerFrame frame) {
		WSOptionsStorage optionsStorage = PluginWorkspaceProvider.getPluginWorkspace().getOptionsStorage();
		String value;

		// set checkCurrent radioButton or checkOther radioButton
		value = optionsStorage.getOption(Tags.CHECK_FILE_KEY, Tags.SET);
		System.out.println(value);
		if (value.equals(Tags.SET)) {
			frame.getCheckCurrent().doClick();
		} else {
			frame.getCheckOtherFiles().doClick();
		}

		// set checkExternalLinks checkButton
		value = optionsStorage.getOption(Tags.CHECK_EXTERNAL_KEY, Tags.SET);
		if (value.equals(Tags.SET)) {
			frame.getCheckExternalLinksCBox().setSelected(true);
		} else {
			frame.getCheckExternalLinksCBox().setSelected(false);
		}

		// set checkImages checkButton
		value = optionsStorage.getOption(Tags.CHECK_INTERNAL_KEY, Tags.SET);
		if (value.equals(Tags.SET)) {
			frame.getCheckInternalLinksCbox().setSelected(true);
		} else {
			frame.getCheckInternalLinksCbox().setSelected(false);
		}
		// set checkInternalLinks checkButton
		value = optionsStorage.getOption(Tags.CHECK_IMAGES_KEY, Tags.SET);
		if (value.equals(Tags.SET)) {
			frame.getCheckImagesCBox().setSelected(true);
		} else {
			frame.getCheckImagesCBox().setSelected(false);
		}

		// set rows in table
		value = optionsStorage.getOption(Tags.TABLE_ROWS, "");
		if (!value.isEmpty()) {
			DefaultTableModel tableModel = frame.getTablePanelCreator().getTableModel();

			// split value String in list with Strings
			List<String> rowList = new ArrayList<String>(Arrays.asList(value.split(";")));

			// add row in table model
			for (int i = 0; i < rowList.size(); i++) {
				tableModel.addRow(new String[] { rowList.get(i) });
			}
		}
	}

}
