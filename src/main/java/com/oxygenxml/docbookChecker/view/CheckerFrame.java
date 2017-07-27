package com.oxygenxml.docbookChecker.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbookChecker.Settings;
import com.oxygenxml.docbookChecker.SettingsImpl;
import com.oxygenxml.docbookChecker.Worker;
import com.oxygenxml.docbookChecker.persister.ContentPersister;
import com.oxygenxml.docbookChecker.persister.ContentPersisterImpl;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.ProblemReporterImpl;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;
import com.oxygenxml.ldocbookChecker.parser.ParserCreator;
import com.oxygenxml.ldocbookChecker.parser.PlainParserCreator;
import com.sun.corba.se.spi.orbutil.fsm.Action;

import ro.sync.ecss.extensions.commons.ui.OKCancelDialog;

/**
 * The GUI for Broken Links Checker
 * 
 * @author intern4
 *
 */
public class CheckerFrame extends OKCancelDialog {
	
	private JRadioButton checkCurrent = new JRadioButton("Check current file");

	private JRadioButton checkOtherFiles = new JRadioButton("Check other files");

	/**
	 *  
	 */
	private JCheckBox checkExternalLinksCBox = new JCheckBox("Check external links");

	private JCheckBox checkImagesCBox = new JCheckBox("Check images");

	private JCheckBox checkInternalLinksCbox = new JCheckBox("Check internal links");

	/**
	 * 
	 */
	private TablePanelCreator tablePanelCreater = new TablePanelCreator();

	private CheckerFrame view = this;

	/**
	 * 
	 */
	private ProblemReporter problemReporter;
	
	private StatusReporter statusReporter;

	private FileChooserCreator fileChooser;

	private ParserCreator parserCreator;
	
	private ContentPersister contentPersister;

	/**
	 * The background worker
	 */
	private Worker worker;

	/**
	 * 
	 */

	private String currentUrl;

	

	/**
	 * Constructor
	 */
	public CheckerFrame(String url, Component component, ProblemReporter problemReporter, StatusReporter statusReporter, FileChooserCreator fileChooser,
			ParserCreator parseCreator, ContentPersister contentPersister) {
		super((JFrame) component, "DocBook references checker", true);
		
		//Initialize GUI
		initGUI();

		
		// add action listener on add button
		tablePanelCreater.addListenerOnAddBtn(addBtnAction);

		tablePanelCreater.addListenerOnRemoveBtn(removeBtnAction);

		this.currentUrl = url;

		this.fileChooser = fileChooser;
		this.parserCreator = parseCreator;

		this.problemReporter = problemReporter;
		this.statusReporter = statusReporter;
		this.contentPersister = contentPersister;

		
		// add action listener on radio buttons
		checkCurrent.addActionListener(checkCurrentAction);
		checkOtherFiles.addActionListener(checkOtherAction);
		
		// set saved content 
		contentPersister.setSavedContent(this);
		
		// add action listener on check/stop button
		getOkButton().addActionListener(checkBtnAction);
		
		
		setOkButtonText("Check");
		//set background color on main panel and buttons panel of OKCancelDialog
		getOkButton().getParent().setBackground(Color.WHITE);
		getOkButton().getParent().getParent().setBackground(Color.WHITE);
		
		pack();
		setLocationRelativeTo(component);
		setMinimumSize(new Dimension(350, 350));
		setSize(new Dimension(400, 400));
		setVisible(true);
		setFocusable(true);

	}
	
	// getters
	public JRadioButton getCheckCurrent() {
		return checkCurrent;
	}

	public JRadioButton getCheckOtherFiles() {
		return checkOtherFiles;
	}

	public JCheckBox getCheckExternalLinksCBox() {
		return checkExternalLinksCBox;
	}

	public JCheckBox getCheckImagesCBox() {
		return checkImagesCBox;
	}

	public JCheckBox getCheckInternalLinksCbox() {
		return checkInternalLinksCbox;
	}
	
	public TablePanelCreator getTablePanelCreator(){
		return tablePanelCreater;
	}

	/**
	 * Initialize GUI
	 */
	private void initGUI() {

		JPanel mainPanel = new JPanel(new GridBagLayout());

		mainPanel.setBackground(Color.WHITE);

		// Constrains for GridBagLayout manager.
		GridBagConstraints gbc = new GridBagConstraints();

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(checkCurrent);
		group.add(checkOtherFiles);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		mainPanel.add(new JLabel("Select files for check:"), gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 10, 0, 0);
		checkCurrent.setBackground(Color.WHITE);
		checkCurrent.setSelected(true);
		mainPanel.add(checkCurrent, gbc);

		gbc.gridy++;
		checkOtherFiles.setBackground(Color.WHITE);
		mainPanel.add(checkOtherFiles, gbc);

		gbc.gridy++;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 30, 10, 5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(tablePanelCreater.create(), gbc);

		gbc.gridy++;
		gbc.weighty = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 0);
		// mainPanel.add(new JSeparator(JSeparator.HORIZONTAL), gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 10, 0, 0);
		checkExternalLinksCBox.setBackground(Color.WHITE);
		checkExternalLinksCBox.setSelected(true);
		mainPanel.add(checkExternalLinksCBox, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(5, 10, 0, 0);
		checkImagesCBox.setBackground(Color.WHITE);
		checkImagesCBox.setSelected(true);
		mainPanel.add(checkImagesCBox, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(5, 10, 10, 0);
		checkInternalLinksCbox.setBackground(Color.WHITE);
		checkInternalLinksCbox.setSelected(true);
		mainPanel.add(checkInternalLinksCbox, gbc);

		getContentPane().add(mainPanel);
	}

	/**
	 * ActionListener for add button
	 */
	ActionListener addBtnAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			File[] files = fileChooser.createFileChooser();
			if (files != null) {
				DefaultTableModel tableModel = tablePanelCreater.getTableModel();
				for (int i = 0; i < files.length; i++) {
					tableModel.addRow(new String[] { files[i].toString() });

				}
			}
		}
	};

	/**
	 * Action listener for checkCurrent checkBox
	 */
	ActionListener checkCurrentAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			tablePanelCreater.getTableFiles().clearSelection();
			tablePanelCreater.getAddBtn().setEnabled(false);
			tablePanelCreater.getRemvBtn().setEnabled(false);
		}
	};

	/**
	 * Action listener for checkOtherFiles checkBox
	 */
	ActionListener checkOtherAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			tablePanelCreater.getAddBtn().setEnabled(true);
		}
	};


	/**
	 * ActionListener for check Button
	 */
	ActionListener checkBtnAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			List<String> listUrl = new ArrayList<String>();

				if (checkCurrent.isSelected()) {
					JOptionPane.showMessageDialog(view, currentUrl, "", JOptionPane.WARNING_MESSAGE);
					
					// clear last reported problems
					problemReporter.clearReportedProblems();

					listUrl.add(currentUrl);
					System.out.println("**checkCUrrent: "+currentUrl);
					worker = new Worker(listUrl, new SettingsImpl(view), parserCreator, problemReporter, statusReporter);

					worker.execute();

					
					view.setVisible(false);
					view.dispose();
				} 
				else {
					DefaultTableModel tableModel = tablePanelCreater.getTableModel();

					for (int i = 0; i < tableModel.getRowCount(); i++) {
						listUrl.add(""+tableModel.getValueAt(i, 0) );
					}
					System.out.println("**check table");
					if (!listUrl.isEmpty()) {
						
						System.out.println("**tableList: "+listUrl.toString());
						// clear last reported problems
						problemReporter.clearReportedProblems();
						worker = new Worker(listUrl, new SettingsImpl(view), parserCreator, problemReporter, statusReporter);

						worker.execute();
						view.setVisible(false);
						view.dispose();
					}
					else {
						JOptionPane.showMessageDialog(view, "List with files is empty.", "", JOptionPane.WARNING_MESSAGE);
					}

				}
				contentPersister.saveContent(view);
		}
	};

	/**
	 * Action listener for table remove button
	 */
	ActionListener removeBtnAction = new ActionListener() {
		JTable table = tablePanelCreater.getTableFiles();
		DefaultTableModel model = tablePanelCreater.getTableModel();

		@Override
		public void actionPerformed(ActionEvent e) {
			int index0 = table.getSelectionModel().getMinSelectionIndex();
			int index1 = table.getSelectionModel().getMaxSelectionIndex();

			for (int i = index1; i >= index0; i--) {
				int modelRow = table.convertRowIndexToModel(i);
				model.removeRow(modelRow);
			}
		}
	};
}
