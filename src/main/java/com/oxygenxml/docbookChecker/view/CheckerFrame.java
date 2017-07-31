package com.oxygenxml.docbookChecker.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.docbookChecker.Worker;
import com.oxygenxml.docbookChecker.persister.ContentPersister;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;
import com.oxygenxml.docbookChecker.translator.Tags;
import com.oxygenxml.docbookChecker.translator.Translator;
import com.oxygenxml.ldocbookChecker.parser.ParserCreator;

import ro.sync.ecss.extensions.commons.ui.OKCancelDialog;

/**
 * The GUI for Broken Links Checker
 * 
 * @author intern4
 *
 */
public class CheckerFrame extends OKCancelDialog implements CheckerInteractor {

	/**
	 * Radio button for select to check current file
	 */
	private JRadioButton checkCurrent = new JRadioButton();
	/**
	 * Radio button for select to check other files
	 */
	private JRadioButton checkOtherFiles = new JRadioButton();

	/**
	 * Check box to select to check external links
	 */
	private JCheckBox checkExternalLinksCBox = new JCheckBox();
	/**
	 * Check box to select to check images
	 */
	private JCheckBox checkImagesCBox = new JCheckBox();
	/**
	 * Check box to select to check internal links
	 */
	private JCheckBox checkInternalLinksCbox = new JCheckBox();

	/**
	 * Creator for table panel.
	 */
	private TableFilesPanelCreator tablePanelCreater;

	
	/**
	 * Creator for profiling panel.
	 */
	private ProfilingPanelCreator profilingPanelCreator;

	
	/**
	 * This JDialog
	 */
	private CheckerFrame thisJDialog = this;

	/**
	 * 
	 */
	private Translator translator;

	/**
	 * 
	 * private String currentUrl;
	 * 
	 * /** Constructor
	 */
	public CheckerFrame(String url, Component component, ProblemReporter problemReporter, StatusReporter statusReporter,
			FileChooserCreator fileChooser, ParserCreator parseCreator, ContentPersister contentPersister,
			Translator translator) {
		super((JFrame) component, translator.getTraslation(Tags.FRAME_TITLE), true);

		this.translator = translator;
		tablePanelCreater = new TableFilesPanelCreator(translator);
		profilingPanelCreator = new ProfilingPanelCreator(translator);

		// Initialize GUI
		initGUI();

		// add action listener on add button
		tablePanelCreater.addListenerOnAddBtn(createAddBtnAction(fileChooser, tablePanelCreater));
		profilingPanelCreator.addListenerOnAddBtn(createAddBtnAction(fileChooser, profilingPanelCreator));

		tablePanelCreater.addListenerOnRemoveBtn(createRemoveBtnAction(tablePanelCreater));
		profilingPanelCreator.addListenerOnRemoveBtn(createRemoveBtnAction(profilingPanelCreator));
		
		
		// add action listener on radio buttons
		checkCurrent.addActionListener(createCheckCurrentAction());
		checkOtherFiles.addActionListener(createCheckOtherAction());

		// set saved content
		contentPersister.setSavedContent(this);

		// add action listener on check/stop button
		getOkButton().addActionListener(createCheckBtnAction(url, component, problemReporter, statusReporter, fileChooser,
				parseCreator, contentPersister));

		setOkButtonText(translator.getTraslation(Tags.CHECK_BUTTON));
		setCancelButtonText(translator.getTraslation(Tags.CANCEL_BUTTON));

		// set background color on main panel and buttons panel of OKCancelDialog
		getOkButton().getParent().setBackground(Color.WHITE);
		getOkButton().getParent().getParent().setBackground(Color.WHITE);

		pack();
		setLocationRelativeTo(component);
		setMinimumSize(new Dimension(350, 450));
		setSize(new Dimension(400, 500));
		setVisible(true);
		setFocusable(true);

	}


	/**
	 * Initialize GUI
	 */
	private void initGUI() {

		JPanel mainPanel = new JPanel(new GridBagLayout());

		mainPanel.setBackground(Color.WHITE);

		// Constrains for GridBagLayout manager.
		GridBagConstraints gbc = new GridBagConstraints();

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(checkCurrent);
		group.add(checkOtherFiles);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		mainPanel.add(new JLabel(translator.getTraslation(Tags.SELECT_FILES_LABEL_KEY)), gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 10, 0, 0);
		checkCurrent.setBackground(Color.WHITE);
		checkCurrent.setText(translator.getTraslation(Tags.CHECK_FILE_KEY));
		mainPanel.add(checkCurrent, gbc);

		gbc.gridy++;
		checkOtherFiles.setBackground(Color.WHITE);
		checkOtherFiles.setText(translator.getTraslation(Tags.CHECK_OTHER_FILES_KEY));
		mainPanel.add(checkOtherFiles, gbc);

		gbc.gridy++;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 30, 10, 5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(tablePanelCreater.create(), gbc);

		gbc.gridy++;
		gbc.insets = new Insets(15, 0, 0, 0);
		mainPanel.add(profilingPanelCreator.create(), gbc);

		
		gbc.gridy++;
		gbc.weighty = 0;
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 0, 0, 0);
		checkExternalLinksCBox.setBackground(Color.WHITE);
		checkExternalLinksCBox.setSelected(true);
		checkExternalLinksCBox.setText(translator.getTraslation(Tags.CHECK_EXTERNAL_KEY));
		mainPanel.add(checkExternalLinksCBox, gbc);

		gbc.gridy++;
		checkImagesCBox.setBackground(Color.WHITE);
		checkImagesCBox.setSelected(true);
		checkImagesCBox.setText(translator.getTraslation(Tags.CHECK_IMAGES_KEY));
		mainPanel.add(checkImagesCBox, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(5, 0, 10, 0);
		checkInternalLinksCbox.setBackground(Color.WHITE);
		checkInternalLinksCbox.setSelected(true);
		checkInternalLinksCbox.setText(translator.getTraslation(Tags.CHECK_INTERNAL_KEY));
		mainPanel.add(checkInternalLinksCbox, gbc);

		getContentPane().add(mainPanel);
	}


	/**
	 * Create action listener for checkCurrent checkBox
	 */
	private ActionListener createCheckCurrentAction() {
		ActionListener checkCurrentAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tablePanelCreater.getTable().clearSelection();
				tablePanelCreater.getAddBtn().setEnabled(false);
				tablePanelCreater.getRemvBtn().setEnabled(false);
			}
		};
		return checkCurrentAction;
	}

	/**
	 * Create action listener for checkOtherFiles checkBox
	 */
	private ActionListener createCheckOtherAction() {
		ActionListener checkOtherAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tablePanelCreater.getAddBtn().setEnabled(true);
			}
		};
		return checkOtherAction;
	}

	/**
	 * create actionListener for check Button
	 */
	private ActionListener createCheckBtnAction(final String url, Component component,
			final ProblemReporter problemReporter, final StatusReporter statusReporter, FileChooserCreator fileChooser,
			final ParserCreator parseCreator, final ContentPersister contentPersister) {
		ActionListener checkBtnAction = new ActionListener() {
			Worker worker;

			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> listUrl = new ArrayList<String>();

				if (checkCurrent.isSelected()) {
					JOptionPane.showMessageDialog(thisJDialog, url, "", JOptionPane.WARNING_MESSAGE);

					// clear last reported problems
					problemReporter.clearReportedProblems();

					listUrl.add(url);
					worker = new Worker(listUrl, thisJDialog, parseCreator, problemReporter, statusReporter);

					worker.execute();

					thisJDialog.setVisible(false);
					thisJDialog.dispose();
				} else {
					DefaultTableModel tableModel = tablePanelCreater.getTableModel();

					for (int i = 0; i < tableModel.getRowCount(); i++) {
						listUrl.add("" + tableModel.getValueAt(i, 0));
					}
					if (!listUrl.isEmpty()) {

						// clear last reported problems
						problemReporter.clearReportedProblems();
						worker = new Worker(listUrl, thisJDialog, parseCreator, problemReporter, statusReporter);

						worker.execute();
						thisJDialog.setVisible(false);
						thisJDialog.dispose();
					} else {
						JOptionPane.showMessageDialog(thisJDialog, translator.getTraslation(Tags.EMPTY_TABLE), "",
								JOptionPane.WARNING_MESSAGE);
					}

				}
				contentPersister.saveContent(thisJDialog);
			}
		};
		return checkBtnAction;
	}
	
	
	/**
	 * Create ActionListener for add button
	 */
	private ActionListener createAddBtnAction(final FileChooserCreator fileChooser, final TablePanelAccess tableAccess) {
		ActionListener addBtnAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tableAccess instanceof  TableFilesPanelCreator){ 
				File[] files = fileChooser.createFileChooser(translator.getTraslation(Tags.FILE_CHOOSER_TITLE),
						translator.getTraslation(Tags.FILE_CHOOSER_BUTTON));
				if (files != null) {
					DefaultTableModel tableModel = tableAccess.getTableModel();
					for (int i = 0; i < files.length; i++) {
						tableModel.addRow(new String[] { files[i].toString() });
						
					}
				}
				}
				else if(tableAccess instanceof ProfilingPanelCreator){
					JOptionPane.showMessageDialog(thisJDialog,
					    "aici apare un inputDialog");
				}
			}
		};
		return addBtnAction;
	}

	/**
	 * Create action listener for table remove button
	 */
	private ActionListener createRemoveBtnAction(final TablePanelAccess tableAccess) {
		ActionListener removeBtnAction = new ActionListener() {
			JTable table = tableAccess.getTable();
			DefaultTableModel model = tableAccess.getTableModel();

			@Override
			public void actionPerformed(ActionEvent e) {
				int index0 = table.getSelectionModel().getMinSelectionIndex();
				int index1 = table.getSelectionModel().getMaxSelectionIndex();

				for (int i = index1; i >= index0; i--) {
					int modelRow = table.convertRowIndexToModel(i);
					model.removeRow(modelRow);
				}
			}
		};
		return removeBtnAction;
	}

	@Override
	public boolean isSelectedCheckCurrent() {
		return checkCurrent.isSelected();
	}

	@Override
	public List<String> getTableRows() {
		List<String> toReturn = new ArrayList<String>();

		DefaultTableModel tabelModel = tablePanelCreater.getTableModel();

		// add rows in a list
		for (int i = 0; i < tabelModel.getRowCount(); i++) {
			toReturn.add(tabelModel.getValueAt(i, 0).toString());
		}
		return toReturn;
	}

	@Override
	public boolean isSelectedCheckExternal() {
		return checkExternalLinksCBox.isSelected();
	}

	@Override
	public boolean isSelectedCheckImages() {
		return checkImagesCBox.isSelected();
	}

	@Override
	public boolean isSelectedCheckInternal() {
		return checkInternalLinksCbox.isSelected();
	}

	@Override
	public void doClickOnCheckCurrentLink() {
		checkCurrent.doClick();
	}

	@Override
	public void doClickOnCheckOtherLink() {
		checkOtherFiles.doClick();
	}

	@Override
	public void setCheckExternal(boolean state) {
		checkExternalLinksCBox.setSelected(state);
	}

	@Override
	public void setCheckImages(boolean state) {
		checkImagesCBox.setSelected(state);
	}

	@Override
	public void setCheckInternal(boolean state) {
		checkInternalLinksCbox.setSelected(state);
	}

	@Override
	public void setRowsInFilesTable(List<String> rows) {
		if (!rows.isEmpty()) {
			DefaultTableModel tableModel = tablePanelCreater.getTableModel();
			// add row in table model
			for (int i = 0; i < rows.size(); i++) {
				tableModel.addRow(new String[] { rows.get(i) });
			}
		}
	}
}
