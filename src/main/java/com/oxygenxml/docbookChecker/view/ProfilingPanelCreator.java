package com.oxygenxml.docbookChecker.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.HTML.Tag;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.google.common.base.Joiner;
import com.jidesoft.utils.StringUtils;
import com.oxygenxml.docbookChecker.translator.Tags;
import com.oxygenxml.docbookChecker.translator.Translator;
import com.oxygenxml.profiling.ProfilingInformationWorkerReporter;

import ro.sync.exml.workspace.api.standalone.ui.OKCancelDialog;
import ro.sync.exml.workspace.api.standalone.ui.TreeCellRenderer;

public class ProfilingPanelCreator implements TablePanelAccess, ProfilingInformationWorkerReporter{
	/**
	 * CheckBox for select profiling condition check
	 */
	private JCheckBox profilingCondCBox = new JCheckBox();

	/**
	 * RadioButton for select to configure profiling condition set.
	 */
	private JRadioButton configProfilingRBtn = new JRadioButton();

	/**
	 * RadioButton for select to check all combination of profiling conditions
	 */
	private JRadioButton checkAllProfilingRBtn = new JRadioButton();

	/**
	 * table with profiling conditions to check
	 */
	private JTable tableConditions = new JTable(20, 2);

	private DefaultTableModel modelTable;
	/**
	 * scrollPane for table.
	 */
	private JScrollPane scrollPane = new JScrollPane(tableConditions);

	/**
	 * button for get profiling attributes from files.
	 */
	private JButton getBtn = new JButton();
	/**
	 * button for add profiling condition in table
	 */
	private JButton addBtn = new JButton();
	/**
	 * button for remove profiling condition from table
	 */
	private JButton remvBtn = new JButton();

	private Translator translator;

	private Component parentComponent;
	/**
	 * Constructor
	 */
	public ProfilingPanelCreator(Translator translator , Component parentComponent) {
		this.translator = translator;
		this.parentComponent = parentComponent;
		
		//table model
		modelTable = new DefaultTableModel(translator.getTraslation(Tags.CONDTIONS_TABLE_HEAD).split(";"), 0);
		
		//add list selection listener
		tableConditions.getSelectionModel().addListSelectionListener(listSelectionListener);

		// add actionListeners on checkBox and radioButtons
		profilingCondCBox.addActionListener(createProfilingAction());
		configProfilingRBtn.addActionListener(createConfigProfilingAction());
		checkAllProfilingRBtn.addActionListener(createCheckAllProfilingAction());
	}

	// getters
	public JTable getTable() {
		return tableConditions;
	}

	public JButton getAddBtn() {
		return addBtn;
	}

	public JButton getRemvBtn() {
		return remvBtn;
	}
	

	public JCheckBox getProfilingCondCBox() {
		return profilingCondCBox;
	}

	public JRadioButton getConfigProfilingRBtn() {
		return configProfilingRBtn;
	}

	public JRadioButton getCheckAllProfilingRBtn() {
		return checkAllProfilingRBtn;
	}

	public DefaultTableModel getModelTable() {
		return modelTable;
	}

	public void setProfilingCondCBox(JCheckBox profilingCondCBox) {
		this.profilingCondCBox = profilingCondCBox;
	}

	public void setConfigProfilingRBtn(JRadioButton configProfilingRBtn) {
		this.configProfilingRBtn = configProfilingRBtn;
	}

	public void setCheckAllProfilingRBtn(JRadioButton checkAllProfilingRBtn) {
		this.checkAllProfilingRBtn = checkAllProfilingRBtn;
	}

	public void setModelTable(DefaultTableModel modelTable) {
		this.modelTable = modelTable;
	}

	public DefaultTableModel getTableModel() {
		return modelTable;
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
		group.add(configProfilingRBtn);
		group.add(checkAllProfilingRBtn);

		//configure table
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tableConditions.setPreferredScrollableViewportSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight()));
		tableConditions.setModel(modelTable);

		profilingPanel.setOpaque(false);
		scrollPane.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();

		// add checkBox for select to check using profiling conditions
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		profilingCondCBox.setText(translator.getTraslation(Tags.USE_PROFLING_CBOX));
		profilingPanel.add(profilingCondCBox, gbc);

		// Radio button for select to check considering the table.
		gbc.gridy++;
		gbc.insets = new Insets(0, 10, 0, 0);
		configProfilingRBtn.setText(translator.getTraslation(Tags.CONFIG_CONDITIONS_SET));
		configProfilingRBtn.setSelected(true);
		profilingPanel.add(configProfilingRBtn, gbc);

		// add scrollPane
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 30, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		profilingPanel.add(scrollPane, gbc);

		// add addBtn and removeBtn
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		
		// panel that contains add and remove buttons
		JPanel btnsPanel = new JPanel(new GridLayout(1, 3));
		btnsPanel.add(getBtn);
		getBtn.setEnabled(false);
		getBtn.setText(translator.getTraslation(Tags.GET_TABLE));

		btnsPanel.add(addBtn);
		addBtn.setEnabled(false);
		addBtn.setText(translator.getTraslation(Tags.ADD_TABLE));

		btnsPanel.add(remvBtn);
		remvBtn.setEnabled(false);
		remvBtn.setText(translator.getTraslation(Tags.REMOVE_TABLE));
		btnsPanel.setBackground(Color.WHITE);
		
		// add btnsPanel
		profilingPanel.add(btnsPanel, gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 15, 0, 0);
		checkAllProfilingRBtn.setText(translator.getTraslation(Tags.COMBINATIONS_CONDITIONS_SET));
		checkAllProfilingRBtn.setSelected(false);
		profilingPanel.add(checkAllProfilingRBtn, gbc);

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
	 * Create action listener for profilingCondCBox JCheckButton
	 */
	private ActionListener createProfilingAction() {
		ActionListener profilingCondCBoxAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!profilingCondCBox.isSelected()) {
					configProfilingRBtn.setEnabled(false);
					checkAllProfilingRBtn.setEnabled(false);
					tableConditions.clearSelection();
					getBtn.setEnabled(false);
					addBtn.setEnabled(false);
					remvBtn.setEnabled(false);
				} else {
					configProfilingRBtn.setEnabled(true);
					checkAllProfilingRBtn.setEnabled(true);
					if (configProfilingRBtn.isSelected()) {
						getBtn.setEnabled(true);
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
				getBtn.setEnabled(true);
				addBtn.setEnabled(true);
			}
		};
		return configAction;
	}

	/**
	 * Create action listener for configProfilingRBtn radioButton
	 */
	private ActionListener createCheckAllProfilingAction() {
		ActionListener checkAllProfilingAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tableConditions.clearSelection();
				getBtn.setEnabled(false);
				addBtn.setEnabled(false);
				remvBtn.setEnabled(false);
			}
		};
		return checkAllProfilingAction;
	}

	/**
	 * Add listener on get attributes button.
	 * 
	 * @param action
	 */
	public void addListenerOnGetBtn(ActionListener action) {
		getBtn.addActionListener(action);
	}

	/**
	 * Add listener on add button.
	 * 
	 * @param action
	 */
	public void addListenerOnAddBtn(ActionListener action) {
		addBtn.addActionListener(action);
	}

	/**
	 * Add listener on remove button.
	 * 
	 * @param action
	 */
	public void addListenerOnRemoveBtn(ActionListener action) {
		remvBtn.addActionListener(action);
	}
	
	/**
	 * Delete all rows from table.
	 */
	public void clearTable(){
		for(int i = modelTable.getRowCount()-1 ; i >= 0; i--){
			modelTable.removeRow(i);
		} 
	}
	
	/**
	 * Add a given map with conditions in table.
	 * @param conditions the map 
	 */
	public void addInTable(Map<String, Set<String> > conditions){
		Iterator it = conditions.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pair = (Map.Entry)it.next();
        String attribute = pair.getKey().toString();

        String value = Joiner.on(";").join((Set<String>)pair.getValue()) ;
    
       modelTable.addRow(new String[]{attribute, value});
    }
	}

	public void displayAddTreeDialog( TreeModel treeModel, String dialogTitle, boolean expandNodes /*,Set<String> existentTableValues*/) {
		OKCancelDialog dialog = new OKCancelDialog((JFrame) parentComponent, "Add", true);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		final JCheckBoxTree cbTree = new JCheckBoxTree();
		cbTree.setModel(treeModel);
		cbTree.setShowsRootHandles(true);
		cbTree.setRootVisible(false);
		
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1; 
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(new JScrollPane(cbTree),gbc);

		// add action listener on ok button
		dialog.getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				Map<String, Set<String>> toAdd = new HashMap<String, Set<String>>();
				TreePath[] paths = cbTree.getCheckedPaths();
				for (TreePath tp : paths) {
		
					if (tp.getPath().length == 3) {
						Set<String> value = new HashSet<String>();
						value.add(tp.getPath()[2].toString());
						if(toAdd.containsKey(tp.getPath()[1].toString()) ) { 
							value.addAll(toAdd.get(tp.getPath()[1].toString()) ); 
						}
						toAdd.put(tp.getPath()[1].toString(), value);
						
					}
				}
				clearTable();
				addInTable(toAdd);
			}
		});

		if(expandNodes){
			cbTree.expandAllNodes();
		}
		
		dialog.add(panel);
		dialog.setTitle(dialogTitle);
		dialog.setOkButtonText(translator.getTraslation(Tags.ADD_BUTTON_IN_DIALOGS));
		dialog.pack();
		dialog.setSize(250, 400);
		dialog.setResizable(true);
		dialog.setLocationRelativeTo(parentComponent);
		dialog.setVisible(true);
		dialog.setFocusable(true);

	}

/*	*//**
	 * Create and display a JDialog with 2 inputFields for add row in table of
	 * condition set.
	 * 
	 * @param parentFrame
	 * @param translator
	 *//*
	public void displayAddJDialog(Component parentComponent, Translator translator) {
		OKCancelDialog dialog = new OKCancelDialog((JFrame) parentComponent, "Add", true);

		TreeCellRenderer tree = new TreeCellRenderer();

		JLabel attributeLabel = new JLabel();
		final JTextField atributeTextField = new JTextField();

		JLabel valueLabel = new JLabel();
		final JTextField valuesTextField = new JTextField();

		// add action listener on ok button
		dialog.getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("attrib: " + atributeTextField.getText() + " val: " + valuesTextField.getText());
				modelTable.addRow(new String[] { atributeTextField.getText(), valuesTextField.getText() });
			}
		});

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		// TODO add translate
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, 10, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		attributeLabel.setText("Atribute:");
		panel.add(attributeLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 1;
		panel.add(atributeTextField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.weightx = 0;
		// TODO add translate
		valueLabel.setText("Value :");
		panel.add(valueLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 1;
		panel.add(valuesTextField, gbc);

		dialog.add(panel);
		dialog.setOkButtonText("Add");

		dialog.pack();
		dialog.setSize(250, 200);
		dialog.setLocationRelativeTo(parentComponent);
		dialog.setVisible(true);
		dialog.setFocusable(true);

	}*/

	@Override
	public void reportConditionsSetsWorkerFinish(Map<String, Map<String, Set<String>>> result) {
    
		
	}

	@Override
	public void reportConditionsWorkerFinish(Map<String, Set<String>> result) {
		//create the root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Conditions");
		
		 Iterator it = result.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        DefaultMutableTreeNode node = new DefaultMutableTreeNode(pair.getKey());
	        
	        Iterator itVal = ((Set<String>)pair.getValue()).iterator();
	        while (itVal.hasNext()) {
						String value =  (String)itVal.next();
						DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(value);
						node.add(leaf);
	        }
	        root.add(node);
	    }	
	    addBtn.setEnabled(true);
	    displayAddTreeDialog(new DefaultTreeModel(root), "Add", false);
		
	}

	@Override
	public void reportConditionsAttributeNamesWorkerFinish(Set<String> result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportConditionsProfileDocsToCheckWorkerFinish(Map<String, Set<String>> result) {
		//create the root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Conditions");
		
		 Iterator it = result.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        DefaultMutableTreeNode node = new DefaultMutableTreeNode(pair.getKey());

	        Iterator itVal = ((Set<String>)pair.getValue()).iterator();
	        while (itVal.hasNext()) {
						String value =  (String)itVal.next();
						DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(value);
						node.add(leaf);
	        }
	        root.add(node);
	    }	
	    getBtn.setEnabled(true);
	    displayAddTreeDialog(new DefaultTreeModel(root), translator.getTraslation(Tags.FILE_CONDITIONS_DIALOG_TITLE), true);
		
	}
}
