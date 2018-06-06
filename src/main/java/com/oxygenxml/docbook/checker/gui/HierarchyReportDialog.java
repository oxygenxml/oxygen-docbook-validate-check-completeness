package com.oxygenxml.docbook.checker.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;

import com.oxygenxml.docbook.checker.hierarchy.report.HtmlReportCreator;
import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.standalone.ui.OKCancelDialog;
import ro.sync.exml.workspace.api.standalone.ui.Tree;

/**
 * Dialog for report the resource hierarchy.
 * 
 * @author Cosmin Duna
 *
 */
public class HierarchyReportDialog extends OKCancelDialog {

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Text field for search
	 */
	private JTextField searchTextField;

	/**
	 * The tree.
	 */
	private Tree tree;

	
	/**
	 * Translator
	 */
	private transient Translator translator;

	/**
	 * Task for search
	 */
	private transient TimerTask inProgress = null;

	/**
	 * Timer.
	 */
	private transient Timer timer = new Timer(false);

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(HierarchyReportDialog.class);

	/**
	 * Constructor
	 * 
	 * @param translator
	 *          Translator.
	 * @param parentFrame
	 *          The parent frame;
	 * @param rootNode
	 *          The root node of tree.
	 */
	public HierarchyReportDialog(Translator translator, JFrame parentFrame, DefaultMutableTreeNode rootNode) {
		super(parentFrame, translator.getTranslation(Tags.HIERARCHY_REPORT_DIALOG_TITLE), false);

		this.translator = translator;
		tree = new Tree(new DefaultTreeModel(rootNode));
		ToolTipManager.sharedInstance().registerComponent(tree);
		searchTextField = new JTextField();

		// add a document listener on search text field
		searchTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				search();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				search();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				/// Do nothing because we should do search on insert and remove
			}
		});

		// set the cell render for tree.
		tree.setCellRenderer(new ReportTreeCellRenderer());

		initDialog();
		
		pack();
		this.setMinimumSize(getSize());
		this.setLocationRelativeTo(parentFrame);
		this.setResizable(true);
		this.setVisible(true);
	}

	/**
	 * Initialize the dialog.
	 */
	private void initDialog() {

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setPreferredSize(new Dimension(350, 250));
		
		// add a label
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 0, 10);
		panel.add(new JLabel(translator.getTranslation(Tags.QUICK_SEARCH_LABEL)), gbc);

		// add the search textField
		gbc.gridx++;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(searchTextField, gbc);

		// add the a scrollPane with the tree
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane, gbc);

		// configure tree.
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);

		// add mouse listener on tree
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				// get the selected path.
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());

				// when was count two clicks
				if (e.getClickCount() == 2 && selPath != null) {
					Object node = selPath.getLastPathComponent();
					node = ((DefaultMutableTreeNode) node).getUserObject();

					// if the node is instance of Link
					if (node instanceof Link) {
						Link link = (Link) node;
						try {
							// open the page in editor
							PluginWorkspaceProvider.getPluginWorkspace().open(
									new URL(link.getDocumentURL().toString() + "#line=" + link.getLine() + "column=" + link.getColumn()));
						} catch (MalformedURLException e1) {
							logger.debug(e1.getMessage(), e1);
						}

						// if the node is instance of URL
					} else if (node instanceof URL) {
						URL url = (URL) node;
						// open the corresponding page in editor.
						PluginWorkspaceProvider.getPluginWorkspace().open(url);
					}
				}
			}
		});

		this.add(panel);
		this.setOkButtonText(translator.getTranslation(Tags.EXPORT_HTML_REPORT_BUTTON));
	}

	/**
	 * OK(Save report) button was pressed.
	 */
	@Override
	protected void doOK() {

		// open a file chooser
		File outputFile = PluginWorkspaceProvider.getPluginWorkspace().chooseFile("Save as", new String[] { "html" }, "",
				true);

		HtmlReportCreator htmlReportCreator = new HtmlReportCreator();
		String content = htmlReportCreator.convertToHtml((DefaultMutableTreeNode) tree.getModel().getRoot(), outputFile);


		super.doOK();
		
		try {
			htmlReportCreator.prettifyAndPrintHtml(content, outputFile);
			PluginWorkspaceProvider.getPluginWorkspace().openInExternalApplication(outputFile.toURI().toURL(), false);
		} catch (TransformerException e) {
			logger.debug(e.getMessage(), e);

			//retry to print.
			OutputStream outputStream = null;
			OutputStreamWriter writer = null;
			try {
				outputStream = new FileOutputStream(outputFile);
				writer = new OutputStreamWriter(outputStream, "UTF-8");
				writer.write(content);
				PluginWorkspaceProvider.getPluginWorkspace().openInExternalApplication(outputFile.toURI().toURL(), false);
				
			} catch (IOException e2) {
				logger.debug(e.getMessage(), e);
			} finally {
				try {
					if(writer != null){
						writer.close();
					}
					else{
						if(outputStream != null){
							outputStream.close();
						}
					}
				} catch (IOException e1) {
					logger.debug(e1.getMessage(), e1);
				}
			}

		} catch (MalformedURLException e) {
			logger.debug(e.getMessage(), e);
		}

	}

	/**
	 * Start the search.
	 */
	private void search() {
		if (inProgress != null) {
			inProgress.cancel();
		}

		inProgress = new TimerTask() {

			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						actualSearch();
					}
				});
			}
		};

		timer.schedule(inProgress, 500);
	}

	/**
	 * Do the search operation.
	 */
	private void actualSearch() {
		// the treePaths that was found
		List<TreePath> foundTreePaths = new ArrayList<TreePath>();
		// the text from searchTextField
		String searchedText = searchTextField.getText();

		if (!searchedText.isEmpty()) {

			// traverse the tree
			@SuppressWarnings("unchecked")
			Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode) tree.getModel().getRoot())
					.depthFirstEnumeration();
			while (e.hasMoreElements()) {
				DefaultMutableTreeNode currentNode = e.nextElement();

				//check if node contains searchedText
				TreePath nodeTreePath = nodeContains(currentNode, searchedText);
				
				if(nodeTreePath != null){
					foundTreePaths.add(nodeTreePath);
				}
			}

			// select the found treePaths
			int size = foundTreePaths.size();
			TreePath[] treePathsVector = null;
			if(size > 0) {
				treePathsVector =  new TreePath[size];
				treePathsVector = foundTreePaths.toArray(treePathsVector);
			}
			collapseAllTree();
			tree.getSelectionModel().clearSelection();
			tree.scrollRowToVisible(0);
			if(treePathsVector != null) {
				tree.scrollPathToVisible(treePathsVector[0]);
				tree.getSelectionModel().addSelectionPaths(treePathsVector);
			}

		} else {
			// clear the selection
			tree.scrollRowToVisible(0);
			tree.getSelectionModel().clearSelection();
		}
	}

	
	/**
	 * Check if the given node contains the given text.
	 * @param node The node.
	 * @param text The text.
	 * @return The TreePath of node if the node contains the given text, or <code>null</code>if node doesn't contains.
	 */
	private TreePath nodeContains(DefaultMutableTreeNode node, String text){
		TreePath toReturn = null;
		
		// check the current node
		if (node.getUserObject() instanceof Link) {
			//get the link
			Link link = (Link) node.getUserObject();
			URL absoluteLocation = link.getAbsoluteLocation();
			if(absoluteLocation != null){
				if (absoluteLocation.toString().contains(text)) {
					toReturn = new TreePath(node.getPath());
				}
			}else if (link.getRef().contains(text)) {
					toReturn = new TreePath(node.getPath());
				}
			
		}else if (node.getUserObject() instanceof URL) {
			//get the URL
			String stringUrl = node.getUserObject().toString();

			if (stringUrl.contains(text)) {
				toReturn = new TreePath(node.getPath());
			}

		} else if (node.toString().contains(text)) {
			toReturn = new TreePath(node.getPath());
		}
		
		return toReturn;
	}
	
	/**
	 * Collapse all tree.
	 */
	private void collapseAllTree() {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		collapseAll(tree, new TreePath(root));
		tree.expandPath(new TreePath(root));
	}

	/**
	 * Collapse the given tree.
	 * 
	 * @param tree
	 *          The tree to be collapse.
	 * @param parent
	 *          The parent.
	 */
	private void collapseAll(Tree tree, TreePath parent) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				collapseAll(tree, path);
			}
		}
		tree.collapsePath(parent);
	}
}
