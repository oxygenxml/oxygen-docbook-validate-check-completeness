//package com.oxygenxml.docbookChecker.view;
//
//import java.awt.Frame;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.util.concurrent.Delayed;
//import java.util.concurrent.TimeUnit;
//
//import javax.swing.BoundedRangeModel;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JProgressBar;
//import javax.swing.ProgressMonitor;
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
//import javax.swing.event.ChangeListener;
//
//import com.oxygenxml.docbookChecker.translator.Tags;
//
//import ro.sync.exml.workspace.api.standalone.ui.OKCancelDialog;
//
//public class ProgressBarDialog extends OKCancelDialog implements ProgressBarUpdater{
//	
//	ProgressMonitor progressBar = new ProgressMonitor(null, "", "", 0, 0);
//	
//	public ProgressBarDialog(Frame parentFrame, String title, boolean modal) {
//		super(parentFrame, "title", modal);
//
//	}
//
//	public void create(){
//	
//			JPanel panel = new JPanel(new GridBagLayout());
//			
//			GridBagConstraints gbc =new GridBagConstraints();
//			
//			
//			
//			JLabel label = new JLabel();
//			label.setText("Operation in progress");
//			
//			gbc.gridx = 0; 
//			gbc.gridy = 0;
//			gbc.weightx = 1;
//			gbc.fill = GridBagConstraints.HORIZONTAL;
//			gbc.anchor = GridBagConstraints.CENTER;
//			panel.add(label, gbc);
//			
//			gbc.gridy++;
//			panel.add(progressBar, gbc);
//			
//			
//			add(panel);
//			pack();
//			setSize(300, 200);
//			setResizable(true);
//			setLocationRelativeTo(null);
//			setVisible(true);
//			setFocusable(true);
//	}
//	
//	@Override
//	public void reportFile(String file) {
//		progressBar.setNote(file);
//	}
//
//	public static void main(String[] args) {
//	
//		String message = "Description of Task";
//    String note = "subtask";
//    String title = "Task Title";
//    UIManager.put("ProgressMonitor.progressText", title);
//
//    int min = 0;
//    int max = 100;
//    JFrame component = new JFrame();
//    ProgressMonitor pm = new ProgressMonitor(component, message, note, min, max);
//
//    boolean cancelled = pm.isCanceled();
//    if (cancelled) {
//      System.out.println("Stop task");
//    } else {
//      pm.setProgress(100);
//      pm.setNote("New Note");
//    }
//		
//		
////		SwingUtilities.invokeLater(new Runnable() {
////	    public void run() {
////	    	progressBarDialog.create();
////	    }
////		});
////		
//	//	progressBarDialog.reportFile("bla bla ");
//		for(int i = 0; i < 100; i++){
//			try {
//				TimeUnit.SECONDS.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println(i);
//			pm.setNote("bla bla "+ i);
//		}
//}
//}