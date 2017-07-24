package com.oxygenxml.docbookChecker.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * Table panel creator
 * 
 * @author intern4
 *
 */
public class TablePanelCreator {

	/**
	 * table with files to check
	 */
	private JTable tableFiles = new JTable(20, 2);
	/**
	 * scrollPane for table.
	 */
	private JScrollPane scrollPane = new JScrollPane(tableFiles);
	/**
	 * button for add elements in table
	 */
	private JButton addBtn = new JButton("Add");
	/**
	 * button for remove elements from table
	 */
	private JButton remvBtn = new JButton("Remove");

	/**
	 * table head
	 */
	private String[] theadTable = { "Files URLs:" };
	/**
	 * table model
	 */
	private DefaultTableModel modelTable = new DefaultTableModel(theadTable, 0);

	// getters
	public JTable getTableFiles() {
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
	 * Constructor
	 */
	public TablePanelCreator() {
		tableFiles.getSelectionModel().addListSelectionListener(listSelectionListener);
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
		gbc.insets = new Insets(10, 25, 5, 25);
		tablePanel.add(scrollPane, gbc);

		//add addBtn and removeBtn
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 0, 25);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		//panel that contains add and remove buttons
		JPanel btnsPanel = new JPanel();
		btnsPanel.setLayout(new GridLayout(1, 2));
		btnsPanel.add(addBtn);
		btnsPanel.add(remvBtn);
		remvBtn.setEnabled(false);
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
			// set remove button enable
			remvBtn.setEnabled(true);

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
