package com.oxygenxml.docbook.checker.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

import ro.sync.ecss.extensions.commons.ui.OKCancelDialog;

public class ProgressDialog extends OKCancelDialog implements ProgressDialogInteractor {


	private JProgressBar progressBar;
	private JLabel noteLabel;
	
	public ProgressDialog(JFrame parentFrame, Translator translator) {
		super(parentFrame , translator.getTranslation(Tags.FRAME_TITLE), true);
		
		noteLabel = new JLabel();
		
		progressBar =  new JProgressBar();
		progressBar.setStringPainted(false);
		progressBar.setIndeterminate(true);
		
		JPanel panel = new JPanel(new GridBagLayout());
	
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 15, 5, 15);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(new JLabel(translator.getTranslation(Tags.PROGRESS_DIALOG_MESSAGE)), gbc);
		
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 5, 0, 5);
		panel.add(progressBar, gbc);
		
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 5, 10, 5);
		panel.add(noteLabel, gbc);
	
		add(panel);
		
		getOkButton().setVisible(false);
		
		setSize(new Dimension(370, 150));
		setLocationRelativeTo(parentFrame);
		
	}

	
	@Override
	public void setNote(final String note){
				noteLabel.setText("<html>"+ note + "</html>");
	}
	
	@Override
	public void close(){
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				doCancel();

			}
		});
	}
	
	public void addCancelActionListener(ActionListener actionListener){
		getCancelButton().addActionListener(actionListener);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				getCancelButton().doClick();
				super.windowClosing(e);
			}
		});
	}
}


