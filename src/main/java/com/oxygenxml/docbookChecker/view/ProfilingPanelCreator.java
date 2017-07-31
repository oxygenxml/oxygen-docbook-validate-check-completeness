package com.oxygenxml.docbookChecker.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbookChecker.translator.Tags;
import com.oxygenxml.docbookChecker.translator.Translator;

public class ProfilingPanelCreator implements TablePanelAccess {
	/**
	 *CheckBox for select profiling condition check 
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

	/**
	 * table model
	 */
	
	private Translator translator;


	/**
	 * Constructor
	 */
	public ProfilingPanelCreator(Translator translator) {
		this.translator = translator;
		modelTable = new DefaultTableModel(new String[]{"Attribute","Values"}, 0);
		tableConditions.getSelectionModel().addListSelectionListener(listSelectionListener);
		
		//add actionListeners on checkBox and radioButtons
		profilingCondCBox.addActionListener(createProfilingAction());
		configProfilingRBtn.addActionListener(createConfigProfilingAction());
		checkAllProfilingRBtn.addActionListener(createCheckAllProfilingAction());
	}
	
	// getters
	public JTable getTable(){
		return tableConditions;
	}
	
	public JButton getAddBtn() {
		return addBtn;
	}
	
	public JButton getRemvBtn() {
		return remvBtn;
	}
	
	public DefaultTableModel getTableModel() {
		return modelTable;
	}

	
	/**
	 * Method for create Profiling Panel.
	 * @return the panel.
	 */
	public JPanel create() {
		//panel toReturn
		JPanel profilingPanel = new JPanel(new GridBagLayout());
		//group with radioButtons
		ButtonGroup group = new ButtonGroup();
		
		//add radioButtons in ButtonGroup
		group.add(configProfilingRBtn);
		group.add(checkAllProfilingRBtn);
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tableConditions.setPreferredScrollableViewportSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight()));
		tableConditions.setModel(modelTable);

		profilingPanel.setBackground(Color.WHITE);
		scrollPane.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		
		//add checkBox for select to check considering profiling conditions 
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		profilingCondCBox.setText("Use profiling conditions for check");
		profilingPanel.add(profilingCondCBox, gbc);
		
		//Radio button for select to check considering the table.
		gbc.gridy++;
		gbc.insets = new Insets(0, 10, 0, 0);
		configProfilingRBtn.setText("Configure profiling condition set");
		configProfilingRBtn.setEnabled(false);
		profilingPanel.add(configProfilingRBtn, gbc);
		
		
		//add scrollPane
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 30, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		profilingPanel.add(scrollPane, gbc);

		//add addBtn and removeBtn
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		//panel that contains add and remove buttons
		JPanel btnsPanel = new JPanel(new GridLayout(1, 3));
		btnsPanel.add(getBtn);
		getBtn.setEnabled(false);
		getBtn.setText("Get attributes");
		
		btnsPanel.add(addBtn);
		addBtn.setEnabled(false);
		addBtn.setText(translator.getTraslation(Tags.ADD_TABLE));
		btnsPanel.add(remvBtn);
		remvBtn.setEnabled(false);
		remvBtn.setText(translator.getTraslation(Tags.REMOVE_TABLE));
		btnsPanel.setBackground(Color.WHITE);
		//add btnsPanel
		profilingPanel.add(btnsPanel, gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 15, 0, 0);
		checkAllProfilingRBtn.setText("Check all combinations of profiling condition sets");
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
			if(!remvBtn.isEnabled()){
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
				if(!profilingCondCBox.isSelected()){
					configProfilingRBtn.setEnabled(false);
					checkAllProfilingRBtn.setEnabled(false);
					tableConditions.clearSelection();
					getBtn.setEnabled(false);
					addBtn.setEnabled(false);
					remvBtn.setEnabled(false);
				}
				else{
					configProfilingRBtn.setEnabled(true);
					checkAllProfilingRBtn.setEnabled(true);
					if(configProfilingRBtn.isSelected()){
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
	 * @param action
	 */
	public void addListenerOnGetBtn(ActionListener action) {
		getBtn.addActionListener(action);
	}
	
	
	/**
	 * Add listener on add button. 
	 * @param action
	 */
	public void addListenerOnAddBtn(ActionListener action) {
		addBtn.addActionListener(action);
	}

	/**
	 * Add listener on remove button.
	 * @param action
	 */
	public void addListenerOnRemoveBtn(ActionListener action) {
		remvBtn.addActionListener(action);
	}
}
