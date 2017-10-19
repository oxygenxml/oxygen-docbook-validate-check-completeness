package com.oxygenxml.docbook.checker.gui;

import java.awt.Component;
import java.awt.Font;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.oxygenxml.docbook.checker.parser.Link;

/**
 * Report tree cell render.
 * @author intern4
 *
 */
public class ReportTreeCellRenderer extends DefaultTreeCellRenderer {
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus)
	{

		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		label.setIcon(null);
		
	    if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
	    	Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
	    	String text = "";
	    	String tooltip = "";
	    	Font labelFont = label.getFont().deriveFont(Font.PLAIN);
	    	
	    	//if the node is a Link
	    	if (userObject instanceof Link) {
	        Link link = (Link) userObject;
	        text = link.getRef();
	        tooltip = link.getAbsoluteLocation().toString();
	        labelFont = label.getFont().deriveFont(Font.ITALIC);
	    	}
	    	//if the node is a URL
	    	else if(userObject instanceof URL){
	    		URL url = (URL) userObject;
	    		tooltip = url.toString();
	    		text = tooltip.substring(tooltip.lastIndexOf("/")+1);
	    	}
	    	//if it's a String
	    	else if (userObject instanceof String){
	    		 text = (String) userObject;
	    	}
	    		
	    	//set the text of label and the tooltip
	    	if(!text.isEmpty()){
	    		label.setText(text);
	    		if(!tooltip.isEmpty()){
	    			label.setToolTipText(tooltip);
	    		}
	    		else{
	    			label.setToolTipText(null);
	    		}
	    	}
	    	label.setFont(labelFont);
	    }
	    return label;
	  }
	
}
