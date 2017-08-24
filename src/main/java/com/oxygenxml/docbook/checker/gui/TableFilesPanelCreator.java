package com.oxygenxml.docbook.checker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

/**
 * Files table panel creator.
 * 
 * @author intern4
 *
 */
public class TableFilesPanelCreator  {

	/**
	 * Table with files to check.
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

	private FileChooser fileChooser;


	/**
	 * Constructor
	 */
	public TableFilesPanelCreator(Translator translator, FileChooser fileChooser) {
		this.translator = translator;
		this.fileChooser = fileChooser;
		modelTable = new DefaultTableModel(new String[]{translator.getTranslation(Tags.FILES_TABLE_HEAD)}, 0);
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

		scrollPane.setOpaque(false);

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
		addBtn.addActionListener(createAddActionListener());
		addBtn.setText(translator.getTranslation(Tags.ADD_TABLE));
		btnsPanel.add(remvBtn);
		remvBtn.setEnabled(false);
		remvBtn.addActionListener(createRemoveActionListener());
		remvBtn.setText(translator.getTranslation(Tags.REMOVE_TABLE));
		btnsPanel.setOpaque(false);
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
	 * Create action listener for table add button
	 */
	private ActionListener createAddActionListener(){
		ActionListener toReturn = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				File[] files = fileChooser.createFileChooser(translator.getTranslation(Tags.FILE_CHOOSER_TITLE),
						translator.getTranslation(Tags.FILE_CHOOSER_BUTTON));
				
				if (files != null) {
					//add files in table
					for (int i = 0; i < files.length; i++) {
						
						if(!tableContains(files[i].toString())){
							modelTable.addRow(new String[] { files[i].toString() });
						}
					}
				}
				
			}
		};
		return toReturn;
	}
	
	/**
	 * Create action listener for table remove button
	 */
	private ActionListener createRemoveActionListener(){
		ActionListener toReturn = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index0 = tableFiles.getSelectionModel().getMinSelectionIndex();
				int index1 = tableFiles.getSelectionModel().getMaxSelectionIndex();

				for (int i = index1; i >= index0; i--) {
					int modelRow = tableFiles.convertRowIndexToModel(i);
					modelTable.removeRow(modelRow);
				}
				
				remvBtn.setEnabled(false);
			}				
		};
		return toReturn;
	}
	
	

	/**
	 * Add rows in files table.
	 * @param URLs List with URLs in string format.
	 */
	public void addRowsInTable(List<String> URLs){
		int size = URLs.size();
		for (int i = 0; i < size; i++) {
			if(!tableContains(URLs.get(i))){
				modelTable.addRow(new String[] { URLs.get(i) });
			}
		}
	}
	
	
	/**
	 * Get a list with URLs, in string format, from files table.
	 * @return
	 */
	public List<String> getTableUrls() {
		List<String> toReturn = new ArrayList<String>();

		// add rows in a list
		for (int i = 0; i < modelTable.getRowCount(); i++) {
			toReturn.add(modelTable.getValueAt(i, 0).toString());
		}
		return toReturn;
	}
	
	
	/**
	 * Check if table contains the given URL.
	 * @param url The URL in string format.
	 * @return <code>true</code>>if URL is in table, <code>false</code>> if isn't.
	 */
	private boolean tableContains(String url){
		boolean toReturn = false;
		for(int i = 0; i < modelTable.getRowCount(); i++){
			if(url.equals(modelTable.getValueAt(i, 0)) ){
				return true;
			}
		}

		return toReturn;
	}
}
