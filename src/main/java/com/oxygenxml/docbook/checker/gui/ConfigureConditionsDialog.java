package com.oxygenxml.docbook.checker.gui;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
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
 * Configure conditions dialog.
 * @author intern4
 */
public class ConfigureConditionsDialog extends OKCancelDialog implements ProfileConditionsFromDocsWorkerReporter{
	
	/**
	 * The width of dialog
	 */
	private static final int DIALOG_WIDTH = 250;
	
	/**
	 * The height of dialog.
	 */
	private static final int DIALOG_HEIGHT = 400;
	
	private static final int INSET_5 = 5;
	
	private static final int INSET_10 = 10;
	/**
	 * Profiling panel 
	 */
	private ProfilingPanel profilingPanel;
	
	/**
	 * Translator used for internationalization.
	 */
	private Translator translator;

	/**
	 * The parent component.
	 */
	private JFrame parentComponent;

	/**
	 * Problem reporter
	 */
	private ProblemReporter problemReporter;

	/**
	 * List with urlsToCheck.
	 */
	private List<URL> urlsToCheck;

	/**
	 *  CheckBox Tree
	 */
	private CheckBoxTree cbTree;

	/**
	 *  button for get conditions used in documents
	 */
	JButton getConditionsBtn;

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
	private ProfilingConditionsInformations conditionsInformations;

	
	/**
	 * Constructor
	 * @param problemReporter Problem reporter
	 * @param urls 	List with URLs in String format.
	 * @param profilingPanel 	Profiling panel.
	 * @param translator Translator
	 * @param parentComponent parent frame
	 * @param conditionsInformations Conditions information
	 */
	public ConfigureConditionsDialog(ProblemReporter problemReporter, List<URL> urls,  ProfilingPanel profilingPanel,
				Translator translator,  JFrame parentComponent , ProfilingConditionsInformations conditionsInformations) {
		super(parentComponent, translator.getTranslation(Tags.CONFIGURE_CONDITIONS_DIALOG_TITLE) , true);
		this.problemReporter = problemReporter;
		this.urlsToCheck = urls;
		this.profilingPanel = profilingPanel;
		this.translator = translator;
		this.parentComponent = parentComponent;
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
		
		//add the a scrollPane with the tree
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		configuteConditionPanel.add(new JScrollPane(cbTree), gbc);

		//add button for get used conditions
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 5, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		getConditionsBtn.setEnabled(true);
		configuteConditionPanel.add(getConditionsBtn, gbc);
		
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
		this.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		this.setLocationRelativeTo(parentComponent);
		this.setResizable(true);
		this.setVisible(true);
	}
	
	
	/**
	 * OK button pressed.
	 */
	@Override
	protected void doOK() {
		//map to add in conditions table
		LinkedHashMap<String, LinkedHashSet<String>> toAdd = cbTree.getCheckedLeafPaths();
	
		//clear conditions table
		profilingPanel.clearTable();
		
		if(!toAdd.isEmpty()){
			//add components in the conditionalTable
			profilingPanel.addInTable(toAdd);
			
			//change add button text in "edit"
			profilingPanel.getAddBtn().setText(translator.getTranslation(Tags.EDIT_TABLE));
		}else{
			//change add button text in "Add"
			profilingPanel.getAddBtn().setText(translator.getTranslation(Tags.ADD_TABLE));
		}
		super.doOK();
	}

	
	/**
	 * Create a panel that contains the checkBox for select to use all available
	 * conditions sets and a button for access the preferences.
	 * 
	 * @return The panel.
	 */
	private JPanel createWarningPanel() {
		JPanel toReturn = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		JLabel conditionsWarning = new JLabel();
		
		conditionsWarning.setText("<html><font color=\"orange\">*</font>"+
				translator.getTranslation(Tags.WARNING_MESSAGE_UNDEFINED_CONDITIONS)+"</html>");
		
		//add the checkBox
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		toReturn.add(conditionsWarning, gbc);


		/**
		 * Action for show profiling preferences.
		 */
		 AbstractAction showProfilingPageAction = new AbstractAction("Profiling Preferences") {

			@Override
			public void actionPerformed(ActionEvent e) {
				PluginWorkspaceProvider.getPluginWorkspace().showPreferencesPages(new String[] { "profiling.conditions" },
						"profiling.conditions", true);
				
				//refresh the content of dialog
				if(!getConditionsBtn.isEnabled()){
					//press "learn conditions" button
					getConditionsBtn.setEnabled(true);
					getConditionsBtn.doClick();
				}else{
					//the learn conditions button wasn't pressed.
					//set model with new conditions
					cbTree.setModel(conditionsInformations.getProfileConditions(profilingPanel.getSelectedDocumentType()));
					
					//checkPath in tree and verify
					boolean setWarning = cbTree.checkPathsInTreeAndVerify(profilingPanel.getConditionsFromTable());
					//shows warning panel 
					conditionsWarningPanel.setVisible(setWarning);
				}
				
			}
		};

		/**
		 * Button for display profiling preferences
		 */
		 ToolbarButton buttonToProfiling = new ToolbarButton(showProfilingPageAction, true);
		
		
		URL imageToLoad = getClass().getClassLoader().getResource(Images.PREFERENCES_ICON);
		if (imageToLoad != null) {
			buttonToProfiling.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
			buttonToProfiling.setText("");
		} else {
			buttonToProfiling.setText("PC");
		}

		//add the button
		gbc.gridx = 1;
		gbc.weightx = 1;		
		gbc.anchor = GridBagConstraints.WEST;
		toReturn.add(buttonToProfiling, gbc);

		return toReturn;
	}

	
	
	/**
	 * Add the given map with result in CheckBox tree.
	 * @param result A LinkedHashMap with results.
	 */
	@Override
	public void reportProfileConditionsFromDocsWorkerFinish(LinkedHashMap<String, LinkedHashSet<String>> result) {
		
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
