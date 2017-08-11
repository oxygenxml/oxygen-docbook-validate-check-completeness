package com.oxygenxml.docbookChecker.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.oxygenxml.docbookChecker.translator.Tags;
import com.oxygenxml.docbookChecker.translator.Translator;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

import ro.sync.exml.workspace.api.standalone.ui.OKCancelDialog;
/**
 * Add conditions dialog creator
 * @author intern4
 *
 */
public class AddConditionsTreeDialog {
	
	
	private ProfilingPanelCreator profilingPanel;
	
	private ProfilingConditionsInformations profilingConditionsInformations = new ProfilingConditionsInformationsImpl();
	
	private Map<String, Set<String>> selectedConditions;
	
	private Translator translator;
	
	public AddConditionsTreeDialog( ProfilingPanelCreator profilingPanel, Translator translator, Map<String, Set<String>> selectedConditions ) {
		this.profilingPanel = profilingPanel;
		this.translator = translator;
		this.selectedConditions = selectedConditions;
	}
	
	/**
	 * Display the dialog.
	 * @param dialogTitle Dialog title
	 * @param expandNodes	<code>true</code> if nodes will be expanded
	 * @param parentComponent The parent component.
	 */
	public void display(String dialogTitle,
			boolean expandNodes , JFrame parentComponent /* ,Set<String> existentTableValues */) {
		
		OKCancelDialog dialog = new OKCancelDialog( parentComponent, "Add", true);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		JLabel documentTypeLb = new JLabel();
		
		String[] combBoxItems = {ProfilingConditionsInformations.DOCBOOK , ProfilingConditionsInformations.DOCBOOK4 , ProfilingConditionsInformations.DOCBOOK5};		
		JComboBox<String> combBoxDocumentTypes = new JComboBox<String>(combBoxItems);
		
		final JCheckBoxTree cbTree = new JCheckBoxTree();

		//add document type JLabel
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		documentTypeLb.setText("Document type: ");
		panel.add(documentTypeLb, gbc);
		
		//add comboBox
		gbc.gridx++;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		combBoxDocumentTypes.setOpaque(false);
		panel.add(combBoxDocumentTypes, gbc);
		
		//add the scrollPane with the tree
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(new JScrollPane(cbTree), gbc);

		
		// add action listener on OK button
		dialog.getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//map to add in table
				Map<String, Set<String>> toAdd = new HashMap<String, Set<String>>();
				
				//get the paths from checkBox tree
				TreePath[] paths = cbTree.getCheckedPaths();
				
				for (TreePath tp : paths) {

					if (tp.getPath().length == 3) {
						Set<String> value = new HashSet<String>();
						value.add(tp.getPath()[2].toString());
						if (toAdd.containsKey(tp.getPath()[1].toString())) {
							value.addAll(toAdd.get(tp.getPath()[1].toString()));
						}
						toAdd.put(tp.getPath()[1].toString(), value);

					}
				}
				//clear conditions table
				profilingPanel.clearTable();
				//add components in the conditionalTable
				profilingPanel.addInTable(toAdd);
			}
		});

		
		cbTree.setModel(profilingConditionsInformations.getProfileConditions(ProfilingConditionsInformations.DOCBOOK));
		cbTree.checkSubTrees(selectedConditions);
		
		combBoxDocumentTypes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 JComboBox<String> cb = (JComboBox<String>) e.getSource();
				 String docType = (String) cb.getSelectedItem();
				 cbTree.setModel(profilingConditionsInformations.getProfileConditions(docType));
				 cbTree.setShowsRootHandles(true);
				 cbTree.setRootVisible(true);
				 cbTree.setRootVisible(false);
			}
		});
		
		cbTree.setShowsRootHandles(true);
		cbTree.setRootVisible(false);
		
		if (expandNodes) {
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

}
