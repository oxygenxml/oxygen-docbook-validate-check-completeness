package com.oxygenxml.docbook.checker.gui;

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
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ProgressMonitor;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.docbook.checker.OxygenInteractor;
import com.oxygenxml.docbook.checker.OxygenSourceDescription;
import com.oxygenxml.docbook.checker.ValidationWorker;
import com.oxygenxml.docbook.checker.parser.ParserCreator;
import com.oxygenxml.docbook.checker.persister.ContentPersister;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.StatusReporter;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

import ro.sync.ecss.extensions.commons.ui.OKCancelDialog;
import ro.sync.exml.workspace.api.PluginWorkspaceProvider;

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
	 * Check box to select that undefined conditions to be reported.
	 */
	private JCheckBox reportUndefinedConditionsCBox = new JCheckBox();
	
	/**
	 * Creator for table panel.
	 */
	private TableFilesPanelCreator filesTablePanelCreater;

	/**
	 * Creator for profiling panel.
	 */
	private ProfilingPanelCreator profilingPanelCreator;

	/**
	 * Link to Git repository description.
	 */
	private String linkToGitHub = "https://github.com/oxygenxml/docbook-validate-check-completeness";
	
	/**
	 * Translator used for internationalization.
	 */
	private Translator translator;

	/**
	 * Progress monitor.
	 */
	private ProgressMonitor progressMonitor;

	/**
	 * Validation worker.
	 */
	private ValidationWorker validationWorker;
	
	/**
	 * Problem reporter.
	 */
	private ProblemReporter problemReporter;
	
	/**
	 * Parser creator.
	 */
	private ParserCreator parseCreator;
	
	/**
	 * Status reporter.
	 */
	private StatusReporter statusReporter;
	
	/**
	 * Content persister
	 */
	private ContentPersister contentPersister;
	private OxygenSourceDescription sourceDescription;
	private OxygenInteractor oxygenInteractor;
	

	/**
	 * Constructor
	 */
	public DocBookCheckerDialog(OxygenSourceDescription sourceDescription, OxygenInteractor oxygenInteractor, Component component, ProblemReporter problemReporter, StatusReporter statusReporter,
			FileChooser fileChooser, ParserCreator parseCreator, ContentPersister contentPersister,
			Translator translator) {
		super((JFrame) component, translator.getTranslation(Tags.FRAME_TITLE), true);
		this.sourceDescription = sourceDescription;
		this.oxygenInteractor = oxygenInteractor;
		this.problemReporter = problemReporter;
		this.statusReporter = statusReporter;
		this.parseCreator = parseCreator;
		this.contentPersister = contentPersister;

		this.translator = translator;
		filesTablePanelCreater = new TableFilesPanelCreator(translator, fileChooser, this.getOkButton());

		profilingPanelCreator = new ProfilingPanelCreator(checkCurrent, filesTablePanelCreater.getTableModel(), sourceDescription.getCurrentUrl(), problemReporter, translator, component);

		// Initialize GUI
		initGUI();

		// add action listener on radio buttons
		checkCurrent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getOkButton().setEnabled(true);
				filesTablePanelCreater.getTable().clearSelection();
				filesTablePanelCreater.getAddBtn().setEnabled(false);
				filesTablePanelCreater.getRemvBtn().setEnabled(false);
			}
		});
		checkOtherFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(getOtherFilesToCheck().isEmpty()){
					System.out.println("table is empty");
					//disable the check button if is not other filesToCheck
					getOkButton().setEnabled(false);
				}
				
				filesTablePanelCreater.getAddBtn().setEnabled(true);
			}
		});

		// Load saved state of the dialog
		contentPersister.loadState(this);
		
		//Adapt at source of action that open the dialog 
		adaptAtSourceOfAction();
		
		//set the text of OK button
		setOkButtonText(translator.getTranslation(Tags.CHECK_BUTTON));

		setResizable(true);
		setMinimumSize(new Dimension(350, 500));
		setSize(new Dimension(450, 550));
		setLocationRelativeTo(component);
		setVisible(true);
	}
	
	
	@Override
	protected void doOK() {

		List<String> listUrls = new ArrayList<String>();

		//get a list with URLs to be verified
		if (checkCurrent.isSelected()) {
				listUrls.add(sourceDescription.getCurrentUrl());
		} else {
			DefaultTableModel tableModel = filesTablePanelCreater.getTableModel();

			for (int i = 0; i < tableModel.getRowCount(); i++) {
				listUrls.add(String.valueOf(tableModel.getValueAt(i, 0)));
			}
		}
		
		//check the table with manually defined conditions is not empty
		if (!(isUsingProfile() && isUseManuallyConfiguredConditionsSet() && getDefinedConditions().isEmpty()) ) {

			// create the progress monitor
			progressMonitor = new ProgressMonitor(DocBookCheckerDialog.this, translator.getTranslation(Tags.PROGRESS_MONITOR_MESSAGE), "", 0, 100);
			progressMonitor.setProgress(0);

			// clear last reported problems
			validationWorker = new ValidationWorker(listUrls, oxygenInteractor , this, parseCreator, problemReporter, statusReporter, this);
			validationWorker.addPropertyChangeListener(this);

			validationWorker.execute();
			contentPersister.saveState(this);
			oxygenInteractor.setButtonsEnable(false);
			super.doOK();

		} else {
				PluginWorkspaceProvider.getPluginWorkspace().showWarningMessage(translator.getTranslation(Tags.EMPTY_CONDITIONS_TABLE));
		}
	}

	/**
	 * Initialize GUI
	 */
	private void initGUI() {

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

		
		//add checkCurrent radio button
		gbc.gridy++;
		gbc.insets = new Insets(0, 10, 0, 0);
		checkCurrent.setText(translator.getTranslation(Tags.CHECK_CURRENT_FILE_KEY));
		mainPanel.add(checkCurrent, gbc);

		//add checkOtherFiles radio button
		gbc.gridy++;
		checkOtherFiles.setText(translator.getTranslation(Tags.CHECK_OTHER_FILES_KEY));
		mainPanel.add(checkOtherFiles, gbc);

		//add filesTable panel
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 30, 10, 5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(filesTablePanelCreater.create(), gbc);

		//add the panel for profiling
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(15, 0, 0, 0);
		mainPanel.add(profilingPanelCreator.create(), gbc);

		//add checkExternal checkBox
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

		//add checkInternal checkBox
		gbc.gridy++;
		checkInternalLinksCbox.setSelected(true);
		checkInternalLinksCbox.setText(translator.getTranslation(Tags.CHECK_INTERNAL_KEY));
		mainPanel.add(checkInternalLinksCbox, gbc);

		//add checkImage checkBox
		gbc.gridy++;
		checkImagesCBox.setSelected(true);
		checkImagesCBox.setText(translator.getTranslation(Tags.CHECK_IMAGES_KEY));
		mainPanel.add(checkImagesCBox, gbc);
		
		//add reportUndefined checkBox
		gbc.gridy++;
		gbc.insets = new Insets(5, 0, 10, 0);
		reportUndefinedConditionsCBox.setSelected(true);
		reportUndefinedConditionsCBox.setText(translator.getTranslation(Tags.REPORT_UNDEFINED_CONDITIONS));
		mainPanel.add(reportUndefinedConditionsCBox,gbc);
		
		getContentPane().add(mainPanel);
	}

	
	private void adaptAtSourceOfAction() {
		if(OxygenSourceDescription.CONTEXTUAL.equals(sourceDescription.getSource())){
			checkCurrent.doClick();
		}
		else if(sourceDescription.getCurrentUrl() == null){
			checkCurrent.setEnabled(false);
			checkOtherFiles.doClick();
		}
		
	}
	

	//----Implementation of CheckerInteractor
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
	public List<String> getOtherFilesToCheck() {
		return filesTablePanelCreater.getTableUrls();
	}
	@Override
	public void setOtherFilesToCheck(List<String> resources) {
		filesTablePanelCreater.addRowsInTable(resources);
	}


	@Override
	public boolean isCheckExternal() {
		return checkExternalLinksCBox.isSelected();
	}

	@Override
	public boolean isCheckImages() {
		return checkImagesCBox.isSelected();
	}

	@Override
	public boolean isCheckInternal() {
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
	public boolean isReporteUndefinedConditions() {
		return reportUndefinedConditionsCBox.isSelected();
	}

	@Override
	public void setReporteUndefinedConditions(boolean state) {
		reportUndefinedConditionsCBox.setSelected(state);
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



	
//----------Implementation of PropertyChangeListener
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// if the worker is finished or has been canceled by
		// the user, take appropriate action
		if (progressMonitor.isCanceled()) {
			try {
				validationWorker.cancel(true);
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

	//---------Implementation of ProgressMonitorReporter
	@Override
	public void reportNote(String note) {
		if (!progressMonitor.isCanceled()) {
		progressMonitor.setNote(note);
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
