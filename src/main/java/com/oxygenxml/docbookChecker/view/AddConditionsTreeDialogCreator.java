package com.oxygenxml.docbookChecker.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.CheckBoxTreeSelectionModel;
import com.oxygenxml.docbookChecker.translator.Tags;
import com.oxygenxml.docbookChecker.translator.Translator;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

import ro.sync.exml.workspace.api.standalone.ui.OKCancelDialog;
import ro.sync.exml.workspace.api.standalone.ui.TreeCellRenderer;
/**
 * Add conditions dialog creator
 * @author intern4
 */
public class AddConditionsTreeDialogCreator extends OKCancelDialog {
	
	/**
	 * Profiling panel Creator
	 */
	private ProfilingPanelCreator profilingPanel;
	
	/**
	 * Object used for get the oxygen profile conditions.
	 */
	private ProfilingConditionsInformations profilingConditionsInformations = new ProfilingConditionsInformationsImpl();
	
	/**
	 * This conditions will be selected in Tree. 
	 */
	private LinkedHashMap<String, LinkedHashSet<String>> selectedConditions;
	
	/**
	 * Translator used for internationalization.
	 */
	private Translator translator;
	
	
	/**
	 * Constructor
	 * @param profilingPanel 
	 * @param translator
	 * @param selectedConditions
	 * @param parentComponent
	 */
	public AddConditionsTreeDialogCreator(ProfilingPanelCreator profilingPanel, Translator translator, LinkedHashMap<String, LinkedHashSet<String>> selectedConditions, 
			 JFrame parentComponent ) {
		super(parentComponent, translator.getTranslation(Tags.ADD_DIALOG_TITLE) , true);
		
		this.profilingPanel = profilingPanel;
		this.translator = translator;
		this.selectedConditions = selectedConditions;
	}
	
	/**
	 * Display the dialog.
	 * @param expandNodes	<code>true</code> if nodes will be expanded
	 */
	public void display(boolean expandNodes) {
		//the panel that will be displayed
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		//comboBox for select documentType
		String[] combBoxItems = {ProfilingConditionsInformations.DOCBOOK , ProfilingConditionsInformations.DOCBOOK4 , ProfilingConditionsInformations.DOCBOOK5};		
		final JComboBox<String> combBoxDocumentTypes = new JComboBox<String>(combBoxItems);
		
		//CheckBox Tree for select conditions
		final CheckBoxTree cbTree = new CheckBoxTree();
		
		cbTree.putClientProperty("JTree.lineStyle" , "None");

		cbTree.setCellRenderer(new TreeCellRenderer());
		
		//add document type JLabel
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JLabel documentTypeLb = new JLabel(translator.getTranslation(Tags.SELECT_DOCUMENT_TYPE));
		panel.add(documentTypeLb, gbc);
		
		//add comboBox
		gbc.gridx++;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		combBoxDocumentTypes.setOpaque(false);
		panel.add(combBoxDocumentTypes, gbc);
		
		//add the a scrollPane with the tree
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(new JScrollPane(cbTree), gbc);

		
		// add action listener on OK button
		this.getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//map to add in conditions table
				LinkedHashMap<String, LinkedHashSet<String>> toAdd = new LinkedHashMap<String, LinkedHashSet<String>>();
				
				//get the paths from checkBox tree
				 CheckBoxTreeSelectionModel selectionModel = cbTree.getCheckBoxTreeSelectionModel();
				
				 TreePath[] paths = selectionModel.getSelectionPaths();
				 
				//iterate over paths
				for (TreePath tp : paths) {
					System.out.println( tp.toString());
					//use only the path with leaf node( path with length 3)
					if (tp.getPath().length == 3) {
						LinkedHashSet<String> value = new LinkedHashSet<String>();
						
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

		
		cbTree.setModel(createTreeModel(profilingConditionsInformations
				.getProfileConditions(ProfilingConditionsInformations.DOCBOOK)));
		
		addSelectionInTree(cbTree);
		
		combBoxDocumentTypes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 String docType = (String) combBoxDocumentTypes.getSelectedItem();
				 cbTree.setModel( createTreeModel(profilingConditionsInformations.getProfileConditions(docType)) );
				 cbTree.setShowsRootHandles(true);
				 cbTree.setRootVisible(true);
				 cbTree.setRootVisible(false);
			}
		});
		
		cbTree.setShowsRootHandles(true);
		cbTree.setRootVisible(false);
		

		this.add(panel);
		this.setOkButtonText(translator.getTranslation(Tags.ADD_BUTTON_IN_DIALOGS));
		this.setSize(250, 400);
		this.setResizable(true);
		this.setVisible(true);
	}

	
	
	 /**
	 * Create a DefaultTreeModel with the result of ProfileConditionsFromDocsWorker and ConditionsWorker.
	 * @param result
	 * @return
	 */
	private DefaultTreeModel createTreeModel(Map<String, Set<String>> result){
		// create the root node
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Conditions");

			Iterator<String> itKeys = result.keySet().iterator();
			while (itKeys.hasNext()) {
				String key = itKeys.next();
				//create node
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(key);

				Iterator<String> itValues = result.get(key).iterator();
				while (itValues.hasNext()) {
					String value = itValues.next();
					DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(value);
					//add leafs at node
					node.add(leaf);
				}
				//add node at root node
				root.add(node);
			}
			
			return new DefaultTreeModel(root);
	}

	private void addSelectionInTree(CheckBoxTree tree){
		Iterator<String> iterKey = selectedConditions.keySet().iterator();
		CheckBoxTreeSelectionModel selectionModel = tree.getCheckBoxTreeSelectionModel(); 
		
		while(iterKey.hasNext()){
			String key = iterKey.next();
			
			Iterator<String> iterValue = selectedConditions.get(key).iterator();

			while(iterValue.hasNext()){
				String value = iterValue.next();
				TreePath path = new TreePath(new String[]{"Conditions", key, value});
				System.out.println(path.toString());
				try {
					
					selectionModel.setSelectionPath(path);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
