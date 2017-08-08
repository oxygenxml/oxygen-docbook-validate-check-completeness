package com.oxygenxml.docbookChecker.view;

import java.awt.event.ActionListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Interface for access table from panel with table.
 * @author intern4
 *
 */
public interface TablePanelAccess {

	public JTable getTable();
	
	public DefaultTableModel getTableModel();
	
	public void addListenerOnAddBtn(ActionListener action);
	
	public void addListenerOnRemoveBtn(ActionListener action);

}
