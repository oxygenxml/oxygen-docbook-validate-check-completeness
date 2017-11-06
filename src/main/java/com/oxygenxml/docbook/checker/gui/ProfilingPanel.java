package com.oxygenxml.docbook.checker.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbook.checker.ApplicationInteractor;
import com.oxygenxml.docbook.checker.ApplicationSourceDescription;
import com.oxygenxml.docbook.checker.persister.ContentPersisterUtil;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.resources.Images;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.standalone.ui.ToolbarButton;

/**
 * Profiling panel creator.
 * 
 * @author intern4
 *
 */
public class ProfilingPanel extends JPanel {

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * CheckBox for select to use profiling conditions for check.
	 */
	private JCheckBox useProfilingCondCBox;

	/**
	 * RadioButton for select to configure a profiling condition set.
	 */
	private JRadioButton configProfilingCondSetRBtn;

	/**
	 * RadioButton for select all available conditions set.
	 */
	private JRadioButton useAllCondSetsRBtn ;

	/**
	 * Table with profiling conditions.
	 */
	private JTable table = new JTable(20, 2);

	/**
	 * Conditions model for table.
	 */
	private DefaultTableModel modelCondTable;

	/**
	 * ScrollPane for table.
	 */
	private JScrollPane scrollPane = new JScrollPane(table);

	/**
	 * Button for add profiling conditions in table
	 */
	private JButton addBtn;
	/**
	 * Button for remove selected profiling conditions from table
	 */
	private JButton remvBtn;

	/**
	 * Used for internationalization.
	 */
	private transient Translator translator;

	/**
	 * combo box with document types
	 */
	private JComboBox combBoxDocumentTypes;

	/**
	 * Button for add a document type in comboBox;
	 */
	private ToolbarButton addDocumentTypeButton;
	
	/**
	 * Button for remove a document type from comboBox;
	 */
	private ToolbarButton removeDocumentTypeButton;
	
	/**
	 * Check box to select that undefined conditions to be reported.
	 */
	private JCheckBox reportUndefinedConditionsCBox;

	/**
	 * Profiling conditions informations
	 */
	private ProfilingConditionsInformations profilingConditionsInformations = new ProfilingConditionsInformationsImpl();

	
	
/**
 * Constructor
 * @param selectFilePanel selectedFilePanel
 * @param sourceDescription ApplicationSourceDescription
 * @param oxygenInteractor Application interactor,
 * @param problemReporter Problem reporter
 * @param translator Translator
 */
	public ProfilingPanel(final SelectFilesPanel selectFilePanel, final ApplicationSourceDescription sourceDescription, 
			final ApplicationInteractor oxygenInteractor, final ProblemReporter problemReporter ,final Translator translator) {
		this.translator = translator;

		useProfilingCondCBox = new JCheckBox(translator.getTranslation(Tags.USE_PROFLING_CBOX));
		configProfilingCondSetRBtn = new JRadioButton(translator.getTranslation(Tags.CONFIG_CONDITIONS_SET));
		useAllCondSetsRBtn = new JRadioButton();
		
		 addBtn = new JButton(translator.getTranslation(Tags.ADD_TABLE));
		 remvBtn = new JButton(translator.getTranslation(Tags.REMOVE_TABLE));
		
		 reportUndefinedConditionsCBox = new JCheckBox(translator.getTranslation(Tags.REPORT_UNDEFINED_CONDITIONS));
		 
		//comboBox for select documentType
		combBoxDocumentTypes  = new JComboBox<String>();
		combBoxDocumentTypes.setOpaque(false);
		
		//initialize the profiling panel
		initPanel();
		
		
		//add action listener on add button
		addBtn.addActionListener(new ActionListener() {
				@SuppressWarnings("unused")
				@Override
				public void actionPerformed(ActionEvent e) {
					List<URL> urls;
					
					if(selectFilePanel.isSelectedCheckCurrent()){
						urls = new ArrayList<URL>();	
						urls.add(sourceDescription.getCurrentUrl());
					} else {
						urls = selectFilePanel.getFilesFromTable();
					}
					
					ConfigureConditionsDialog conditionsDialog = new ConfigureConditionsDialog(problemReporter, urls, ProfilingPanel.this,
							translator, oxygenInteractor.getApplicationFrame(), profilingConditionsInformations );
				}
			});
		
		
		//add action listener on remove button
		remvBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int index0 = table.getSelectionModel().getMinSelectionIndex();
				int index1 = table.getSelectionModel().getMaxSelectionIndex();

				for (int i = index1; i >= index0; i--) {
					int modelRow = table.convertRowIndexToModel(i);
					modelCondTable.removeRow(modelRow);
				}
				
					if(modelCondTable.getRowCount() == 0){
						addBtn.setText(translator.getTranslation(Tags.ADD_TABLE));
					}
					remvBtn.setEnabled(false);

			}
		});

		// add actionListeners on useProfilingCondCBox checkBox
		useProfilingCondCBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!useProfilingCondCBox.isSelected()) {
					// when checkBox wasn't select

					// set radioButtons disable
					configProfilingCondSetRBtn.setEnabled(false);
					useAllCondSetsRBtn.setEnabled(false);

					// clear selections from table
					table.clearSelection();

					// set table buttons disable
					addBtn.setEnabled(false);
					remvBtn.setEnabled(false);

					if(!reportUndefinedConditionsCBox.isSelected()){
						//disable the comboBox and the buttons
						combBoxDocumentTypes.setEnabled(false);
						addDocumentTypeButton.setEnabled(false);
						removeDocumentTypeButton.setEnabled(false);
					}

				} else {
					// when checkBox was select

					// set radioButtons enable
					configProfilingCondSetRBtn.setEnabled(true);
					useAllCondSetsRBtn.setEnabled(true);

					//enable the comboBox and the buttons
					combBoxDocumentTypes.setEnabled(true);
					addDocumentTypeButton.setEnabled(true);
					if(!(combBoxDocumentTypes.getSelectedItem().equals(ProfilingConditionsInformations.DOCBOOK4) 
							|| combBoxDocumentTypes.getSelectedItem().equals(ProfilingConditionsInformations.DOCBOOK5))){
						removeDocumentTypeButton.setEnabled(true);
					}
					
					if (configProfilingCondSetRBtn.isSelected()) {
						// set table buttons enable
						addBtn.setEnabled(true);
					}
				}
			}
		});

		
		// add action listener on comboBox
		combBoxDocumentTypes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				comboBoxUpdate();
			}

		});
		
		// add action listener on configureProfilingConditionSet radioButton
		configProfilingCondSetRBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// set get and add buttons enable
				addBtn.setEnabled(true);
			}
		});

		
		// add action listener on useAllCondSets radioButton
		useAllCondSetsRBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// disable table buttons
				addBtn.setEnabled(false);
				remvBtn.setEnabled(false);
			}
		});
		
		
		//add action listener on reportUndefinedConditions checkBox
		reportUndefinedConditionsCBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!reportUndefinedConditionsCBox.isSelected()) {
					// when checkBox wasn't select

					if(!useProfilingCondCBox.isSelected()){
						//disable the comboBox and the buttons
						combBoxDocumentTypes.setEnabled(false);
						addDocumentTypeButton.setEnabled(false);
						removeDocumentTypeButton.setEnabled(false);
					}

				} else {
					// when checkBox was select

					//enable the comboBox and the buttons
					combBoxDocumentTypes.setEnabled(true);
					addDocumentTypeButton.setEnabled(true);

					if(!(combBoxDocumentTypes.getSelectedItem().equals(ProfilingConditionsInformations.DOCBOOK4) 
							|| combBoxDocumentTypes.getSelectedItem().equals(ProfilingConditionsInformations.DOCBOOK5))){
						removeDocumentTypeButton.setEnabled(true);
					}
				}
			}
		});
		
	}

	/**
	 * Get the add button.
	 * 
	 * @return The button.
	 */
	public JButton getAddBtn() {
		return addBtn;
	}

	/**
	 * Is selected useProfiling CheckBox.
	 * 
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	public boolean isSelectedUseProfilingCBox() {
		return useProfilingCondCBox.isSelected();
	}

	/**
	 * Set selected useProfiling CheckBox.
	 * 
	 * @param state
	 *          <code>true</code> if set selected, <code>false</code>otherwise.
	 */
	public void setSelectedUseProfilingCBox(boolean state) {
		useProfilingCondCBox.setSelected(state);
	}

	/**
	 * Do click on useProfiling CheckBox
	 */
	public void doClickOnUseProfilingCBox() {
		useProfilingCondCBox.doClick();
	}

	/**
	 * Is selected configProfilingCondSet radioButton.
	 * 
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	public boolean isSelectedConfigProfilingRBtn() {
		return configProfilingCondSetRBtn.isSelected();
	}

	/**
	 * Do click on configProfilingCondSet radioButton.
	 */
	public void doClickOnConfigProfilingRBtn() {
		configProfilingCondSetRBtn.doClick();
	}

	/**
	 * Do click on useAllCondSets radioButton.
	 */
	public void doClickOnUseAllCondSetsRBtn() {
		useAllCondSetsRBtn.doClick();
	}

	/**
	 * Is selected reportUndefinedConditions CheckBox.
	 * 
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	public boolean isSelectedReportedUndefinedConditionsCBox() {
		return reportUndefinedConditionsCBox.isSelected();
	}

	/**
	 * Set state of  reportUndefinedConditions CheckBox and doClick
	 * @param state <code>true</code> selected, <code>false</code> otherwise.
	 */
	public void setSelectedReportUndefinedConditionsCBox(boolean state) {
		reportUndefinedConditionsCBox.setSelected(!state);
		reportUndefinedConditionsCBox.doClick();
	}


	/**
	 * Get the selected document type.
	 * 
	 * @return The selected document type
	 */
	public String getSelectedDocumentType() {
		return (String) combBoxDocumentTypes.getSelectedItem();
	}

	/**
	 * Set selected document type.
	 * 
	 * @param docType
	 *          The document type
	 */
	public void setSelectedDocumentType(String docType) {
		combBoxDocumentTypes.setSelectedItem(docType);
	}

	/**
	 * Get all document types.
	 * 
	 * @return List with document types.
	 */
	public List<String> getAllDocumentTypes() {
		List<String> toReturn = new ArrayList<String>();

		int size = combBoxDocumentTypes.getItemCount();

		for (int i = 0; i < size; i++) {
			toReturn.add((String) combBoxDocumentTypes.getItemAt(i));
		}

		return toReturn;
	}

	/**
	 * Set document types.
	 * 
	 * @param toSet
	 *          Document types
	 */
	public void setAllDocumentTypes(List<String> toSet) {

		int size = toSet.size();

		for (int i = 0; i < size; i++) {
			combBoxDocumentTypes.addItem(toSet.get(i));
		}
	}
	
	/**
	 * Get declared conditions form table.
	 * 
	 * @return a LinkedHashMap with conditions.
	 */
	public LinkedHashMap<String, LinkedHashSet<String>> getConditionsFromTable() {
		LinkedHashMap<String, LinkedHashSet<String>> toReturn = new LinkedHashMap<String, LinkedHashSet<String>>();
		for (int i = 0; i < modelCondTable.getRowCount(); i++) {
			String key = (String) modelCondTable.getValueAt(i, 0);
			String value = (String) modelCondTable.getValueAt(i, 1);
			LinkedHashSet<String> setValue = new LinkedHashSet<String>(Arrays.asList(value.split(";")));

			toReturn.put(key, setValue);
		}
		return toReturn;
	}
	
	/**
	 * Add a given map with conditions in table and change the text from add button according to given map.
	 * 
	 * @param conditions The conditions in Map<String, LinkedHashSet<String>> format.
	 */
	public void addInTable(Map<String, LinkedHashSet<String>> conditions) {
		if (!conditions.isEmpty()) {
			Iterator<String> itKeys = conditions.keySet().iterator();
			// iterate over keys
			while (itKeys.hasNext()) {
				// key
				String key = itKeys.next();
				// value
				String value = ContentPersisterUtil.join(";", conditions.get(key));
				// add in table
				modelCondTable.addRow(new String[] { key, value });
			}
			// change add button text in "edit"
			addBtn.setText(translator.getTranslation(Tags.EDIT_TABLE));
		}else {
			// change add button text in "Add"
			addBtn.setText(translator.getTranslation(Tags.ADD_TABLE));
		}

	}

	/**
	 * Add a given map with conditions in table and change the text from add button according to given map.
	 * 
	 * @param conditions The conditions in LinkedHashMap<String, String> format.
	 */
	public void addInTable(LinkedHashMap<String, String> conditions) {
	
		if(!conditions.isEmpty()){
			
			//iterate over keys(attributes)
			Iterator<String> iterKey = conditions.keySet().iterator();
			while(iterKey.hasNext()){
				String attribute = iterKey.next();
				
				//get the value
				String value = conditions.get(attribute);
				
				//add condition in table
				modelCondTable.addRow(new String[]{attribute, value});
			}
			//set text on add button to "edit"
			addBtn.setText(translator.getTranslation(Tags.EDIT_TABLE));
		}
	}
	
	/**
	 * Delete all rows from table.
	 */
	public void clearTable() {
		for (int i = modelCondTable.getRowCount() - 1; i >= 0; i--) {
			modelCondTable.removeRow(i);
		}
	}
	
	/**
	 * Method for initialize the Profiling Panel.
	 * 
	 */
	private void initPanel() {

		// Create a group with radioButtons
		ButtonGroup group = new ButtonGroup();
		// add radioButtons in ButtonGroup
		group.add(configProfilingCondSetRBtn);
		group.add(useAllCondSetsRBtn);

		// table models
		modelCondTable = new DefaultTableModel(translator.getTranslation(Tags.CONDTIONS_TABLE_HEAD).split(";"), 0);
		
		// configure table
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		table.setPreferredScrollableViewportSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight()));
		table.setModel(modelCondTable);

		// Set element transparent.
		scrollPane.setOpaque(false);

		//set layout manager
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();

		// -------------- add the panel for select the document type
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(createSelectDocTypePanel(),gbc);

		// -------------- add checkBox for select to check using profiling
		// conditions
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 5, 0, 0);
		this.add(useProfilingCondCBox, gbc);
		
		// -------------- Radio button for select to configure a conditions set
		gbc.gridy++;
		gbc.insets = new Insets(0, 15, 0, 0);
		configProfilingCondSetRBtn.setSelected(true);
		this.add(configProfilingCondSetRBtn, gbc);

		// --------------- add scrollPane, that contains conditionsTable
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 30, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		// add list selection listener
		table.getSelectionModel().addListSelectionListener(listSelectionListener);
		this.add(scrollPane, gbc);

		// ---------------- add getBtn, addBtn and removeBtn
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;

		// panel that contains get, add and remove buttons
		JPanel btnsPanel = new JPanel(new GridLayout(1, 2));
		btnsPanel.setOpaque(false);

		btnsPanel.add(addBtn);
		btnsPanel.add(remvBtn);

		// add table btnsPanel
		this.add(btnsPanel, gbc);

		// ------------------ add checkBox for select to check using all available
		// conditions sets and a button to display  Preferences/Profiling/Conditional text.
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(4, 15, 0, 0);
		this.add(createAvailableConditionsSetPanel(), gbc);
		
		// ------------------  add reportUndefined checkBox
		gbc.gridy++;
		gbc.insets = new Insets(4, 5, 0, 0);
		this.add(reportUndefinedConditionsCBox,gbc);


	}

	/**
	 * List selection listener.
	 */
	ListSelectionListener listSelectionListener = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!remvBtn.isEnabled()) {
				// set remove button enable
				remvBtn.setEnabled(true);
			}
		}
	};

	
	
	/**
	 * Create a panel that contains the checkBox for select to use all available
	 * conditions sets and a button for access the preferences.
	 * 
	 * @return The panel.
	 */
	private JPanel createAvailableConditionsSetPanel() {
		JPanel toReturn = new JPanel(new GridBagLayout());

		
		// Action for show profiling preferences.
		 AbstractAction showProfilingPageAction = new AbstractAction("Profiling Preferences") {

			@Override
			public void actionPerformed(ActionEvent e) {
				PluginWorkspaceProvider.getPluginWorkspace().showPreferencesPages(new String[] { "profiling.conditions" },
						"profiling.conditions", true);
			
				//update the panel 
				comboBoxUpdate();
			}
		};

		
		 // Button for display profiling preferences
		 ToolbarButton buttonToProfiling = new ToolbarButton(showProfilingPageAction, true);
		
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		//add the checkBox
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		toReturn.add(useAllCondSetsRBtn, gbc);
		
		//add the button
		gbc.gridx = 1;
		gbc.weightx = 0;

		URL imageToLoad = getClass().getClassLoader().getResource(Images.PREFERENCES_ICON);
		if (imageToLoad != null) {
			buttonToProfiling.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
			buttonToProfiling.setText("");
		} else {
			buttonToProfiling.setText("PC");
		}
		toReturn.add(buttonToProfiling, gbc);

		return toReturn;
	}

	
	/**
	 * Create a panel for select the document type.
	 * @return The panel
	 */
	private JPanel createSelectDocTypePanel(){
		JPanel toReturn  =  new JPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		
		//add document type JLabel
		gbc.gridx = 0;
		gbc.gridy = 0;
		toReturn.add(new JLabel(translator.getTranslation(Tags.SELECT_DOCUMENT_TYPE)) , gbc);
			
		//add comboBox
		gbc.gridx++;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		toReturn.add(combBoxDocumentTypes, gbc);
		
		//action for add button
		Action addDocTypeAction = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String docType = JOptionPane.showInputDialog(null, (Object)new JLabel(translator.getTranslation(Tags.INSERT_DOC_TYPE_LABEL)), "", 
						 JOptionPane.PLAIN_MESSAGE);
				if(docType !=null && !docType.isEmpty()){
					combBoxDocumentTypes.addItem(docType);
					combBoxDocumentTypes.setSelectedItem(docType);
				}
			}
		};
		
		addDocumentTypeButton = new ToolbarButton(addDocTypeAction, false);
		
		// Get the image for toolbar button
		URL imageToLoad = getClass().getClassLoader().getResource(Images.ADD_ICON);
		if (imageToLoad != null) {
			addDocumentTypeButton.setText("");
			addDocumentTypeButton.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
		}
		
		//add the addButton
		gbc.gridx++;
		gbc.weightx = 0;
		toReturn.add(addDocumentTypeButton,gbc);
		
		
		
		//action for remove button
			Action removeAction = new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
						combBoxDocumentTypes.removeItem(combBoxDocumentTypes.getSelectedItem());
					}
			};
			
			removeDocumentTypeButton = new ToolbarButton(removeAction, false);
			
			// Get the image for toolbar button
			imageToLoad = getClass().getClassLoader().getResource(Images.REMOVE_ICON);
			if (imageToLoad != null) {
				removeDocumentTypeButton.setText("");
				removeDocumentTypeButton.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
			}
			
			//add the addButton
			gbc.gridx++;
			toReturn.add(removeDocumentTypeButton,gbc);
		
		
		return toReturn;
		
	}
	
	/**
	 * Update the panel according to conboBox selected item.
	 */
	private void comboBoxUpdate(){
		String docType = (String) combBoxDocumentTypes.getSelectedItem();
		if(!docType.isEmpty()){
			changeConditionsSetsFromRadioButton(profilingConditionsInformations.getConditionSetsNames(docType));
		}else{
			changeConditionsSetsFromRadioButton(new HashSet<String>());
		}
		if(docType.equals(ProfilingConditionsInformations.DOCBOOK4) || docType.equals(ProfilingConditionsInformations.DOCBOOK5)){
			removeDocumentTypeButton.setEnabled(false);
		}else{
			removeDocumentTypeButton.setEnabled(true);
		}

	}
	
	
	/**
	 * Change conditions text from useAllCondSetsRBtn's text 
	 * @param conditionSetsNames Conditions to be add.
	 */
	public void changeConditionsSetsFromRadioButton(Set<String> conditionSetsNames){
		//text to be add
		String toAdd;

		//integrate conditions set in text from radio button
		if (conditionSetsNames.isEmpty()) {
			toAdd = "<"+translator.getTranslation(Tags.USPECIFIED_CONDITIONS)+">";
		} else {
			toAdd = ContentPersisterUtil.join(";", conditionSetsNames);
		}
		
		//set text
		useAllCondSetsRBtn.setText(translator.getTranslation(Tags.ALL_CONDITIONS_SETS) + " " + toAdd);
	}
	
}


