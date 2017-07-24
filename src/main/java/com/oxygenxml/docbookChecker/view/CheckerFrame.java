package com.oxygenxml.docbookChecker.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbookChecker.Settings;
import com.oxygenxml.docbookChecker.SettingsImpl;
import com.oxygenxml.docbookChecker.Worker;
import com.oxygenxml.docbookChecker.reporters.LinkReporterImpl;
import com.oxygenxml.docbookChecker.reporters.ProblemReporterImpl;
import com.oxygenxml.docbookChecker.reporters.ProblemReporterImplExtension;
import com.oxygenxml.ldocbookChecker.parser.PlainParserCreator;

/**
 * The GUI for Broken Links Checker
 * 
 * @author intern4
 *
 */
public class CheckerFrame extends JFrame {

	/**
	 * Check button
	 */
	private JButton checkBtn = new JButton("Check");

	/**
	 *  
	 */
	private JCheckBox externalLinksCBox = new JCheckBox("check external links");

	/**
	 * 
	 */
	private TablePanelCreator tablePanelCreater = new TablePanelCreator();

	private CheckerFrame view = this;

	/**
	 * The background worker
	 */
	private Worker worker;

	/**
	 * 
	 */
	private Settings settings = new SettingsImpl();
	
	/**
	 * Constructor
	 */
	public CheckerFrame(URL url) {
		initGUI();

		// add action listener on add button
		tablePanelCreater.addListenerOnAddBtn(addBtnAction);

		tablePanelCreater.addListenerOnRemoveBtn(removeBtnAction);

		if(url != null){
			String [] toAdd = {url.toString()};
			tablePanelCreater.getTableModel().addRow(toAdd);
		}
		
		// add listener on check/stop button
		checkBtn.addActionListener(checkBtnAction);
		pack();
		setMinimumSize(new Dimension(350, 300));
		setSize(new Dimension(400, 350));
		setVisible(true);
		setFocusable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	/**
	 * Initialize GUI
	 */
	private void initGUI() {

		JPanel mainPanel = new JPanel(new GridBagLayout());

		mainPanel.setBackground(Color.WHITE);

		// Constrains for GridBagLayout manager.
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 5, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(tablePanelCreater.create(), gbc);

		gbc.gridy++;
		gbc.weighty = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 0);
		mainPanel.add(new JSeparator(JSeparator.HORIZONTAL), gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 15, 10, 0);
		externalLinksCBox.setBackground(Color.WHITE);
		externalLinksCBox.setSelected(true);
		mainPanel.add(externalLinksCBox, gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 0, 10, 15);
		gbc.anchor = GridBagConstraints.EAST;
		mainPanel.add(checkBtn, gbc);

		this.getContentPane().add(mainPanel);
	}

	ActionListener addBtnAction = new ActionListener() {

	//	JFileChooser chooser = pluginWorkspaceAccess.chooseFiles(new File("\\test-samples"), "Choose files",new FileNameExtensionFilter("xml files (*.xml)", "xml") , "Choose");
		JFileChooser chooser = new JFileChooser();
		
		@Override
		public void actionPerformed(ActionEvent e) {

			chooser.setCurrentDirectory(new File("D:\\docbook-validate-check-completeness\\test-samples"));
			chooser.setMultiSelectionEnabled(true);
			chooser.setDialogTitle("Choose files");
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setApproveButtonText("Choose");

			chooser.addChoosableFileFilter(new FileNameExtensionFilter("xml files (*.xml)", "xml"));
			chooser.setAcceptAllFileFilterUsed(false);

			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

				File[] files = chooser.getSelectedFiles();

				DefaultTableModel tableModel = tablePanelCreater.getTableModel();
				for (int i = 0; i < files.length; i++) {
					System.out.print(files[i]);
					String[] toAdd = { files[i].toString() };
					tableModel.addRow(toAdd);
				}

				if (tableModel.getRowCount() != 0) {
					checkBtn.setEnabled(true);
				}
			}
		}
	};

	ActionListener checkBtnAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			List<URL> listUrl = new ArrayList<URL>();

			try {
				DefaultTableModel tableModel = tablePanelCreater.getTableModel();

				for (int i = 0; i < tableModel.getRowCount(); i++) {
					listUrl.add(new URL("file:\\" + tableModel.getValueAt(i, 0)));
				}

				if (!listUrl.isEmpty()) {
					settings.setCheckExternal(externalLinksCBox.isSelected());
					worker = new Worker(listUrl, settings, new LinkReporterImpl(view), new PlainParserCreator(), new ProblemReporterImplExtension());

					worker.execute();
					view.setVisible(false);
					view.dispose();

				}
			} catch (MalformedURLException e1) {

			}
		}
	};

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

			if (model.getRowCount() == 0) {
				tablePanelCreater.getRemvBtn().setEnabled(false);
				checkBtn.setEnabled(false);
			}
		}
	};
}
