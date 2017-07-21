package com.oxygenxml.docbookChecker.reporters;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.oxygenxml.docbookChecker.view.CheckerFrame;
import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinkDetails;

/**
 * Update reported links in view
 * @author intern4
 *
 */
public class LinkReporterImpl implements LinksReporter{
	
	private CheckerFrame checkerFrame;
	
	/**
	 * Constructor
	 * @param view
	 */
	public LinkReporterImpl( CheckerFrame view) {
		this.checkerFrame = view;
	}

	
	
	@Override
	public void reportFinish(List<Link> results) {
		//TODO show invalid links in  oxygen 
		
	}
	
	
	


}
