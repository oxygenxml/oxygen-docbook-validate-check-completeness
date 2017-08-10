package com.oxygenxml.docbookChecker.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.ProgressMonitor;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.docbookChecker.Worker;
import com.oxygenxml.docbookChecker.persister.ContentPersister;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;
import com.oxygenxml.docbookChecker.translator.Tags;
import com.oxygenxml.docbookChecker.translator.Translator;
import com.oxygenxml.ldocbookChecker.parser.ParserCreator;
import com.oxygenxml.profiling.ProfileConditionsFromDocsWorker;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

import ro.sync.ecss.extensions.commons.ui.OKCancelDialog;

/**
 * The GUI for Broken Links Checker
 * 
 * @author intern4
 *
 */
public class CheckerFrame extends OKCancelDialog
		implements CheckerInteractor, ProgressMonitorReporter, java.beans.PropertyChangeListener {

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

	private ProfilingConditionsInformationsImpl profilingConditionsInformations = new ProfilingConditionsInformationsImpl();

	/**
	 * This JDialog
	 */
	private CheckerFrame thisJDialog = this;

	/**
	 * 
	 */
	private Translator translator;

	private ProgressMonitor progressMonitor;

	private Worker worker;

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
		profilingPanelCreator = new ProfilingPanelCreator(translator, component);

		// Initialize GUI
		initGUI();

		// add action listener on add button
		tablePanelCreater.addListenerOnAddBtn(createAddBtnAction(fileChooser, tablePanelCreater, component));
		profilingPanelCreator.addListenerOnAddBtn(createAddBtnAction(fileChooser, profilingPanelCreator, component));

		tablePanelCreater.addListenerOnRemoveBtn(createRemoveBtnAction(tablePanelCreater));
		profilingPanelCreator.addListenerOnRemoveBtn(createRemoveBtnAction(profilingPanelCreator));

		profilingPanelCreator.addListenerOnGetBtn(createGetBtnAction(url, problemReporter));

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

		setResizable(true);
		setMinimumSize(new Dimension(350, 500));
		pack();
		setSize(new Dimension(450, 550));
		setLocationRelativeTo(component);
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

		// add JLa
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
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
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
		checkInternalLinksCbox.setBackground(Color.WHITE);
		checkInternalLinksCbox.setSelected(true);
		checkInternalLinksCbox.setText(translator.getTraslation(Tags.CHECK_INTERNAL_KEY));
		mainPanel.add(checkInternalLinksCbox, gbc);

		gbc.gridy++;
		checkImagesCBox.setBackground(Color.WHITE);
		gbc.insets = new Insets(5, 0, 10, 0);
		checkImagesCBox.setSelected(true);
		checkImagesCBox.setText(translator.getTraslation(Tags.CHECK_IMAGES_KEY));
		mainPanel.add(checkImagesCBox, gbc);

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

			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> listUrl = new ArrayList<String>();

				if (checkCurrent.isSelected()) {

					// clear last reported problems
					problemReporter.clearReportedProblems();

					listUrl.add(url);

					// create the progress monitor
					progressMonitor = new ProgressMonitor(CheckerFrame.this, "Operation in progress...", "", 0, 100);
					progressMonitor.setProgress(0);

					worker = new Worker(listUrl, thisJDialog, parseCreator, problemReporter, statusReporter, thisJDialog);
					worker.addPropertyChangeListener(thisJDialog);
					worker.execute();

					thisJDialog.setVisible(false);
					thisJDialog.dispose();
				} else {
					DefaultTableModel tableModel = tablePanelCreater.getTableModel();

					for (int i = 0; i < tableModel.getRowCount(); i++) {
						listUrl.add("" + tableModel.getValueAt(i, 0));
					}
					if (!listUrl.isEmpty()) {

						// create the progress monitor
						progressMonitor = new ProgressMonitor(CheckerFrame.this, "Operation in progress...", "", 0, 100);
						progressMonitor.setProgress(0);

						// clear last reported problems
						problemReporter.clearReportedProblems();
						worker = new Worker(listUrl, thisJDialog, parseCreator, problemReporter, statusReporter, thisJDialog);
						worker.addPropertyChangeListener(thisJDialog);

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
	private ActionListener createAddBtnAction(final FileChooserCreator fileChooser, final TablePanelAccess tableAccess,
			final Component component) {
		ActionListener addBtnAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Add button for file table.
				if (tableAccess instanceof TableFilesPanelCreator) {
					File[] files = fileChooser.createFileChooser(translator.getTraslation(Tags.FILE_CHOOSER_TITLE),
							translator.getTraslation(Tags.FILE_CHOOSER_BUTTON));
					if (files != null) {
						tablePanelCreater.addRowsInTable(files);
					}
				}
				// add button for conditions table.
				else if (tableAccess instanceof ProfilingPanelCreator) {
					profilingPanelCreator.getAddBtn().setEnabled(false);
					profilingPanelCreator.displayAllConditions(
							profilingConditionsInformations.getProfileConditions(ProfilingConditionsInformations.DOCBOOK));
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
				profilingPanelCreator.getRemvBtn().setEnabled(false);
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

	private ActionListener createGetBtnAction(final String url, final ProblemReporter problemReporter) {

		ActionListener getBtnAction = new ActionListener() {
			// Worker for get profile conditions from a document;
			ProfileConditionsFromDocsWorker worker;

			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> urls = new ArrayList<String>();
				worker = new ProfileConditionsFromDocsWorker(urls, profilingPanelCreator, problemReporter);
				if (checkCurrent.isSelected()) {
					urls.add(url);
					worker.execute();
				} else {
					DefaultTableModel tableModel = tablePanelCreater.getTableModel();

					for (int i = 0; i < tableModel.getRowCount(); i++) {
						urls.add("" + tableModel.getValueAt(i, 0));
					}
					if (!urls.isEmpty()) {

						// clear last reported problems
						worker.execute();
					} else {
						JOptionPane.showMessageDialog(thisJDialog, translator.getTraslation(Tags.EMPTY_TABLE), "",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		};
		return getBtnAction;
	}

	@Override
	public boolean isSelectedCheckCurrent() {
		return checkCurrent.isSelected();
	}

	@Override
	public List<String> getFilesTableRows() {
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

	@Override
	public boolean isSelectedCheckUsingProfile() {
		return profilingPanelCreator.getProfilingCondCBox().isSelected();
	}

	@Override
	public Map<String, Set<String>> getConditionsTableRows() {
		Map<String, Set<String>> toReturn = new HashMap<String, Set<String>>();
		DefaultTableModel model = profilingPanelCreator.getModelTable();
		Set<String> value;

		for (int i = 0; i < model.getRowCount(); i++) {
			value = new HashSet<String>();
			String[] vectValue = model.getValueAt(i, 1).toString().split(";");
			for (int j = 0; j < vectValue.length; j++) {
				value.add(vectValue[j]);
			}
			toReturn.put(model.getValueAt(i, 0).toString(), value);
		}
		return toReturn;
	}

	@Override
	public boolean isSelectedConfigConditionsSet() {
		return profilingPanelCreator.getConfigProfilingRBtn().isSelected();
	}

	@Override
	public void doClickOnConfigConditionSet() {
		profilingPanelCreator.getConfigProfilingRBtn().doClick();
	}

	@Override
	public void doClickOnCheckAllConbinations() {
		profilingPanelCreator.getCheckAllProfilingRBtn().doClick();
	}

	@Override
	public void setUseProfiligConditions(boolean state) {
		profilingPanelCreator.getProfilingCondCBox().setSelected(state);

	}

	@Override
	public void setRowsInConditionsTable(List<String[]> rows) {
		DefaultTableModel tableModel = profilingPanelCreator.getModelTable();
		for (int i = 0; i < rows.size(); i++) {
			tableModel.addRow(rows.get(i));
		}
	}

	@Override
	public void reportNote(String note) {
		System.out.println("setNote: " + note);
		progressMonitor.setNote(note);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		System.out.println("property change dialog");

		// if the worker is finished or has been canceled by
		// the user, take appropriate action
		if (progressMonitor.isCanceled()) {
			System.out.println("cancel*********************************************");
			try {
				worker.cancel(true);
			} catch (Exception e) {
				System.out.println("===========================+ am prins exceptia +===================");
				e.printStackTrace();
			}
		}
		else if (event.getPropertyName().equals("progress")) {
			// get the % complete from the progress event
			// and set it on the progress monitor
			int progress = ((Integer) event.getNewValue()).intValue();
			System.out.println("set progress in propertyCHange: " + progress);
			progressMonitor.setProgress(progress);
		}
	}

	@Override
	public void doClickOnUseProfilingConditions() {
		profilingPanelCreator.getProfilingCondCBox().doClick();
	}

	@Override
	public void closeMonitor() {
		progressMonitor.close();

	}

}
