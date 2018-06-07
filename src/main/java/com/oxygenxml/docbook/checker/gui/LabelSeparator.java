package com.oxygenxml.docbook.checker.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import ro.sync.exml.view.graphics.Font;

/**
 * A separator with a bold label.
 * @author cosmin_duna
 *
 */
public class LabelSeparator extends JPanel{

	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param title The title of the label.
	 */
	public LabelSeparator(String title) {
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(10, 0, 5, 5);
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		add(titleLabel, c);
		
		JSeparator sep = new JSeparator();
		c.gridx ++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.insets.right = 0;
		add(sep, c);

	}
}
