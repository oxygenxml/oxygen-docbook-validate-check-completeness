package com.oxygenxml.docbook.checker.checkboxtree;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * Overriding cell renderer by a class that ignores the original "selection" mechanism
 *  It decides how to show the nodes due to the checking-mechanism
 * @author Cosmin Duna
 *
 */
public class CheckBoxCellRenderer extends JPanel implements TreeCellRenderer {
	/**
	 * Node checkBox
	 */
	JCheckBox checkBox;
	
	/**
	 * Interactor with node from checkBoxTree.
	 */
	private transient CheckBoxTreeInteractor checkBoxTreeInteractor;

	/**
	 * Constructor
	 */
	 CheckBoxCellRenderer(CheckBoxTreeInteractor checkBoxTreeInteractor) {
		super();
		this.checkBoxTreeInteractor = checkBoxTreeInteractor;
		this.setLayout(new BorderLayout());
		
		checkBox = new JCheckBox();
		//add the checkBox
		add(checkBox, BorderLayout.CENTER);
		setOpaque(false);
	}

	/**
	 * Sets the value of the current tree cell to value. Selected, expanded, leaf, row, and hasFocus will be ignored.
	 * The component will be render according to the NodeState from nodesCheckingState map.
	 * @return the Component that the renderer uses to draw the node.   
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
	
		//get the model
		CheckBoxTreeModel model = ((CheckBoxTreeModel) tree.getModel());
		
		//get the treePath
		TreePath tp = model.getPath(value);

		// get the checkedNode state
		NodeState nodeState = checkBoxTreeInteractor.getNodeCheckingState().get(tp);
	
		if (nodeState != null) {
			//set the state of checkBox
			checkBox.setSelected(nodeState.isSelected());
			
			//set the text of checkBox
			if (value instanceof LeafNode) {
				checkBox.setText(((LeafNode) value).getValue());
			} else {
				checkBox.setText(String.valueOf(value));
			}
			
			//mark checkBox with warning
			if (nodeState.isWarning()) {
				String text = checkBox.getText();
				checkBox.setText("<HTML>" + text + "<font color=\"orange\">*</font></HTML>");
			} else {
				String text = checkBox.getText();
				if (text.contains("<font color=\"orange\">*")) {
					checkBox.setText(text.substring(text.indexOf("<HTML>") + 6, text.indexOf("<font")));
				}
			}
		}
		return this;
	}
}
