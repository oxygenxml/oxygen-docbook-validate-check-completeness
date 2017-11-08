package com.oxygenxml.docbook.checker.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
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

import org.apache.log4j.Logger;

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
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Radio button for select to check current file
	 */
	private JRadioButton checkCurrent;
	/**
	 * Radio button for select to check other files
	 */
	private JRadioButton checkOtherFiles;
	
	/**
	 * Table with files to check.
	 */
	private JTable tableFiles = new JTable(20, 2);

	/**
	 * Model of table.
	 */
	private FileTableModel modelTable;
	
	/**
	 * scrollPane for table.
	 */
	private JScrollPane scrollPane = new JScrollPane(tableFiles);
	/**
	 * button for add elements in table
	 */
	private JButton addBtn;
	/**
	 * button for remove elements from table
	 */
	private JButton remvBtn;

	/**
	 * Translator
	 */
	private transient Translator translator;

	/**
	 * Logger
	 */
	 private static final Logger logger = Logger.getLogger(SelectFilesPanel.class);

	/**
	 * Constructor.
	 * @param translator Translator
	 * @param checkButton The check button.
	 */
	public SelectFilesPanel(final Translator translator, final JButton checkButton) {
		this.translator = translator;
		
		checkCurrent = new JRadioButton(translator.getTranslation(Tags.CHECK_CURRENT_FILE_KEY));
		checkOtherFiles = new JRadioButton(translator.getTranslation(Tags.CHECK_OTHER_FILES_KEY));
		
		addBtn = new JButton(translator.getTranslation(Tags.ADD_TABLE));
		remvBtn = new JButton(translator.getTranslation(Tags.REMOVE_TABLE));
		
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

				if (modelTable.getRowCount() == 0) {
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
					try {
						URL fileUrl = new URL(file);
						if (!tableContains(fileUrl)) {
							//add row
							modelTable.addRow(new URL[] { fileUrl });
							
							//set check button enable
							checkButton.setEnabled(true);
						}
					} catch (MalformedURLException e1) {
						logger.debug(e1.getMessage(), e1);
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
				
				if(modelTable.getRowCount() == 0 && checkOtherFiles.isSelected()){
					checkButton.setEnabled(false);
				}
				
				remvBtn.setEnabled(false);
			}				
		});
		
	}
	
	/**
	 * Is selected checkCurrent radioButton.
	 * @return <code>true</code> if it's selected, <code>false</code>otherwise.
	 */
	public boolean isSelectedCheckCurrent() {
		return checkCurrent.isSelected();
	}
	
	
	/**
	 * Set enable checkCurrent radioButton.
	 * @param state <code>true</code> if set enable, <code>false</code>otherwise.
	 */
	public void setEnableCheckCurrent(boolean state) {
		 checkCurrent.setEnabled(state);
	}

	/**
	 * Do click on checkCurrent radioButton
	 */
	public void doClickOnCheckCurrent() {
			checkCurrent.doClick();
	}
	
	/**
	 * Do click on checkOtherFiles radioButton
	 */
	public void doClickOnCheckOtherFiles() {
		checkOtherFiles.doClick();
	}
	
	/**
	 * Get file from files table.
	 * @return List with files in String format.
	 */
	public List<URL> getFilesFromTable()  {
		List<URL> toReturn = new ArrayList<URL>();

		int size = modelTable.getRowCount();
		for (int i = 0; i < size; i++) {
			Object currentFile = modelTable.getValueAt(i, 0);
			if(currentFile instanceof URL){
				toReturn.add((URL)currentFile);
			}
		}
		return toReturn;
	}
	
	/**
	 * Add rows in files table.
	 * @param urls List with URLs in string format.
	 */
	public void addRowsInTable(List<URL> urls){
		int size = urls.size();
		for (int i = 0; i < size; i++) {
			if(!tableContains(urls.get(i))){
				modelTable.addRow(new URL[] { urls.get(i)});
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
	 * Method for initialize the Panel.
	 */
	private void initPanel() {
		
		modelTable = new FileTableModel(new String[]{translator.getTranslation(Tags.FILES_TABLE_HEAD)}, 0);
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
		this.add(checkCurrent, gbc);

		//------add checkOtherFiles radio button
		gbc.gridy++;
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
		btnsPanel.setOpaque(false);
		
		btnsPanel.add(addBtn);
		btnsPanel.add(remvBtn);
		
		//add btnsPanel
		this.add(btnsPanel, gbc);

	}

	
	/**
	 * List selection listener.
	 */
	transient ListSelectionListener listSelectionListener = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(!remvBtn.isEnabled()){
				// set remove button enable
				remvBtn.setEnabled(true);
			}
		}
	};
	
	
	/**
	 * Check if table contains the given URL.
	 * @param url The URL.
	 * @return <code>true</code>>if URL is in table, <code>false</code>> if isn't.
	 */
	private boolean tableContains(URL url){
		boolean toReturn = false;
		int size = modelTable.getRowCount();
		for(int i = 0; i < size; i++){
			if(url.equals(modelTable.getValueAt(i, 0)) ){
				toReturn = true;
				break;
			}
		}
		return toReturn;
	}
}
