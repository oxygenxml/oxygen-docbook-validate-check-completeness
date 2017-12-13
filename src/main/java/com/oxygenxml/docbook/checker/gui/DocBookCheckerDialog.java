package com.oxygenxml.docbook.checker.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
 * @author Cosmin Duna
 *
 */
public class DocBookCheckerDialog extends OKCancelDialog implements CheckerInteractor {

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
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
	 * Check box to select to generate resource hierarchy report
	 */
	private JCheckBox generateHierarchyReportCbox;
	
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
	private transient Translator translator;

	/**
	 * Progress monitor.
	 */
	private ProgressDialog progressDialog;

	/**
	 * Validation worker.
	 */
	private transient ValidationWorker validationWorker;
	
	/**
	 * Problem reporter.
	 */
	private final transient ProblemReporter problemReporter;
	
	/**
	 * Content persister
	 */
	private final transient ContentPersister contentPersister = new ContentPersisterImpl();
	/**
	 *The current open URL.
	 */
	private URL currentOpenUrl ;
	/**
	 * Application interactor.
	 */
	private transient ApplicationInteractor applicationInteractor;
	

/**
 * Constructor.
 * 
 * @param sourceDescription Describe the place of the action that open the checker dialog.
 * @param applicationInteractor	Application interactor with the editor.
 * @param translator Translator.
 */
	public DocBookCheckerDialog(ApplicationSourceDescription sourceDescription, ApplicationInteractor applicationInteractor, 
			Translator translator) {
		super(applicationInteractor.getApplicationFrame(), "", true);
		
		this.currentOpenUrl = sourceDescription.getCurrentUrl();
		this.applicationInteractor = applicationInteractor;
		this.translator = translator;
		this.problemReporter = new OxygenProblemReporter(translator);
		
		checkExternalLinksCBox = new JCheckBox(translator.getTranslation(Tags.CHECK_EXTERNAL_KEY));
		checkImagesCBox = new JCheckBox(translator.getTranslation(Tags.CHECK_IMAGES_KEY));
		checkInternalLinksCbox = new JCheckBox(translator.getTranslation(Tags.CHECK_INTERNAL_KEY));
		generateHierarchyReportCbox = new JCheckBox(translator.getTranslation(Tags.GENERATE_HIERACHY_REPORT));
		
		
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
		updateView(sourceDescription);
		
		//set the text of OK button
		setOkButtonText(translator.getTranslation(Tags.CHECK_BUTTON));
		
		//set the dialog title
		setTitle(translator.getTranslation(Tags.FRAME_TITLE));
		
		setResizable(true);
		pack();
		setMinimumSize(getSize());
		setLocationRelativeTo(applicationInteractor.getApplicationFrame());
		setVisible(true);
	}
	
	
	/**
	 * Check button pressed.
	 */
	@Override
	protected void doOK() {

		//List with Urls to be validate
		List<URL> listUrls;

		//get a list with URLs to be verified
		if (isCheckCurrentResource()) {
			listUrls = new ArrayList<URL>();
			//add current open file url in list
			listUrls.add(currentOpenUrl);
		}else {
				listUrls = selectFilePanel.getFilesFromTable();
		}
		
		//check the table with manually defined conditions is not empty
		if (!(isUsingProfile() && isUseManuallyConfiguredConditionsSet() && getDefinedConditions().isEmpty()) ) {

			// create the progress dialog
			progressDialog = new ProgressDialog(applicationInteractor.getApplicationFrame() , translator);

			validationWorker = new ValidationWorker(listUrls, applicationInteractor , this, problemReporter, progressDialog);

			//add action listener on cancel button from the progress dialog
			progressDialog.addCancelActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					validationWorker.cancel(true);
					progressDialog.dispose();
				}
			});

			//set the progress dialog visible.
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					progressDialog.setVisible(true);
				}
			});

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
		gbc.gridy = 0;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(selectFilePanel, gbc);

		//add the panel for profiling
		gbc.gridy++;
		gbc.insets = new Insets(20, 0, 0, 0);
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
		mainPanel.add(checkImagesCBox, gbc);
		
		//add checkImage checkBox
		gbc.gridy++;
		gbc.insets = new Insets(5, 0, 10, 0);
		mainPanel.add(generateHierarchyReportCbox, gbc);
		
		add(mainPanel);
	}

	
	/**
	 * Update the view according to given oxygen SourceDescription.
	 * 
	 * @param sourceDescription source description of application.
	 */
	private void updateView(ApplicationSourceDescription sourceDescription) {
		 if(sourceDescription.getCurrentUrl() == null){
			 	selectFilePanel.setEnableCheckCurrent(false);
				setCheckCurrentResource(false);
		 }
		 
		if(Source.CONTEXTUAL.equals(sourceDescription.getSource())){
			setCheckCurrentResource(true);
		}else if(Source.PROJECT_MANAGER.equals(sourceDescription.getSource())){
			setCheckCurrentResource(false);
			
			//clear table with files
			selectFilePanel.clearTable();
			
			//add selected files from project in table
			List<URL> selectedFilesInProject = sourceDescription.getSelectedFilesInProject();
			if(!selectedFilesInProject.isEmpty()){
				setOtherFilesToCheck(selectedFilesInProject);
				getOkButton().setEnabled(true);
			} else {
				//set check button disable
				getOkButton().setEnabled(false);
			}
		}
		
	}
	

	//----Implementation of CheckerInteractor
	/**
	 * Check if it's select to check the current file.
	 */
	@Override
	public boolean isCheckCurrentResource() {
		return selectFilePanel.isSelectedCheckCurrent();
	}

	/**
	 * Set selected the checkCurrent RadioButton or the checkOther RadioButton .
	 * @param checkCurrentResource <code>true</code> to select checkCurrent, <code>false</code> to select checkOther.
	 */
	@Override
	public void setCheckCurrentResource(boolean checkCurrentResource) {
		if(checkCurrentResource){
			selectFilePanel.doClickOnCheckCurrent();
		} else {
			selectFilePanel.doClickOnCheckOtherFiles();
		}
	}


	/**
	 * Get other files to check from files table.
	 * @return List with files to be check.
	 */
	@Override
	public List<URL> getOtherFilesToCheck() {
		return selectFilePanel.getFilesFromTable();
	}
	
	/**
	 * Set other files to check in files table.
	 * @param resources List with files.
	 */
	@Override
	public void setOtherFilesToCheck(List<URL> resources) {
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
	 *	It's selected the generate hierarchy report  checkBox.
	 * @return <code>true</code> selected, <code>false</code> deselected.
	 */
	@Override
	public boolean isGenerateHierarchyReport() {
		return generateHierarchyReportCbox.isSelected();
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
	 *	Set selected the generate hierarchy report  checkBox.
	 * @param state <code>true</code> selected, <code>false</code> deselected.
	 */
	@Override
	public void setGenerateHierarchyReport(boolean state) {
		generateHierarchyReportCbox.setSelected(state);
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
	 * @param documentTypes List with document types.
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
		return profilingPanel.isSelectedUseProfilingCBox();
	}
	
	/**
	 *	Set selected the useProfilig checkBox.
	 * @param state <code>true</code> selected, <code>false</code> deselected.
	 */
	@Override
	public void setUseProfiligConditions(boolean state) {
		profilingPanel.setSelectedUseProfilingCBox(!state);
		profilingPanel.doClickOnUseProfilingCBox();
	}

	
	/**
	 * It's selected the reporteUndefinedConditions checkBox.
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	@Override
	public boolean isReporteUndefinedConditions() {
		return profilingPanel.isSelectedReportedUndefinedConditionsCBox();
	}
	
	/**
	 *	Set selected the reporteUndefinedConditions checkBox.
	 * @param state <code>true</code> selected, <code>false</code> unselected.
	 */
	@Override
	public void setReporteUndefinedConditions(boolean state) {
		profilingPanel.setSelectedReportUndefinedConditionsCBox(state);
	}

	/**
	 * It's selected the useManuallyConfiguredCondition radioButton.
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	@Override
	public boolean isUseManuallyConfiguredConditionsSet() {
		return profilingPanel.isSelectedConfigProfilingRBtn();
	}
	
	/**
	 * Set selected the useManuallyConfiguredCondition RadioButton or the useAllConditionSets RadioButton .
	 * @param useManuallyConfiguredConditionsSet <code>true</code> to select useManuallyConfiguredCondition, 
	 * <code>false</code> to select useAllConditionSets.
	 */
	@Override
	public void setUseManuallyConfiguredConditionsSet(boolean useManuallyConfiguredConditionsSet) {
		if(useManuallyConfiguredConditionsSet){
			profilingPanel.doClickOnConfigProfilingRBtn();
		} else {
			profilingPanel.doClickOnUseAllCondSetsRBtn();
		}
	}
	
	/**
	 * Get the defined conditions in condition table.
	 * @return A LinkedHashMap with conditions.
	 */
	@Override
	public Map<String, LinkedHashSet<String>> getDefinedConditions() {
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

	
	@Override
	public String getHelpPageID() {
		return LINK_TO_GIT_HUB;
	}


}
