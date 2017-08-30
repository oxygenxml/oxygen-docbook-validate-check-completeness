package com.oxygenxml.docbook.checker.proxy;

import java.lang.reflect.Method;
import java.net.URL;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.oxygenxml.docbook.checker.gui.Images;

/**
 * InvocationHandler for ProjectPopupMenuCustomizer
 * @author intern4
 *
 */
public class ProjectPopupMenuCustomizerInvocationHandler implements java.lang.reflect.InvocationHandler {
	private Action checkerDocBook;

	public ProjectPopupMenuCustomizerInvocationHandler(Action checkerDocBook) {
		this.checkerDocBook = checkerDocBook;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		try {
			
			// if the method name equals with "customizePopUpMenu"
			if (method.getName().equals("customizePopUpMenu")) {
				//cast the args[0] at JPopupMenu
				JPopupMenu popupMenu = (JPopupMenu) args[0];
				
				//item to add in popupMenu
				JMenuItem projectMenuItem = new JMenuItem();

				// Get the image for checkerItem 
				URL imageToLoad = getClass().getClassLoader().getResource(Images.CONTEXTUAL_ICON);

				//set action on MenuItem
				projectMenuItem.setAction(checkerDocBook);

				//set icon on MenuItem
				if (imageToLoad != null) {
					projectMenuItem.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
				}
				
				//add a separator
				popupMenu.addSeparator();

				//add menuItem at popupMenu
				popupMenu.add(projectMenuItem);
			}

		} catch (Exception e) {
		}
		return result;
	}
}