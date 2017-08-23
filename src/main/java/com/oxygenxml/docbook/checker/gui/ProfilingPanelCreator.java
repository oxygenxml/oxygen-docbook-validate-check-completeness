package com.oxygenxml.docbook.checker.gui;

import java.awt.Component;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.google.common.base.Joiner;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
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
public class ProfilingPanelCreator  {
	/**
	 * CheckBox for select to use profiling conditions for check.
	 */
	private JCheckBox useProfilingCondCBox = new JCheckBox();

	/**
	 * RadioButton for select to configure a profiling condition set.
	 */
	private JRadioButton configProfilingCondSetRBtn = new JRadioButton();

	/**
	 * RadioButton for select all available conditions set.
	 */
	private JRadioButton useAllCondSetsRBtn = new JRadioButton();

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
	private JButton addBtn = new JButton();
	/**
	 * Button for remove selected profiling conditions from table
	 */
	private JButton remvBtn = new JButton();

	/**
	 * Used for internationalization.
	 */
	private Translator translator;

	/**
	 * The parent component;
	 */
	private Component parentComponent;
	

	/**
	 * Used for get the available profiling conditions sets
	 */
	private ProfilingConditionsInformations profilingConditionsInformations = new ProfilingConditionsInformationsImpl();


	private String currentOpenedFileURL;

	private ProblemReporter problemReporter;

	private JRadioButton checkCurrent;

	private DefaultTableModel fileTableModel;

	/**
	 * Constructor
	 * 
	 * @param translator
	 * @param parentComponent
	 */
	public ProfilingPanelCreator(JRadioButton checkCurrent, DefaultTableModel fileTableModel, String currentOpenedFileURL, ProblemReporter problemReporter ,Translator translator, Component parentComponent) {
		this.checkCurrent = checkCurrent;
		this.currentOpenedFileURL = currentOpenedFileURL;
		this.fileTableModel = fileTableModel;
		this.problemReporter = problemReporter;
		this.translator = translator;
		this.parentComponent = parentComponent;

		// table models
		modelCondTable = new DefaultTableModel(translator.getTranslation(Tags.CONDTIONS_TABLE_HEAD).split(";"), 0);

		// add list selection listener
		table.getSelectionModel().addListSelectionListener(listSelectionListener);

		// add actionListeners on checkBox and radioButtons
		useProfilingCondCBox.addActionListener(createUseProfilingActionListener());
		configProfilingCondSetRBtn.addActionListener(createConfigProfilingAction());
		useAllCondSetsRBtn.addActionListener(createUseAllCondSetsAction());
	}

	// getters and setters
	public JTable getTable() {
		return table;
	}

	public JButton getAddBtn() {
		return addBtn;
	}

	public JButton getRemvBtn() {
		return remvBtn;
	}

	public JCheckBox getProfilingCondCBox() {
		return useProfilingCondCBox;
	}

	public JRadioButton getConfigProfilingRBtn() {
		return configProfilingCondSetRBtn;
	}

	public JRadioButton getCheckAllProfilingRBtn() {
		return useAllCondSetsRBtn;
	}

	public DefaultTableModel getModelTable() {
		return modelCondTable;
	}

	public void setProfilingCondCBox(JCheckBox profilingCondCBox) {
		this.useProfilingCondCBox = profilingCondCBox;
	}

	public void setConfigProfilingRBtn(JRadioButton configProfilingRBtn) {
		this.configProfilingCondSetRBtn = configProfilingRBtn;
	}

	public void setCheckAllProfilingRBtn(JRadioButton checkAllProfilingRBtn) {
		this.useAllCondSetsRBtn = checkAllProfilingRBtn;
	}

	public void setModelTable(DefaultTableModel modelTable) {
		this.modelCondTable = modelTable;
	}

	public DefaultTableModel getTableModel() {
		return modelCondTable;
	}

	/**
	 * Method for create Profiling Panel.
	 * 
	 * @return the panel.
	 */
	public JPanel create() {
		// panel toReturn
		JPanel profilingPanel = new JPanel(new GridBagLayout());

		// group with radioButtons
		ButtonGroup group = new ButtonGroup();
		// add radioButtons in ButtonGroup
		group.add(configProfilingCondSetRBtn);
		group.add(useAllCondSetsRBtn);

		// configure table
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		table.setPreferredScrollableViewportSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight()));
		table.setModel(modelCondTable);

		// Set element transparent.
		profilingPanel.setOpaque(false);
		scrollPane.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();

		// -------------- add checkBox for select to check using profiling
		// conditions
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		useProfilingCondCBox.setText(translator.getTranslation(Tags.USE_PROFLING_CBOX));
		profilingPanel.add(useProfilingCondCBox, gbc);

		// -------------- Radio button for select to configure a conditions set
		gbc.gridy++;
		gbc.insets = new Insets(0, 10, 0, 0);
		configProfilingCondSetRBtn.setText(translator.getTranslation(Tags.CONFIG_CONDITIONS_SET));
		configProfilingCondSetRBtn.setSelected(true);
		profilingPanel.add(configProfilingCondSetRBtn, gbc);

		// --------------- add scrollPane, that contains conditionsTable
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 30, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		profilingPanel.add(scrollPane, gbc);

		// ---------------- add getBtn, addBtn and removeBtn
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;

		// panel that contains get, add and remove buttons
		JPanel btnsPanel = new JPanel(new GridLayout(1, 2));

		btnsPanel.add(addBtn);
		addBtn.setEnabled(false);
		addBtn.addActionListener(createAddActionListener());
		addBtn.setText(translator.getTranslation(Tags.ADD_TABLE));

		btnsPanel.add(remvBtn);
		remvBtn.setEnabled(false);
		remvBtn.addActionListener(createRemoveActionListener());
		remvBtn.setText(translator.getTranslation(Tags.REMOVE_TABLE));
		btnsPanel.setOpaque(false);

		// add table btnsPanel
		profilingPanel.add(btnsPanel, gbc);

		// ------------------ add checkBox for select to check using all available
		// conditions sets and a button to display  Preferences/Profiling/Conditional text.
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(4, 10, 0, 0);
		profilingPanel.add(createAvailableConditionsSetPanel(), gbc);

		return profilingPanel;
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
	 * Create action listener for useProfilingCondCBox JCheckButton
	 */
	private ActionListener createUseProfilingActionListener() {
		ActionListener profilingCondCBoxAction = new ActionListener() {

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
				} else {
					// when checkBox was select

					// set radioButtons enable
					configProfilingCondSetRBtn.setEnabled(true);
					useAllCondSetsRBtn.setEnabled(true);

					if (configProfilingCondSetRBtn.isSelected()) {
						// set table buttons enable
						addBtn.setEnabled(true);
					}
				}
			}
		};
		return profilingCondCBoxAction;
	}

	/**
	 * Create action listener for configProfilingRBtn radioButton
	 */
	private ActionListener createConfigProfilingAction() {
		ActionListener configAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// set get and add buttons enable
				addBtn.setEnabled(true);
			}
		};
		return configAction;
	}

	/**
	 * Create action listener for useAllCondSets radioButton
	 */
	private ActionListener createUseAllCondSetsAction() {
		ActionListener userAllCondSetsAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// disable table buttons
				addBtn.setEnabled(false);
				remvBtn.setEnabled(false);
			}
		};
		return userAllCondSetsAction;
	}


	
	/**
	 * Action listener for add button
	 * @return
	 */
		private ActionListener createAddActionListener(){
			ActionListener toReturn = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					List<String> urls =new  ArrayList<String>();
					
					if(checkCurrent.isSelected()){
							urls.add(currentOpenedFileURL);
					} else {
						int size = fileTableModel.getRowCount();
						for (int i = 0; i < size; i++) {
							urls.add("" + fileTableModel.getValueAt(i, 0));
						}
					}
					
					ConfigureConditionsDialog conditionsDialogCreator = new ConfigureConditionsDialog(problemReporter, urls, ProfilingPanelCreator.this,
							translator, (JFrame) parentComponent, false);
					
				}
			};
			return toReturn;	
		}
	
/**
 * Action listener for remove button
 * @return
 */
	private ActionListener createRemoveActionListener(){
		ActionListener toReturn = new ActionListener() {
			
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
		};
		return toReturn;
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
	 * Add a given map with conditions in table.
	 * 
	 * @param conditios The conditions in Map<String, LinkedHashSet<String>> format.
	 */
	public void addInTable(Map<String, LinkedHashSet<String>> conditions) {
		Iterator<String> itKeys = conditions.keySet().iterator();
		// iterate over keys
		while (itKeys.hasNext()) {
			// key
			String key = itKeys.next();
			// value
			String value = Joiner.on(";").join((Set<String>) conditions.get(key));
			// add in table
			modelCondTable.addRow(new String[] { key, value });
		}
	}
	

	/**
	 * Add a given map with conditions in table.
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
	 * Create a panel that contains the checkBox for select to use all available
	 * conditions sets and a button for access the preferences.
	 * 
	 * @return
	 */
	private JPanel createAvailableConditionsSetPanel() {
		JPanel toReturn = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		Set<String> conditionSets = profilingConditionsInformations
				.getConditionSetsNames(ProfilingConditionsInformations.ALL_DOCBOOKS);
		String toAdd;

		if (conditionSets.isEmpty()) {
			toAdd = "<"+translator.getTranslation(Tags.USPECIFIED_CONDITIONS)+">";
		} else {
			toAdd = Joiner.on(",").join(conditionSets);
		}
		useAllCondSetsRBtn.setText(translator.getTranslation(Tags.ALL_CONDITIONS_SET) + ": " + toAdd);
		
		//add the checkBox
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		toReturn.add(useAllCondSetsRBtn, gbc);

		

		/**
		 * Action for show profiling preferences.
		 */
		 AbstractAction showProfilingPageAction = new AbstractAction("Profiling Preferences") {

			@Override
			public void actionPerformed(ActionEvent e) {
				PluginWorkspaceProvider.getPluginWorkspace().showPreferencesPages(new String[] { "profiling.conditions" },
						"profiling.conditions", true);
			}
		};

		/**
		 * Button for display profiling preferences
		 */
		 ToolbarButton buttonToProfiling = new ToolbarButton(showProfilingPageAction, true);
		
		
		//add the button
		gbc.gridx = 1;
		gbc.weightx = 0;

		URL imageToLoad = getClass().getClassLoader().getResource("img/DBOptionsShortcut16.png");
		if (imageToLoad != null) {
			buttonToProfiling.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
			buttonToProfiling.setText("");
		} else {
			buttonToProfiling.setText("PC");
		}
		toReturn.add(buttonToProfiling, gbc);

		return toReturn;
	}

	
}
