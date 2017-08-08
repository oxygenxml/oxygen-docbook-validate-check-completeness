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
/**
 * Profiling panel creator.
 * @author intern4
 *
 */
public class ProfilingPanelCreator implements TablePanelAccess, ProfilingInformationWorkerReporter{
	/**
	 * CheckBox for select to use profiling conditions for check.
	 */
	private JCheckBox useProfilingCondCBox = new JCheckBox();

	/**
	 * RadioButton for select to configure a profiling condition set.
	 */
	private JRadioButton configProfilingCondSetRBtn = new JRadioButton();

	/**
	 * RadioButton for select to check all combination of profiling conditions
	 */
	private JRadioButton checkAllProfilingRBtn = new JRadioButton();

	/**
	 * Table with profiling conditions.
	 */
	private JTable tableConditions = new JTable(20, 2);

	/**
	 * Model for table.
	 */
	private DefaultTableModel modelTable;
	
	/**
	 * ScrollPane for table.
	 */
	private JScrollPane scrollPane = new JScrollPane(tableConditions);

	/**
	 * Button for get profiling conditions from files.
	 */
	private JButton getBtn = new JButton();
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
	 * Constructor
	 * @param translator 
	 * @param parentComponent
	*/
	public ProfilingPanelCreator(Translator translator , Component parentComponent) {
		this.translator = translator;
		this.parentComponent = parentComponent;
		
		//table model
		modelTable = new DefaultTableModel(translator.getTraslation(Tags.CONDTIONS_TABLE_HEAD).split(";"), 0);
		
		//add list selection listener
		tableConditions.getSelectionModel().addListSelectionListener(listSelectionListener);

		// add actionListeners on checkBox and radioButtons
		useProfilingCondCBox.addActionListener(createProfilingAction());
		configProfilingCondSetRBtn.addActionListener(createConfigProfilingAction());
		checkAllProfilingRBtn.addActionListener(createCheckAllProfilingAction());
	}

	
	
	// getters and setters
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
		return useProfilingCondCBox;
	}

	public JRadioButton getConfigProfilingRBtn() {
		return configProfilingCondSetRBtn;
	}

	public JRadioButton getCheckAllProfilingRBtn() {
		return checkAllProfilingRBtn;
	}

	public DefaultTableModel getModelTable() {
		return modelTable;
	}

	public void setProfilingCondCBox(JCheckBox profilingCondCBox) {
		this.useProfilingCondCBox = profilingCondCBox;
	}

	public void setConfigProfilingRBtn(JRadioButton configProfilingRBtn) {
		this.configProfilingCondSetRBtn = configProfilingRBtn;
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
		group.add(configProfilingCondSetRBtn);
		group.add(checkAllProfilingRBtn);

		//configure table
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tableConditions.setPreferredScrollableViewportSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight()));
		tableConditions.setModel(modelTable);

		//Set element transparent.
		profilingPanel.setOpaque(false);
		scrollPane.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();

		// add checkBox for select to check using profiling conditions
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		useProfilingCondCBox.setText(translator.getTraslation(Tags.USE_PROFLING_CBOX));
		profilingPanel.add(useProfilingCondCBox, gbc);

		// Radio button for select to configure a conditions set
		gbc.gridy++;
		gbc.insets = new Insets(0, 10, 0, 0);
		configProfilingCondSetRBtn.setText(translator.getTraslation(Tags.CONFIG_CONDITIONS_SET));
		configProfilingCondSetRBtn.setSelected(true);
		profilingPanel.add(configProfilingCondSetRBtn, gbc);

		// add scrollPane, that contains conditionsTable
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 30, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		profilingPanel.add(scrollPane, gbc);

		// add getBtn, addBtn and removeBtn
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		
		// panel that contains get, add and remove buttons
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
		btnsPanel.setOpaque(false);
		
		// add btnsPanel
		profilingPanel.add(btnsPanel, gbc);

		//add checkBox for select to check using all combinations of conditions sets.
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
	 * Create action listener for useProfilingCondCBox JCheckButton
	 */
	private ActionListener createProfilingAction() {
		ActionListener profilingCondCBoxAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!useProfilingCondCBox.isSelected()) {
					//when checkBox wasn't select
					//set radioButtons disable
					configProfilingCondSetRBtn.setEnabled(false);
					checkAllProfilingRBtn.setEnabled(false);
					//clear selections from table
					tableConditions.clearSelection();
					//set table buttons disable
					getBtn.setEnabled(false);
					addBtn.setEnabled(false);
					remvBtn.setEnabled(false);
				} 
				else {
					//when checkBox was select
					//set radioButtons enable
					configProfilingCondSetRBtn.setEnabled(true);
					checkAllProfilingRBtn.setEnabled(true);
					if (configProfilingCondSetRBtn.isSelected()) {
						configProfilingCondSetRBtn.doClick();
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
				//set get and add buttons enable
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
				// clear selected rows from table
				tableConditions.clearSelection();
				// disable table buttons
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

	
	private void displayAddTreeDialog( TreeModel treeModel, String dialogTitle, boolean expandNodes /*,Set<String> existentTableValues*/) {
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
		//TODO delete
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
