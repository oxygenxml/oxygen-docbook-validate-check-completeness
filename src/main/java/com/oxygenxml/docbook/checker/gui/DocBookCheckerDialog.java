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

import com.icl.saxon.functions.Document;
import com.oxygenxml.docbook.checker.ApplicationInteractor;
import com.oxygenxml.docbook.checker.ApplicationSourceDescription;
import com.oxygenxml.docbook.checker.ApplicationSourceDescription.Source;
import com.oxygenxml.docbook.checker.CheckerInteractor;
import com.oxygenxml.docbook.checker.ValidationWorker;
import com.oxygenxml.docbook.checker.persister.ContentPersister;
import com.oxygenxml.docbook.checker.persister.ContentPersisterImpl;
import com.oxygenxml.docbook.checker.reporters.OxygenProblemReporter;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

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
	private JCheckBox checkExternalLinksCBox;
	/**
	 * Check box to select to check images
	 */
	private JCheckBox checkImagesCBox;
	/**
	 * Check box to select to check internal links
	 */
	private JCheckBox checkInternalLinksCbox;
	
	/**
	 * Panel for select files to check
	 */
	private SelectFilesPanel selectFilePanel;

	/**
	 * Creator for profiling panel.
	 */
	private ProfilingPanel profilingPanel;

	/**
	 * Link to GitHub repository description.
	 */
	private static final String LINK_TO_GIT_HUB = "https://github.com/oxygenxml/docbook-validate-check-completeness";
	
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
	private ProblemReporter problemReporter = new OxygenProblemReporter();
	
	/**
	 * Content persister
	 */
	private ContentPersister contentPersister = new ContentPersisterImpl();
	/**
	 *The current open URL.
	 */
	private String currentOpenUrl ;
	/**
	 * Application interactor.
	 */
	private ApplicationInteractor applicationInteractor;
	

/**
 * Constructor.
 * 
 * @param sourceDescription Application source description.
 * @param applicationInteractor	Application interactor.
 * @param translator Translator.
 */
	public DocBookCheckerDialog(ApplicationSourceDescription sourceDescription, ApplicationInteractor applicationInteractor, 
			Translator translator) {
		super(applicationInteractor.getApplicationFrame(), "", true);
		
		this.currentOpenUrl = sourceDescription.getCurrentUrl();
		this.applicationInteractor = applicationInteractor;
		this.translator = translator;
		
		checkExternalLinksCBox = new JCheckBox(translator.getTranslation(Tags.CHECK_EXTERNAL_KEY));
		checkImagesCBox = new JCheckBox(translator.getTranslation(Tags.CHECK_IMAGES_KEY));
		checkInternalLinksCbox = new JCheckBox(translator.getTranslation(Tags.CHECK_INTERNAL_KEY));
		
		selectFilePanel = new SelectFilesPanel(translator, this.getOkButton());

		profilingPanel = new ProfilingPanel(selectFilePanel, sourceDescription, applicationInteractor, problemReporter, translator);

		// Initialize GUI
		initGUI();

		// Load saved state of the dialog
		contentPersister.loadState(this);
		
		// set the text (with conditions sets) of allAvailableConditionsSets radioButton.
		ProfilingConditionsInformations profilingConditionsInformations = new ProfilingConditionsInformationsImpl();
		profilingPanel.changeConditionsSetsFromRadioButton(profilingConditionsInformations.getConditionSetsNames(getDocumentType()));
		
		//Update the view according to oxygen SourceDescription of action that open this dialog.
		updateViewAcordingSourceDescription(sourceDescription);
		
		//set the text of OK button
		setOkButtonText(translator.getTranslation(Tags.CHECK_BUTTON));
		
		//set the dialog title
		setTitle(translator.getTranslation(Tags.FRAME_TITLE));
		
		setResizable(true);
		setMinimumSize(new Dimension(350, 520));
		setSize(new Dimension(470, 600));
		setLocationRelativeTo(applicationInteractor.getApplicationFrame());
		setVisible(true);
	}
	
	
	/**
	 * Check button pressed.
	 */
	@Override
	protected void doOK() {

		//List with Urls to be validate
		List<String> listUrls = new ArrayList<String>();

		//get a list with URLs to be verified
		if (isCheckCurrentResource()) {
			//add current open file url in list
			listUrls.add(currentOpenUrl);
		} 
		else {
			//add rows from file table in list.
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

			validationWorker = new ValidationWorker(listUrls, applicationInteractor , this, problemReporter, this);
			validationWorker.addPropertyChangeListener(this);

			validationWorker.execute();
			contentPersister.saveState(this);
			applicationInteractor.setOperationInProgress(true);
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
		gbc.insets = new Insets(0, 0, 10, 0);
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
		mainPanel.add(checkExternalLinksCBox, gbc);

		//add checkInternal checkBox
		gbc.gridy++;
		mainPanel.add(checkInternalLinksCbox, gbc);

		//add checkImage checkBox
		gbc.gridy++;
		gbc.insets = new Insets(5, 0, 10, 0);
		mainPanel.add(checkImagesCBox, gbc);
		
		
		getContentPane().add(mainPanel);
	}

	
	/**
	 * Update the view according to oxygen SourceDescription.
	 */
	private void updateViewAcordingSourceDescription(ApplicationSourceDescription sourceDescription) {
		 if(sourceDescription.getCurrentUrl() == null){
			 	selectFilePanel.getCheckCurrent().setEnabled(false);
				setCheckCurrentResource(false);
		 }
		 
		if(Source.CONTEXTUAL.equals(sourceDescription.getSource())){
			setCheckCurrentResource(true);
		}
		else if(Source.PROJECT_MANAGER.equals(sourceDescription.getSource())){
			setCheckCurrentResource(false);
			
			//clear table with files
			selectFilePanel.clearTable();
			setOtherFilesToCheck(sourceDescription.getSelectedFilesInProject());
			
		}
		
	}
	

	//----Implementation of CheckerInteractor
	/**
	 * Check if it's select to check the current file.
	 */
	@Override
	public boolean isCheckCurrentResource() {
		return selectFilePanel.getCheckCurrent().isSelected();
	}

	/**
	 * Set selected the checkCurrent RadioButton or the checkOther RadioButton .
	 * @param checkCurrentResource <code>true</code> to select checkCurrent, <code>false</code> to select checkOther.
	 */
	@Override
	public void setCheckCurrentResource(boolean checkCurrentResource) {
		if(checkCurrentResource){
			selectFilePanel.getCheckCurrent().doClick();
		} else {
			selectFilePanel.getCheckOtherFiles().doClick();
		}
	}


	/**
	 * Get other files to check from files table.
	 * @return List with files to be check.
	 */
	@Override
	public List<String> getOtherFilesToCheck() {
		return selectFilePanel.getTableUrls();
	}
	
	/**
	 * Set other files to check in files table.
	 * @param resources List with files.
	 */
	@Override
	public void setOtherFilesToCheck(List<String> resources) {
		selectFilePanel.addRowsInTable(resources);
	}


	/**
	 * It's selected the check external checkBox.
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	@Override
	public boolean isCheckExternal() {
		return checkExternalLinksCBox.isSelected();
	}

	/**
	 * It's selected the check images checkBox.
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	@Override
	public boolean isCheckImages() {
		return checkImagesCBox.isSelected();
	}

	/**
	 * It's selected the check internal checkBox.
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	@Override
	public boolean isCheckInternal() {
		return checkInternalLinksCbox.isSelected();
	}


	/**
	 *	Set selected the check external checkBox.
	 * @param state <code>true</code> selected, <code>false</code> deselected.
	 */
	@Override
	public void setCheckExternal(boolean state) {
		checkExternalLinksCBox.setSelected(state);
	}

	/**
	 *	Set selected the check images checkBox.
	 * @param state <code>true</code> selected, <code>false</code> deselected.
	 */
	@Override
	public void setCheckImages(boolean state) {
		checkImagesCBox.setSelected(state);
	}

	/**
	 *	Set selected the check internal checkBox.
	 * @param state <code>true</code> selected, <code>false</code> deselected.
	 */
	@Override
	public void setCheckInternal(boolean state) {
		checkInternalLinksCbox.setSelected(state);
	}

	/**
	 * Get the selected document type in comboBox.
	 * @return The selected document type.
	 */
	@Override
	public String getDocumentType() {
		return profilingPanel.getSelectedDocumentType();
	}

	/**
	 * Set selected in comboBox the given document type.
	 * @param documentType The document type.
	 */
	@Override
	public void setDocumentType(String documentType) {
		profilingPanel.setSelectedDocumentType(documentType);
	}

	/**
	 * Get all document types form comboBox
	 * @return List with document types.
	 */
	@Override
	public List<String> getAllDocumentTypes() {
		return profilingPanel.getAllDocumentTypes();
	}

	/**
	 * Set given document types in comboBox.
	 * @param List with document types.
	 */
	@Override
	public void setAllDocumentTypes(List<String> documentTypes) {
		profilingPanel.setAllDocumentTypes(documentTypes);
	}
	
	/**
	 * It's selected the useProfiling checkBox.
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	@Override
	public boolean isUsingProfile() {
		return profilingPanel.getProfilingCondCBox().isSelected();
	}
	
	/**
	 *	Set selected the useProfilig checkBox.
	 * @param state <code>true</code> selected, <code>false</code> deselected.
	 */
	@Override
	public void setUseProfiligConditions(boolean state) {
		profilingPanel.getProfilingCondCBox().setSelected(!state);
		profilingPanel.getProfilingCondCBox().doClick();
	}

	
	/**
	 * It's selected the reporteUndefinedConditions checkBox.
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	@Override
	public boolean isReporteUndefinedConditions() {
		return profilingPanel.isReportedUndefinedConditionsCBox();
	}
	
	/**
	 *	Set selected the reporteUndefinedConditions checkBox.
	 * @param state <code>true</code> selected, <code>false</code> deselected.
	 */
	@Override
	public void setReporteUndefinedConditions(boolean state) {
		profilingPanel.setReportUndefinedConditionsCBox(state);
	}

	/**
	 * It's selected the useManuallyConfiguredCondition radioButton.
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	@Override
	public boolean isUseManuallyConfiguredConditionsSet() {
		return profilingPanel.getConfigProfilingRBtn().isSelected();
	}
	
	/**
	 * Set selected the useManuallyConfiguredCondition RadioButton or the useAllConditionSets RadioButton .
	 * @param checkCurrentResource <code>true</code> to select useManuallyConfiguredCondition, 
	 * <code>false</code> to select useAllConditionSets.
	 */
	@Override
	public void setUseManuallyConfiguredConditionsSet(boolean useManuallyConfiguredConditionsSet) {
		if(useManuallyConfiguredConditionsSet){
			profilingPanel.getConfigProfilingRBtn().doClick();
		} else {
			profilingPanel.getCheckAllProfilingRBtn().doClick();
		}
	}
	
	/**
	 * Get the defined conditions in condition table.
	 * @return A LinkedHashMap with conditions.
	 */
	@Override
	public LinkedHashMap<String, LinkedHashSet<String>> getDefinedConditions() {
		return profilingPanel.getConditionsFromTable();
	}

	/**
	 * Set the given conditions in condition table.
	 * @param conditions A LinkedHashMap with conditions.
	 */
	@Override
	public void setDefinedConditions(LinkedHashMap<String, String> conditions) {
		profilingPanel.addInTable(conditions);
	}

	
//----------Implementation of PropertyChangeListener
	/**
	 * Method that is called a bound property(progress) is changed.
	 */
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
	/**
	 * Set the given note at progress monitor.
	 * @param note The note. 
	 */
	@Override
	public void reportNote(String note) {
		if (!progressMonitor.isCanceled()) {
		progressMonitor.setNote(note);
		}
	}
	
	/**
	 * Close the progressMonitor.
	 */
	@Override
	public void closeMonitor() {
		progressMonitor.close();

	}

	@Override
	public String getHelpPageID() {
		return LINK_TO_GIT_HUB;
	}




	

}
