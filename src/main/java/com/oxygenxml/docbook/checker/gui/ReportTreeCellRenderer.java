package com.oxygenxml.docbook.checker.gui;

import java.awt.Component;
import java.awt.Font;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.oxygenxml.docbook.checker.hierarchy.report.HierarchyReportStorageTreeNodeId;
import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.parser.LinkType;

/**
 * Report tree cell render.
 * 
 * @author Cosmin Duna
 *
 */
public class ReportTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		label.setIcon(null);

		if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
			Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
			String text = "";
			String tooltip = "";
			Font labelFont = label.getFont().deriveFont(Font.PLAIN);

			if (userObject instanceof Link) {
				// if the node is a Link
				Link link = (Link) userObject;
				text = link.getRef();
				if (link.getLinkType() != LinkType.INTERNAL) {
					URL absoluteLocation = link.getAbsoluteLocation();
					if (absoluteLocation != null) {
						tooltip = absoluteLocation.toString();
					}else {
						tooltip = link.getRef();
					}
				}else {
					tooltip = link.getDocumentURL().toString();
				}
				labelFont = label.getFont().deriveFont(Font.ITALIC);
			}else if (userObject instanceof URL) {
				// if the node is a URL
				URL url = (URL) userObject;
				tooltip = url.toString();
				text = tooltip.substring(tooltip.lastIndexOf('/') + 1);
			}else if (userObject instanceof HierarchyReportStorageTreeNodeId) {
				// if the node is a HierarchyReportStorageTreeNodeId
				HierarchyReportStorageTreeNodeId nodeId = (HierarchyReportStorageTreeNodeId) userObject;
				tooltip = nodeId.getDocumentUrl().toString();
				text = tooltip.substring(tooltip.lastIndexOf('/') + 1) + " - " + nodeId.getConditionSet();
			}else {
			// else it's a String
				text = userObject.toString();
			}

			// set the text of label and the tooltip
			if (!text.isEmpty()) {
				label.setText(text);
			}else{
				label.setText("Empty");
			}
			if (tooltip != null && !tooltip.isEmpty()) {
				label.setToolTipText(tooltip);
			} else {
				label.setToolTipText(null);
			}
			label.setFont(labelFont);
		}
		return label;
	}

}
