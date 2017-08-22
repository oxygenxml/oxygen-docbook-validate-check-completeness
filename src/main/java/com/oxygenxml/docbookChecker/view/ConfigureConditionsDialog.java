package com.oxygenxml.docbookChecker.view;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.TreePath;

import com.oxygenxml.docbookChecker.checkbox.tree.ConditionValue;
import com.oxygenxml.docbookChecker.checkbox.tree.JCheckBoxTree;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.translator.Tags;
import com.oxygenxml.docbookChecker.translator.Translator;
import com.oxygenxml.profiling.ProfileConditionsFromDocsWorker;
import com.oxygenxml.profiling.ProfileConditionsFromDocsWorkerReporter;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

import ro.sync.exml.workspace.api.standalone.ui.OKCancelDialog;
/**
 * Add conditions dialog creator
 * @author intern4
 */
public class ConfigureConditionsDialog extends OKCancelDialog implements ProfileConditionsFromDocsWorkerReporter{
	
	/**
	 * Profiling panel Creator
	 */
	private ProfilingPanelCreator profilingPanel;
	
	/**
	 * Object used for get the oxygen profile conditions.
	 */
	private ProfilingConditionsInformations profilingConditionsInformations = new ProfilingConditionsInformationsImpl();
	
	/**
	 * Translator used for internationalization.
	 */
	private Translator translator;
	
	private JFrame parentComponent;

	private ProblemReporter problemReporter;

	private List<String> urls;

	//CheckBox Tree for select conditions
	private JCheckBoxTree cbTree = new JCheckBoxTree();

//button for get conditions used in documents
		JButton getConditionsBtn = new JButton();
	
	//the panel that will be displayed
			final JPanel panel = new JPanel();	
	
	//warning 		
	public JLabel conditionsWarning = new JLabel();
	
	/**
	 * Constructor
	 * @param profilingPanel 
	 * @param translator
	 * @param parentComponent
	 */
	public ConfigureConditionsDialog(ProblemReporter problemReporter, List<String> urls,  ProfilingPanelCreator profilingPanel,
				Translator translator,  JFrame parentComponent , boolean expandNodes) {
		super(parentComponent, translator.getTranslation(Tags.CONFIGURE_CONDITIONS_DIALOG_TITLE) , true);
		this.problemReporter = problemReporter;
		this.urls = urls;
		this.profilingPanel = profilingPanel;
		this.translator = translator;
		this.parentComponent = parentComponent;
		
		display(expandNodes);
	}
	
	/**
	 * Display the dialog.
	 * @param expandNodes	<code>true</code> if nodes will be expanded
	 */
	private void display(boolean expandNodes) {
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		
		//comboBox for select documentType
		String[] combBoxItems = {ProfilingConditionsInformations.DOCBOOK , ProfilingConditionsInformations.DOCBOOK4 , ProfilingConditionsInformations.DOCBOOK5};		
		final JComboBox<String> combBoxDocumentTypes = new JComboBox<String>(combBoxItems);
		

		//add document type JLabel
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JLabel documentTypeLb = new JLabel(translator.getTranslation(Tags.SELECT_DOCUMENT_TYPE));
		panel.add(documentTypeLb, gbc);
		
		//add comboBox
		gbc.gridx++;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		combBoxDocumentTypes.setOpaque(false);
		panel.add(combBoxDocumentTypes, gbc);
		
		//add the a scrollPane with the tree
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(new JScrollPane(cbTree), gbc);

		//add button for get used conditions
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 5, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		getConditionsBtn.setEnabled(true);
		getConditionsBtn.setText("Filter conditions");
		getConditionsBtn.setToolTipText("Click this button to display only the conditions used in documents.");
		panel.add(getConditionsBtn, gbc);
		
		gbc.gridy++;
		conditionsWarning.setText("<html><font color=\"orange\">*</font>"+
				translator.getTranslation(Tags.WARNING_MESSAGE_UNDEFINED_CONDITIONS)+"</html>");
		conditionsWarning.setVisible(false);
		panel.add(conditionsWarning, gbc);
		
		//add action listener on getConditionsBtn
		getConditionsBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//disable the comboBox used to select the document type
				combBoxDocumentTypes.setEnabled(false);
				
				//start the worker that get the conditions used in documents
				ProfileConditionsFromDocsWorker	worker = 
						new ProfileConditionsFromDocsWorker(urls, ConfigureConditionsDialog.this, problemReporter);
				worker.execute();
				
				//set cursor in wait 
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				//disable ok button and get conditions button
				 getConditionsBtn.setEnabled(false);
				 getOkButton().setEnabled(false);
			}
		});
		
		
		cbTree.setModel(profilingConditionsInformations.getProfileConditions(ProfilingConditionsInformations.DOCBOOK));
		boolean setWarning = cbTree.checkPathsInTreeAndVerify(profilingPanel.getConditionsFromTable(), ProfilingConditionsInformations.DOCBOOK);
		conditionsWarning.setVisible(setWarning);
		
		combBoxDocumentTypes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 String docType = (String) combBoxDocumentTypes.getSelectedItem();
				 cbTree.setModel(profilingConditionsInformations.getProfileConditions(docType));
				 boolean setWarning = cbTree.checkPathsInTreeAndVerify(profilingPanel.getConditionsFromTable(), docType);
				 conditionsWarning.setVisible(setWarning);
				 cbTree.setShowsRootHandles(true);
				 cbTree.setRootVisible(true);
				 cbTree.setRootVisible(false);
			}
		});
		
		cbTree.setShowsRootHandles(true);
		cbTree.setRootVisible(false);
		
		if (expandNodes) {
			//expands nodes
			cbTree.expandAllNodes();
		}

		this.add(panel);
		this.setSize(250, 400);
		this.setLocationRelativeTo(parentComponent);
		this.setResizable(true);
		this.setVisible(true);
	}
	
	
	
	@Override
	protected void doOK() {
		//map to add in conditions table
		LinkedHashMap<String, LinkedHashSet<String>> toAdd = new LinkedHashMap<String, LinkedHashSet<String>>();
		
		//get the paths from checkBox tree
		Set<TreePath> paths = cbTree.getCheckedLeafPaths();
		
		//iterate over paths
		for (TreePath tp : paths) {
				String key = tp.getPath()[1].toString();
				String value = tp.getPath()[2].toString();
				
				if (toAdd.containsKey(key)) {
					toAdd.get(key).add(value);
				}
				else{
					LinkedHashSet<String> setValue = new LinkedHashSet<String>();
					setValue.add(value);
					toAdd.put(key, setValue);
				}

		}
		//clear conditions table
		profilingPanel.clearTable();
		
		if(!toAdd.isEmpty()){
			//add components in the conditionalTable
			profilingPanel.addInTable(toAdd);
			
			//change add button text in "edit"
			profilingPanel.getAddBtn().setText(translator.getTranslation(Tags.EDIT_TABLE));
		}
		else{
			//change add button text in "Add"
			profilingPanel.getAddBtn().setText(translator.getTranslation(Tags.ADD_TABLE));
		}
		super.doOK();
	}

	
	
	@Override
	public void reportProfileConditionsFromDocsWorkerFinish(LinkedHashMap<String, LinkedHashSet<String>> result) {
		
		//set cursor in default
		panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
		//set OK button enable
		getOkButton().setEnabled(true);	
		
		boolean setWarning = cbTree.setModelAndValidateConditions(result);

		conditionsWarning.setVisible(setWarning);
		System.out.println("setWarning: " + setWarning);
		cbTree.expandAllNodes();		
	}

}
