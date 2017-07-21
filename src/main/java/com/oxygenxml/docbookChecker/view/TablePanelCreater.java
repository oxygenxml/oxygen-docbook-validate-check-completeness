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

public class TablePanelCreater {

	
	private JTable tableFiles = new JTable(20,2);
	private JScrollPane scrollPane = new JScrollPane(tableFiles);
	private JButton addBtn = new JButton("Add");
	private JButton remvBtn = new JButton("Remove");

	private String[] theadTable = {"Files URLs:"};
	private DefaultTableModel modelTable = new DefaultTableModel(theadTable, 0);


	public JTable getTableFiles() {
		return tableFiles;
	}

	public JButton getAddBtn() {
		return addBtn;
	}


	public JButton getRemvBtn() {
		return remvBtn;
	}
	
	public DefaultTableModel getTableModel (){
		return modelTable;
	}


	public TablePanelCreater() {
		tableFiles.getSelectionModel().addListSelectionListener(listSelectionListener);
	}
	
	/**
	 * Method for create Table Panel this return a JPanel object
	 */
	public JPanel create() {
		JPanel tablePanel = new JPanel(new GridBagLayout());
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tableFiles.setPreferredScrollableViewportSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight()));
		tableFiles.setModel(modelTable);
		
		tablePanel.setBackground(Color.WHITE);
		scrollPane.setBackground(Color.WHITE);
		
		GridBagConstraints gbc = new GridBagConstraints();


		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 25, 5, 25);
		tablePanel.add(scrollPane, gbc);


		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 0, 25);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		JPanel btnsPanel = new JPanel();
		btnsPanel.setLayout(new GridLayout(1, 2) );
		btnsPanel.add(addBtn);
		btnsPanel.add(remvBtn);
		remvBtn.setEnabled(false);
		btnsPanel.setBackground(Color.WHITE);
		
		tablePanel.add(btnsPanel, gbc);

		return tablePanel;
	}

	/**
	 * List selection listener.
	 */
	ListSelectionListener listSelectionListener = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			//set remove button enable
			remvBtn.setEnabled(true);

		}
	};


	public void addListenerOnAddBtn(ActionListener action){
		addBtn.addActionListener(action);
	}
	
	
	public void addListenerOnRemoveBtn(ActionListener action){
		remvBtn.addActionListener(action);
	}
	
}
