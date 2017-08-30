package com.oxygenxml.docbook.checker.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.docbook.checker.OxygenInteractor;
import com.oxygenxml.docbook.checker.OxygenSourceDescription;
import com.oxygenxml.docbook.checker.ValidationWorker;
import com.oxygenxml.docbook.checker.persister.ContentPersister;
import com.oxygenxml.docbook.checker.persister.ContentPersisterImpl;
import com.oxygenxml.docbook.checker.reporters.OxygenProblemReporter;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.translator.OxygenTranslator;
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
	 * Panel for select files to check
	 */
	private SelectFilesPanel selectFilePanel;

	/**
	 * Creator for profiling panel.
	 */
	private ProfilingPanel profilingPanel;

	/**
	 * Link to Git repository description.
	 */
	private String linkToGitHub = "https://github.com/oxygenxml/docbook-validate-check-completeness";
	
	/**
	 * Translator used for internationalization.
	 */
	private Translator translator = new OxygenTranslator();

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
	private ProblemReporter problemReporter = new OxygenProblemReporter();
	
	/**
	 * Content persister
	 */
	private ContentPersister contentPersister = new ContentPersisterImpl();
	/**
	 *The current open url.
	 */
	private String currentOpenUrl ;
	/**
	 * Oxygen interactor.
	 */
	private OxygenInteractor oxygenInteractor;
	

/**
 * Constructor.
 * @param sourceDescription
 * @param oxygenInteractor
 * @param component
 * @param problemReporter
 * @param statusReporter
 * @param parseCreator
 * @param contentPersister
 * @param translator
 */
	public DocBookCheckerDialog(OxygenSourceDescription sourceDescription, OxygenInteractor oxygenInteractor) {
		super(sourceDescription.getParrentFrame(), "", true);
		this.currentOpenUrl = sourceDescription.getCurrentUrl();
		this.oxygenInteractor = oxygenInteractor;

		selectFilePanel = new SelectFilesPanel(translator, this.getOkButton());

		profilingPanel = new ProfilingPanel(selectFilePanel, sourceDescription, problemReporter, translator);

		// Initialize GUI
		initGUI();

		// Load saved state of the dialog
		contentPersister.loadState(this);
		
		//Update the view according to oxygen SourceDescription of action that open this dialog.
		updateViewAcordingSourceDescription(sourceDescription);
		
		//set the text of OK button
		setOkButtonText(translator.getTranslation(Tags.CHECK_BUTTON));
		
		//set the dialog title
		setTitle(translator.getTranslation(Tags.FRAME_TITLE));
		
		setResizable(true);
		setMinimumSize(new Dimension(350, 520));
		setSize(new Dimension(470, 600));
		setLocationRelativeTo(sourceDescription.getParrentFrame());
		setVisible(true);
	}
	
	
	@Override
	protected void doOK() {

		List<String> listUrls = new ArrayList<String>();

		//get a list with URLs to be verified
		if (isCheckCurrentResource()) {
				listUrls.add(currentOpenUrl);
		} else {
			DefaultTableModel tableModel = selectFilePanel.getTableModel();

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
			validationWorker = new ValidationWorker(listUrls, oxygenInteractor , this, problemReporter, this);
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

		//add filesTable panel
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 10, 5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(selectFilePanel, gbc);

		//add the panel for profiling
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(15, 0, 0, 0);
		mainPanel.add(profilingPanel, gbc);

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

	/**
	 * Update the view according to oxygen SourceDescription.
	 */
	private void updateViewAcordingSourceDescription(OxygenSourceDescription sourceDescription) {
		 if(sourceDescription.getCurrentUrl() == null){
			 	selectFilePanel.getCheckCurrent().setEnabled(false);
				setCheckCurrentResource(false);
		 }
		 
		if(OxygenSourceDescription.CONTEXTUAL.equals(sourceDescription.getSource())){
			setCheckCurrentResource(true);
		}
		else if(OxygenSourceDescription.PROJECT_MANAGER.equals(sourceDescription.getSource())){
			setCheckCurrentResource(false);
			
			//clear table with files
			selectFilePanel.clearTable();
			setOtherFilesToCheck(sourceDescription.getSelectedFilesInProject());
			
		}
		
	}
	

	//----Implementation of CheckerInteractor
	@Override
	public boolean isCheckCurrentResource() {
		return selectFilePanel.getCheckCurrent().isSelected();
	}

	@Override
	public void setCheckCurrentResource(boolean checkCurrentResource) {
		if(checkCurrentResource){
			selectFilePanel.getCheckCurrent().doClick();
		} else {
			selectFilePanel.getCheckOtherFiles().doClick();
		}
	}

	
	@Override
	public List<String> getOtherFilesToCheck() {
		return selectFilePanel.getTableUrls();
	}
	@Override
	public void setOtherFilesToCheck(List<String> resources) {
		selectFilePanel.addRowsInTable(resources);
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
		return profilingPanel.getProfilingCondCBox().isSelected();
	}
	@Override
	public void setUseProfiligConditions(boolean state) {
		profilingPanel.getProfilingCondCBox().setSelected(!state);
		profilingPanel.getProfilingCondCBox().doClick();
	}

	@Override
	public boolean isUseManuallyConfiguredConditionsSet() {
		return profilingPanel.getConfigProfilingRBtn().isSelected();
	}
	@Override
	public void setUseManuallyConfiguredConditionsSet(boolean useManuallyConfiguredConditionsSet) {
		if(useManuallyConfiguredConditionsSet){
			profilingPanel.getConfigProfilingRBtn().doClick();
		} else {
			profilingPanel.getCheckAllProfilingRBtn().doClick();
		}
	}
	
	@Override
	public LinkedHashMap<String, LinkedHashSet<String>> getDefinedConditions() {
		return profilingPanel.getConditionsFromTable();
	}

	@Override
	public void setDefinedConditions(LinkedHashMap<String, String> conditions) {
		profilingPanel.addInTable(conditions);
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
