package com.oxygenxml.docbookChecker.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbookChecker.translator.Tags;
import com.oxygenxml.docbookChecker.translator.Translator;

/**
 * Table panel creator
 * 
 * @author intern4
 *
 */
public class TableFilesPanelCreator implements TablePanelAccess {

	/**
	 * table with files to check
	 */
	private JTable tableFiles = new JTable(20, 2);

	private DefaultTableModel modelTable;
	/**
	 * scrollPane for table.
	 */
	private JScrollPane scrollPane = new JScrollPane(tableFiles);
	/**
	 * button for add elements in table
	 */
	private JButton addBtn = new JButton();
	/**
	 * button for remove elements from table
	 */
	private JButton remvBtn = new JButton();

	/**
	 * table model
	 */
	
	private Translator translator;


	/**
	 * Constructor
	 */
	public TableFilesPanelCreator(Translator translator) {
		this.translator = translator;
		modelTable = new DefaultTableModel(new String[]{translator.getTraslation(Tags.FILES_TABLE_HEAD)}, 0);
		tableFiles.getSelectionModel().addListSelectionListener(listSelectionListener);
		
	}
	
	// getters
	public JTable getTable() {
		return tableFiles;
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
	 * Method for create Table Panel.
	 * @return the panel.
	 */
	public JPanel create() {
		//panel toReturn
		JPanel tablePanel = new JPanel(new GridBagLayout());

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tableFiles.setPreferredScrollableViewportSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight()));
		tableFiles.setModel(modelTable);

		tablePanel.setBackground(Color.WHITE);
		scrollPane.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();

		//add scrollPane
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		tablePanel.add(scrollPane, gbc);

		//add addBtn and removeBtn
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		//panel that contains add and remove buttons
		JPanel btnsPanel = new JPanel();
		btnsPanel.setLayout(new GridLayout(1, 2));
		btnsPanel.add(addBtn);
		addBtn.setEnabled(false);
		addBtn.setText(translator.getTraslation(Tags.ADD_TABLE));
		btnsPanel.add(remvBtn);
		remvBtn.setEnabled(false);
		remvBtn.setText(translator.getTraslation(Tags.REMOVE_TABLE));
		btnsPanel.setBackground(Color.WHITE);
		//add btnsPanel
		tablePanel.add(btnsPanel, gbc);

		return tablePanel;
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
