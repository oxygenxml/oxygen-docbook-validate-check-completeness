package com.oxygenxml.docbook.checker.proxy;

import java.lang.reflect.Method;
import java.net.URL;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;

import com.oxygenxml.docbook.checker.resources.Images;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

/**
 * InvocationHandler for ProjectPopupMenuCustomizer
 * @author intern4
 *
 */
public class ProjectPopupMenuCustomizerInvocationHandler implements java.lang.reflect.InvocationHandler {
	/**
	 * The action that open the DocBook checker.
	 */
	private Action checkerDocBook;
	
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(ProjectPopupMenuCustomizerInvocationHandler.class);

	/**
	 * The action id of predecessor item.
	 */
	private static final String PREDECESSOR_ITEM_ACTION_ID = "Project/Compare";

	/**
	 * Plugin workspace access.
	 */
	private StandalonePluginWorkspace pluginWorkspaceAccess;
	
	/**
	 * Constructor
	 * @param pluginWorkspaceAccess Plugin workspace access.
	 * @param checkerDocBook The action that open the DocBook checker.
	 */
	public ProjectPopupMenuCustomizerInvocationHandler(StandalonePluginWorkspace pluginWorkspaceAccess, Action checkerDocBook) {
		this.pluginWorkspaceAccess = pluginWorkspaceAccess;
		this.checkerDocBook = checkerDocBook;
	}

	/**
	 * Processes a "customizePopUpMenu" method invocation on a proxy instance and returns the result.
	 */
	public Object invoke(Object proxy, Method method, Object[] args) {
		Object result = null;

		// if the method name equals with "customizePopUpMenu"
		if (method.getName().equals("customizePopUpMenu")) {
			// cast the args[0] at JPopupMenu
			JPopupMenu popupMenu = (JPopupMenu) args[0];

			// get the component count
			int size = popupMenu.getComponentCount();

			// get the index of predecessor item.
			int index = 0;
			for (index = 0; index < size; index++) {

				// get the current element
				Object currentElement = popupMenu.getComponent(index);

				if (currentElement instanceof JMenuItem) {
					JMenuItem item = (JMenuItem) currentElement;
					if (PREDECESSOR_ITEM_ACTION_ID.equals(pluginWorkspaceAccess.getOxygenActionID(item.getAction()))) {
						// the predecessor index was found.
						break;
					}
				}
			}

			// item to add in popupMenu
			JMenuItem projectMenuItem = new JMenuItem();

			// Get the image for checkerItem
			URL imageToLoad = getClass().getClassLoader().getResource(Images.CONTEXTUAL_ICON);

			// set action on MenuItem
			projectMenuItem.setAction(checkerDocBook);

			// set icon on MenuItem
			if (imageToLoad != null) {
				projectMenuItem.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
			}

			// add a separator
			popupMenu.addSeparator();

			// add menuItem at popupMenu
			popupMenu.add(projectMenuItem, index + 1);
		}

		return result;
	}
}