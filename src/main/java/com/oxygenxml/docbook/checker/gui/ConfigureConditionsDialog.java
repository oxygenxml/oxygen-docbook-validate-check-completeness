package com.oxygenxml.docbook.checker.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.oxygenxml.docbook.checker.checkboxtree.CheckBoxTree;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.resources.Images;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;
import com.oxygenxml.profiling.ProfileConditionsFromDocsWorker;
import com.oxygenxml.profiling.ProfileConditionsFromDocsWorkerReporter;
import com.oxygenxml.profiling.ProfilingConditionsInformations;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.standalone.ui.OKCancelDialog;
import ro.sync.exml.workspace.api.standalone.ui.ToolbarButton;
/**
 * Dialog for configure profiling condition for local conditions set.
 * @author Cosmin Duna
 */
public class ConfigureConditionsDialog extends OKCancelDialog implements ProfileConditionsFromDocsWorkerReporter{
	
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * The key for access profiling conditions from oxygen preferences.
	 */
	private static final String KEY_PROFILING_CONDITIONS = "profiling.conditions";
	
	/**
	 * Profiling panel 
	 */
	private ProfilingPanel profilingPanel;
	
	/**
	 * Translator used for internationalization.
	 */
	private transient Translator translator;


	/**
	 * Problem reporter
	 */
	private transient ProblemReporter problemReporter;

	/**
	 * List with urlsToCheck.
	 */
	private List<URL> urlsToCheck;

	/**
	 *  CheckBox Tree
	 */
	private CheckBoxTree cbTree;

	/**
	 *  Button for get conditions used in documents("learn conditions").
	 */
	private final JButton getConditionsBtn;

	/**
	 * the panel that will be displayed
	 */
	private JPanel configuteConditionPanel;

	/**
	 * Warning panel.
	 */
	private JPanel conditionsWarningPanel;

	/**
	 * Profiling conditions informations
	 */
	private transient ProfilingConditionsInformations conditionsInformations;

	
	/**
	 * Constructor
	 * @param problemReporter Problem reporter
	 * @param urls 	List with URLs in String format.
	 * @param profilingPanel 	Profiling panel.
	 * @param translator Translator
	 * @param conditionsInformations Conditions information
	 */
	public ConfigureConditionsDialog(ProblemReporter problemReporter, List<URL> urls,  ProfilingPanel profilingPanel,
				Translator translator, ProfilingConditionsInformations conditionsInformations) {
		super(null, translator.getTranslation(Tags.CONFIGURE_CONDITIONS_DIALOG_TITLE) , true);
		this.problemReporter = problemReporter;
		this.urlsToCheck = urls;
		this.profilingPanel = profilingPanel;
		this.translator = translator;
		this.conditionsInformations = conditionsInformations;
		conditionsWarningPanel = createWarningPanel();
		
		cbTree = new CheckBoxTree();
		
		getConditionsBtn = new JButton(translator.getTranslation(Tags.GET_DOCUMENT_CONDITIONS_BUTTON));
		getConditionsBtn.setToolTipText(translator.getTranslation(Tags.GET_DOCUMENT_CONDITIONS_TOOLTIP));
		
		initDialog(conditionsInformations.getProfileConditions(profilingPanel.getSelectedDocumentType()));
	}
	
	/**
	 * Initialize the dialog.
	 * @param definedConditions Map with definedConditions.
	 */
	private void initDialog( LinkedHashMap<String, LinkedHashSet<String>> definedConditions) {
		
		configuteConditionPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JScrollPane scrollPane = new JScrollPane(cbTree);
		scrollPane.setPreferredSize(new Dimension(230, 270));
		
		//add the a scrollPane with the tree
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		configuteConditionPanel.add(scrollPane, gbc);

		//add button for get used conditions
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets.top =  InsetValues.COMPONENT_TOP_INSET;
		getConditionsBtn.setEnabled(true);
		configuteConditionPanel.add(getConditionsBtn, gbc);
		
		//add warning panel
		gbc.gridy++;
		conditionsWarningPanel.setVisible(false);
		configuteConditionPanel.add(conditionsWarningPanel, gbc);
	
		//add action listener on getConditionsBtn
		getConditionsBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//start the worker that get the conditions used in documents
				ProfileConditionsFromDocsWorker	worker = 
						new ProfileConditionsFromDocsWorker(urlsToCheck, ConfigureConditionsDialog.this, problemReporter, profilingPanel.getSelectedDocumentType());
				worker.execute();
				
				//set cursor in wait 
				configuteConditionPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				//disable ok button and get conditions button
				 getConditionsBtn.setEnabled(false);
				 getOkButton().setEnabled(false);
			}
		});
		
		//set initial model.
		cbTree.setModel(definedConditions);
		
		//checks the conditions from table  
		boolean setWarning = cbTree.checkPathsInTreeAndVerify(profilingPanel.getConditionsFromTable());
		
		//shows warning panel if conditions that was check in tree are undefined
		conditionsWarningPanel.setVisible(setWarning);
		
		cbTree.setShowsRootHandles(true);
		cbTree.setRootVisible(false);
		

		this.add(configuteConditionPanel);
		pack();
		this.setMinimumSize(new Dimension(getSize().width + 20, getSize().height));
		this.setLocationRelativeTo(profilingPanel.getParent());
		this.setResizable(true);
		this.setVisible(true);
	}
	
	
	/**
	 * OK button pressed.
	 */
	@Override
	protected void doOK() {
		// map to add in conditions table
		LinkedHashMap<String, LinkedHashSet<String>> toAdd = cbTree.getCheckedLeafPaths();

		// clear conditions table
		profilingPanel.clearTable();

		// add components in the conditionalTable
		profilingPanel.addInTable(toAdd);

		super.doOK();
	}

	
	/**
	 * Create a panel that contains a label with a warning message and a button for access the preferences.
	 * 
	 * @return The panel.
	 */
	private JPanel createWarningPanel() {
		JPanel toReturn = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		//
		// Create a label that contains the warning message
		//
		JLabel conditionsWarning = new JLabel();
		conditionsWarning.setText("<html><font color=\"orange\">*</font> "
				+ translator.getTranslation(Tags.WARNING_MESSAGE_UNDEFINED_CONDITIONS) + "</html>");

		//
		// Create a abstract action for show profiling preferences.
		//
		AbstractAction showProfilingPageAction = new AbstractAction("Profiling Preferences") {

			@Override
			public void actionPerformed(ActionEvent e) {
				PluginWorkspaceProvider.getPluginWorkspace().showPreferencesPages(new String[] { KEY_PROFILING_CONDITIONS },
						KEY_PROFILING_CONDITIONS, true);

				// refresh the content of dialog
				if (!getConditionsBtn.isEnabled()) {
					// press "learn conditions" button
					getConditionsBtn.setEnabled(true);
					getConditionsBtn.doClick();
				} else {
					// the learn conditions button wasn't pressed.
					// set model with new conditions
					cbTree.setModel(conditionsInformations.getProfileConditions(profilingPanel.getSelectedDocumentType()));

					// checkPath in tree and verify
					boolean setWarning = cbTree.checkPathsInTreeAndVerify(profilingPanel.getConditionsFromTable());
					// shows warning panel
					conditionsWarningPanel.setVisible(setWarning);
				}

			}
		};

		//
		// create button that contains action to show profiling preferences.
		//
		ToolbarButton buttonToProfiling = new ToolbarButton(showProfilingPageAction, true);
		// Get the icon for button
		URL imageToLoad = getClass().getClassLoader().getResource(Images.PREFERENCES_ICON);
		if (imageToLoad != null) {
			buttonToProfiling.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
			buttonToProfiling.setText("");
		} else {
			buttonToProfiling.setText("PC");
		}

		// add the label with warning
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		toReturn.add(conditionsWarning, gbc);

		// add the button that
		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		toReturn.add(buttonToProfiling, gbc);

		return toReturn;
	}
	
	/**
	 * Add the given map with conditions in CheckBox tree.
	 * @param result A LinkedHashMap with conditions.
	 */
	@Override
	public void reportProfileConditionsFromDocsWorkerFinish(Map<String, LinkedHashSet<String>> result) {
		
		//set cursor in default
		configuteConditionPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
		if(result == null){  
			this.doCancel();
		}else if(!result.isEmpty()){
			//document contains profiling conditions
			//set OK button enable
			getOkButton().setEnabled(true);	
			
			conditionsWarningPanel.setVisible(cbTree.setModelAndValidateConditions(result,
					conditionsInformations.getProfileConditions(profilingPanel.getSelectedDocumentType()))) ;
			cbTree.expandAllNodes();
		}else{
			//results list in empty(documents doesn't contains profiling conditions)
			profilingPanel.setSelectedUseProfilingCBox(true);
			profilingPanel.doClickOnUseProfilingCBox();
			this.doCancel();
			PluginWorkspaceProvider.getPluginWorkspace().showInformationMessage(translator.getTranslation(Tags.NOT_FOUND_CONDITIONS));
		
		}
	}


}
