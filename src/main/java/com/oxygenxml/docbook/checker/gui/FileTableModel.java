package com.oxygenxml.docbook.checker.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbook.checker.reporters.OxygenProblemReporter;
import com.oxygenxml.docbook.checker.reporters.TabKeyGenerator;

/**
 * Model for table that contains files to be check.
 * @author Cosmin Duna
 *
 */
public class FileTableModel extends DefaultTableModel {
	
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Oxygen problem reporter. Used for report problems at the URL conversion.
	 */
	private transient OxygenProblemReporter oxygenProblemReporter = new OxygenProblemReporter();
	
	
	/**
	 * Constructor
	 * @param columnNames       <code>array</code> containing the names
   *                          of the new columns; if this is
   *                          <code>null</code> then the model has no columns
   * @param rowCount           the number of rows the table holds
	 */
	public FileTableModel(Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
	}


	/**
	 * Convert the given aValue in URL and set the at given row and column.
	 * @param aValue Value to be set.
	 * @param row the row whose value is to be changed
    * @param column the column whose value is to be changed 
	 */
	@Override
	public void setValueAt(Object aValue, int row, int column) {
		Object valueToSet = aValue;
		if(aValue instanceof String){
			try {
				valueToSet = new URL((String)aValue);
			} catch (MalformedURLException e) {
				try {
					valueToSet  = new File((String)aValue).toURI().toURL();
				} catch (MalformedURLException e1) {
					oxygenProblemReporter.reportException(e, TabKeyGenerator.generate());
				}
			}
		}
		else if(aValue instanceof File){
		  try {
				valueToSet = ((File)aValue).toURI().toURL();
			} catch (MalformedURLException e) {
				oxygenProblemReporter.reportException(e, TabKeyGenerator.generate());
			}
		}
		super.setValueAt(valueToSet, row, column);
	}
}
