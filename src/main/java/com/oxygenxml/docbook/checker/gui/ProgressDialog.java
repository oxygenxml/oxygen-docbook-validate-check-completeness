package com.oxygenxml.docbook.checker.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

import ro.sync.ecss.extensions.commons.ui.OKCancelDialog;

/**
 * Progress dialog.
 * @author Cosmin Duna
 *
 */
public class ProgressDialog extends OKCancelDialog implements ProgressDialogInteractor {

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The progress bar.
	 */
	private JProgressBar progressBar;
	/**
	 * JLabel for notes.
	 */
	private JLabel noteLabel;
	
	/**
	 * Constructor
	 * @param parentFrame The parent frame.
	 * @param translator Translator
	 */
	public ProgressDialog(JFrame parentFrame, Translator translator) {
		super(parentFrame , translator.getTranslation(Tags.FRAME_TITLE), false);
		
		noteLabel = new JLabel();
		
		progressBar =  new JProgressBar();
		progressBar.setStringPainted(false);
		progressBar.setIndeterminate(true);
		
		JPanel panel = new JPanel(new GridBagLayout());
	
		GridBagConstraints gbc = new GridBagConstraints();
		
		// add a label with progress dialog message.
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(new JLabel(translator.getTranslation(Tags.PROGRESS_DIALOG_MESSAGE)), gbc);
		
		// add the progress bar
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 5, 0, 5);
		panel.add(progressBar, gbc);
		
		// add the label for notes
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 5, 10, 5);
		panel.add(noteLabel, gbc);
	
		add(panel);
		
		getOkButton().setVisible(false);
		pack();
		setMinimumSize(new Dimension(getSize().width + 150 , getSize().height + 30));		
		setLocationRelativeTo(parentFrame);
		
	}


	/**
	 * Set the given note in the label for notes.
	 */
	@Override
	public void setNote(final String note){
				noteLabel.setText("<html>"+ note + "</html>");
	}
	
	/**
	 * Do cancel.
	 */
	@Override
	public void close(){
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				doCancel();

			}
		});
	}
	
	/**
	 * Add the given action listener on cancel button
	 * @param actionListener The action listener.
	 */
	public void addCancelActionListener(ActionListener actionListener){
		getCancelButton().addActionListener(actionListener);
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				getCancelButton().doClick();
				super.windowClosing(e);
			}
		});
	}
}


