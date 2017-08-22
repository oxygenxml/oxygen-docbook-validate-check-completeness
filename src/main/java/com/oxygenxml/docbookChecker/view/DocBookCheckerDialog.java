package com.oxygenxml.docbookChecker.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ProgressMonitor;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.docbookChecker.ValidationWorker;
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
public class DocBookCheckerDialog extends OKCancelDialog
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
	private TableFilesPanelCreator fileTablePanelCreater;

	/**
	 * Creator for profiling panel.
	 */
	private ProfilingPanelCreator profilingPanelCreator;

	
	private String linkToGitHub = "https://github.com/oxygenxml/docbook-validate-check-completeness";
	
	/**
	 * 
	 */
	private Translator translator;

	private ProgressMonitor progressMonitor;

	private ValidationWorker worker;
	private ProblemReporter problemReporter;
	private ParserCreator parseCreator;
	private StatusReporter statusReporter;
	private ContentPersister contentPersister;
	private String currentOpenedFileURL;

	/**
	 * 
	 * private String currentUrl;
	 * 
	 * /** Constructor
	 */
	public DocBookCheckerDialog(String currentOpenedFileURL, Component component, ProblemReporter problemReporter, StatusReporter statusReporter,
			FileChooser fileChooser, ParserCreator parseCreator, ContentPersister contentPersister,
			Translator translator) {
		super((JFrame) component, translator.getTranslation(Tags.FRAME_TITLE), true);
		this.currentOpenedFileURL = currentOpenedFileURL;
		this.problemReporter = problemReporter;
		this.statusReporter = statusReporter;
		this.parseCreator = parseCreator;
		this.contentPersister = contentPersister;

		this.translator = translator;
		fileTablePanelCreater = new TableFilesPanelCreator(translator, fileChooser);

		profilingPanelCreator = new ProfilingPanelCreator(checkCurrent, fileTablePanelCreater.getTableModel(), currentOpenedFileURL, problemReporter, translator, component);

		// Initialize GUI
		initGUI(currentOpenedFileURL);



		// add action listener on radio buttons
		checkCurrent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileTablePanelCreater.getTable().clearSelection();
				fileTablePanelCreater.getAddBtn().setEnabled(false);
				fileTablePanelCreater.getRemvBtn().setEnabled(false);
			}
		});
		checkOtherFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileTablePanelCreater.getAddBtn().setEnabled(true);
			}
		});

		// Load saved state of the dialog
		contentPersister.loadState(this);

		setOkButtonText(translator.getTranslation(Tags.CHECK_BUTTON));
		setCancelButtonText(translator.getTranslation(Tags.CANCEL_BUTTON));

		setResizable(true);
		setMinimumSize(new Dimension(350, 500));
		setSize(new Dimension(450, 550));
		setLocationRelativeTo(component);
		setVisible(true);
	}
	
	@Override
	protected void doOK() {

		List<String> listUrl = new ArrayList<String>();

		if (checkCurrent.isSelected()) {
			if(currentOpenedFileURL != null){
				listUrl.add(currentOpenedFileURL);
			}
		} else {
			DefaultTableModel tableModel = fileTablePanelCreater.getTableModel();

			for (int i = 0; i < tableModel.getRowCount(); i++) {
				listUrl.add(String.valueOf(tableModel.getValueAt(i, 0)));
			}
		}
		
		if (!listUrl.isEmpty()) {

			// create the progress monitor
			progressMonitor = new ProgressMonitor(DocBookCheckerDialog.this, translator.getTranslation(Tags.PROGRESS_MONITOR_MESSAGE), "", 0, 100);
			progressMonitor.setProgress(0);

			// clear last reported problems
			worker = new ValidationWorker(listUrl, this, parseCreator, problemReporter, statusReporter, this);
			worker.addPropertyChangeListener(this);

			worker.execute();
		} else {
			JOptionPane.showMessageDialog(this, translator.getTranslation(Tags.EMPTY_TABLE), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
		
		contentPersister.saveState(this);
		super.doOK();
	}

	/**
	 * Initialize GUI
	 */
	private void initGUI(String currentOpenedFileURL) {

		JPanel mainPanel = new JPanel(new GridBagLayout());
		// Constrains for GridBagLayout manager.
		GridBagConstraints gbc = new GridBagConstraints();

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(checkCurrent);
		group.add(checkOtherFiles);

		// add JLabel for select file
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		mainPanel.add(new JLabel(translator.getTranslation(Tags.SELECT_FILES_LABEL_KEY)), gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 10, 0, 0);
		checkCurrent.setText(translator.getTranslation(Tags.CHECK_CURRENT_FILE_KEY));
		if(currentOpenedFileURL == null){
			checkCurrent.setEnabled(false);
			checkOtherFiles.setSelected(true);
		}
		mainPanel.add(checkCurrent, gbc);

		gbc.gridy++;
		checkOtherFiles.setText(translator.getTranslation(Tags.CHECK_OTHER_FILES_KEY));
		mainPanel.add(checkOtherFiles, gbc);

		gbc.gridy++;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 30, 10, 5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(fileTablePanelCreater.create(), gbc);

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
		checkExternalLinksCBox.setSelected(true);
		checkExternalLinksCBox.setText(translator.getTranslation(Tags.CHECK_EXTERNAL_KEY));
		mainPanel.add(checkExternalLinksCBox, gbc);

		gbc.gridy++;
		checkInternalLinksCbox.setSelected(true);
		checkInternalLinksCbox.setText(translator.getTranslation(Tags.CHECK_INTERNAL_KEY));
		mainPanel.add(checkInternalLinksCbox, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(5, 0, 10, 0);
		checkImagesCBox.setSelected(true);
		checkImagesCBox.setText(translator.getTranslation(Tags.CHECK_IMAGES_KEY));
		mainPanel.add(checkImagesCBox, gbc);

		getContentPane().add(mainPanel);
	}



	
	@Override
	public boolean isCheckCurrentResource() {
		return checkCurrent.isSelected();
	}

	@Override
	public void setCheckCurrentResource(boolean checkCurrentResource) {
		if(checkCurrentResource){
			checkCurrent.doClick();
		} else {
			checkOtherFiles.doClick();
		}
	}

	
	@Override
	public List<String> getOtherResourcesToCheck() {
		return fileTablePanelCreater.getTableUrls();
	}
	@Override
	public void setResourcesToCheck(List<String> resources) {
		fileTablePanelCreater.addRowsInTable(resources);
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
	public boolean isUsingProfile() {
		return profilingPanelCreator.getProfilingCondCBox().isSelected();
	}
	@Override
	public void setUseProfiligConditions(boolean state) {
		profilingPanelCreator.getProfilingCondCBox().setSelected(!state);
		profilingPanelCreator.getProfilingCondCBox().doClick();
	}

	@Override
	public boolean isUseManuallyConfiguredConditionsSet() {
		return profilingPanelCreator.getConfigProfilingRBtn().isSelected();
	}
	@Override
	public void setUseManuallyConfiguredConditionsSet(boolean useManuallyConfiguredConditionsSet) {
		if(useManuallyConfiguredConditionsSet){
			profilingPanelCreator.getConfigProfilingRBtn().doClick();
		} else {
			profilingPanelCreator.getCheckAllProfilingRBtn().doClick();
		}
	}
	
	@Override
	public LinkedHashMap<String, LinkedHashSet<String>> getDefinedConditions() {
		return profilingPanelCreator.getConditionsFromTable();
	}

	@Override
	public void setDefinedConditions(LinkedHashMap<String, String> conditions) {
		profilingPanelCreator.addInTable(conditions);
	}

	@Override
	public void reportNote(String note) {
		if (!progressMonitor.isCanceled()) {
		progressMonitor.setNote(note);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// if the worker is finished or has been canceled by
		// the user, take appropriate action
		if (progressMonitor.isCanceled()) {
			try {
				worker.cancel(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (event.getPropertyName().equals("progress")) {
			// get the % complete from the progress event
			// and set it on the progress monitor
			int progress = ((Integer) event.getNewValue()).intValue();
			progressMonitor.setProgress(progress);
		}
	}

	@Override
	public void closeMonitor() {
		progressMonitor.close();

	}

	@Override
	public String getHelpPageID() {
		return linkToGitHub;
	}
}
