package com.oxygenxml.docbook.checker.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;

/**
 * Files table panel creator.
 * 
 * @author intern4
 *
 */
public class SelectFilesPanel extends JPanel {

	/**
	 * Radio button for select to check current file
	 */
	private JRadioButton checkCurrent = new JRadioButton();
	/**
	 * Radio button for select to check other files
	 */
	private JRadioButton checkOtherFiles = new JRadioButton();
	
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
	 * Translator
	 */
	private Translator translator;



	/**
	 * Constructor
	 */
	public SelectFilesPanel(final Translator translator, final JButton checkButton) {
		this.translator = translator;
		
		// initialize the panel
		initPanel();
		
		// add action listener on check current radio button
		checkCurrent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				checkButton.setEnabled(true);
				tableFiles.clearSelection();
				addBtn.setEnabled(false);
				remvBtn.setEnabled(false);
			}
		});
		
		
		// add action listener on check other files radio button
		checkOtherFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (getTableUrls().isEmpty()) {
					// disable the check button if is not other filesToCheck
					checkButton.setEnabled(false);
				}

				addBtn.setEnabled(true);
			}
		});
		
		
		// add action listener on add button
		addBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//open a URL chooser
				String file = PluginWorkspaceProvider.getPluginWorkspace()
						.chooseURLPath(translator.getTranslation(Tags.FILE_CHOOSER_TITLE), new String[] { "xml" }, "xml files", "");

				if(file != null){
					if (!tableContains(file)) {
						modelTable.addRow(new String[] { file });
					}
				}
			}
		});

		
		// add action listener on remove button
		remvBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index0 = tableFiles.getSelectionModel().getMinSelectionIndex();
				int index1 = tableFiles.getSelectionModel().getMaxSelectionIndex();

				for (int i = index1; i >= index0; i--) {
					int modelRow = tableFiles.convertRowIndexToModel(i);
					modelTable.removeRow(modelRow);
				}
				
				if(modelTable.getRowCount() == 0){
					checkButton.setEnabled(false);
				}
				
				remvBtn.setEnabled(false);
			}				
		});
		
	}
	
	// getters and setters
	public JTable getTable() {
		return tableFiles;
	}

	public DefaultTableModel getTableModel() {
		return modelTable;
	}

	public JRadioButton getCheckCurrent() {
		return checkCurrent;
	}

	public void setCheckCurrent(JRadioButton checkCurrent) {
		this.checkCurrent = checkCurrent;
	}

	public JRadioButton getCheckOtherFiles() {
		return checkOtherFiles;
	}

	public void setCheckOtherFiles(JRadioButton checkOtherFiles) {
		this.checkOtherFiles = checkOtherFiles;
	}

	/**
	 * Method for initialize the Panel.
	 */
	private void initPanel() {
		
		modelTable = new DefaultTableModel(new String[]{translator.getTranslation(Tags.FILES_TABLE_HEAD)}, 0);
		//set modal on table
		tableFiles.setModel(modelTable);
			
		//add list selection listener on table
		tableFiles.getSelectionModel().addListSelectionListener(listSelectionListener);
		
		//configure the scroll pane
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tableFiles.setPreferredScrollableViewportSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight()));
		scrollPane.setOpaque(false);

		//set layout manager
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();

		//Create a group with  the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(checkCurrent);
		group.add(checkOtherFiles);

		//------add JLabel for select file
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(new JLabel(translator.getTranslation(Tags.SELECT_FILES_LABEL_KEY)), gbc);

		//------add checkCurrent radio button
		gbc.gridy++;
		gbc.insets = new Insets(0, 10, 0, 0);
		checkCurrent.setText(translator.getTranslation(Tags.CHECK_CURRENT_FILE_KEY));
		this.add(checkCurrent, gbc);

		//------add checkOtherFiles radio button
		gbc.gridy++;
		checkOtherFiles.setText(translator.getTranslation(Tags.CHECK_OTHER_FILES_KEY));
		this.add(checkOtherFiles, gbc);

		//------add scrollPane
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 30, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		this.add(scrollPane, gbc);

		//------add addBtn and removeBtn
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		
		//create a panel that contains add and remove buttons
		JPanel btnsPanel = new JPanel();
		btnsPanel.setLayout(new GridLayout(1, 2));
		
		btnsPanel.add(addBtn);
		addBtn.setEnabled(false);
		addBtn.setText(translator.getTranslation(Tags.ADD_TABLE));
		
		btnsPanel.add(remvBtn);
		remvBtn.setEnabled(false);
		remvBtn.setText(translator.getTranslation(Tags.REMOVE_TABLE));
		btnsPanel.setOpaque(false);
		
		//add btnsPanel
		this.add(btnsPanel, gbc);

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
	 * Delete all rows from files table.
	 */
	public void clearTable(){
		int size = modelTable.getRowCount();
		for (int i = 0; i < size; i++) {
			modelTable.removeRow(0);
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
