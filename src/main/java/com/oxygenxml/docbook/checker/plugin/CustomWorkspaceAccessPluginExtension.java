package com.oxygenxml.docbook.checker.plugin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.oxygenxml.docbook.checker.DocBookCheckerOxygen;
import com.oxygenxml.docbook.checker.OxygenInteractor;
import com.oxygenxml.docbook.checker.OxygenSourceDescription;
import com.oxygenxml.docbook.checker.gui.Images;
import com.oxygenxml.docbook.checker.proxy.ProjectPopupMenuCustomizerInvocationHandler;
import com.oxygenxml.docbook.checker.translator.OxygenTranslator;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.page.text.WSTextEditorPage;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.standalone.ToolbarComponentsCustomizer;
import ro.sync.exml.workspace.api.standalone.ToolbarInfo;
import ro.sync.exml.workspace.api.standalone.actions.MenusAndToolbarsContributorCustomizer;
import ro.sync.exml.workspace.api.standalone.ui.ToolbarButton;

/**
 * Plugin extension - workspace access extension.
 */
public class CustomWorkspaceAccessPluginExtension implements WorkspaceAccessPluginExtension, OxygenInteractor {

	private OxygenSourceDescription sourceDescription = new OxygenSourceDescription();
	private JMenuItem documentMenuItem = new JMenuItem();

	private ToolbarButton toolbarButton;

	/**
	 * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#applicationStarted(ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace)
	 */
	@Override
	public void applicationStarted(final StandalonePluginWorkspace pluginWorkspaceAccess) {

		// A action which will be mounted on the toolbar.
		final Action checkerDocBook = createCheckerDialog(pluginWorkspaceAccess);

		// Mount the action on the contextual menus for the Text and Author modes.
		pluginWorkspaceAccess.addMenusAndToolbarsContributorCustomizer(new MenusAndToolbarsContributorCustomizer() {

			// Get the image for JMenuItem button
			URL imageToLoad = getClass().getClassLoader().getResource(Images.CONTEXTUAL_ICON);
			
			/**
			 * Customize the author popUp menu.
			 */
			@Override
			public void customizeAuthorPopUpMenu(JPopupMenu popUp, AuthorAccess authorAccess) {
				addMenuItem(popUp, checkerDocBook, imageToLoad);
			}

			/**
			 * Customize the text popUp menu.
			 */
			@Override
			public void customizeTextPopUpMenu(JPopupMenu popUp, WSTextEditorPage textPage) {
				addMenuItem(popUp, checkerDocBook, imageToLoad);
			}
		});

		// It is used to populate an existing Oxygen toolbar.
		pluginWorkspaceAccess.addToolbarComponentsCustomizer(new ToolbarComponentsCustomizer() {
			/**
			 * @see ro.sync.exml.workspace.api.standalone.ToolbarComponentsCustomizer#customizeToolbar(ro.sync.exml.workspace.api.standalone.ToolbarInfo)
			 */
			public void customizeToolbar(ToolbarInfo toolbarInfo) {
				// The toolBar ID is defined in the "plugin.xml"
				if ("DocBookValidationToolbar".equals(toolbarInfo.getToolbarID())) {
					List<JComponent> comps = new ArrayList<JComponent>();
					JComponent[] initialComponents = toolbarInfo.getComponents();
					boolean hasInitialComponents = initialComponents != null && initialComponents.length > 0;

					if (hasInitialComponents) {
						// Add initial toolbar components
						for (JComponent toolbarItem : initialComponents) {
							comps.add(toolbarItem);
						}
					}

					// Add a toolbar button using
					// "ro.sync.exml.workspace.api.standalone.ui.ToolbarButton" API
					// component
					toolbarButton = new ToolbarButton(checkerDocBook, true);

					// Get the image for toolbar button
					URL imageToLoad = getClass().getClassLoader().getResource(Images.TOOLBAR_ICON);
					if (imageToLoad != null) {
						toolbarButton.setText("");
						toolbarButton.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
					}

					comps.add(toolbarButton);
					toolbarInfo.setComponents(comps.toArray(new JComponent[0]));
				}
			}
		});

		
		//add a item with checkerDocBook action in contextual menu of ProjectManager
		ProjectManagerEditor.addPopUpMenuCustomizer(pluginWorkspaceAccess, checkerDocBook);

	}

	/**
	 * Create the Swing action for DocBook checker.
	 * 
	 */
	private AbstractAction createCheckerDialog(final StandalonePluginWorkspace pluginWorkspaceAccess) {

		Translator translator = new OxygenTranslator();
		return new AbstractAction(translator.getTranslation(Tags.ICON_HINT)) {

			@Override
			public void actionPerformed(ActionEvent e) {

				WSEditor editorAccess = pluginWorkspaceAccess
						.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA);

				if (editorAccess != null) {
					sourceDescription.setCurrentUrl(editorAccess.getEditorLocation().toString());
				} else {
					sourceDescription.setCurrentUrl(null);
				}

				Object source = e.getSource();
				if (source instanceof JMenuItem) {
					if (((JMenuItem) source).equals(documentMenuItem)) {
						// set the source of action
						sourceDescription.setSource(OxygenSourceDescription.CONTEXTUAL);
					} else {
						// set selected files
						sourceDescription.setSelectedFilesInProject(ProjectManagerEditor.getSelectedXmlFiles(pluginWorkspaceAccess));

						// set the source of action
						sourceDescription.setSource(OxygenSourceDescription.PROJECT_MANAGER);
					}
				} else if (source instanceof ToolbarButton) {
					// set the source of action
					sourceDescription.setSource(OxygenSourceDescription.TOOLBAR);
				}

				// open check frame
				DocBookCheckerOxygen docBookChecker = new DocBookCheckerOxygen(sourceDescription,
						CustomWorkspaceAccessPluginExtension.this, (JFrame) pluginWorkspaceAccess.getParentFrame());

			}
		};
	}

	
	/**
	 * Add documentMenuItem at the given JPopupMenu.
	 * @param popUp	The JPopupMenu.
	 * @param checkerDocBook The action to set on documentMenuItem.
	 * @param imageToLoad 	The Url of image to set on documentMenuItem.
	 */
	private void addMenuItem(JPopupMenu popUp, Action checkerDocBook, URL imageToLoad ){
		//set action on MenuItem
		documentMenuItem.setAction(checkerDocBook);
		
		//set menuItem color
		documentMenuItem.setOpaque(true);
		documentMenuItem.setBackground(Color.WHITE);
		
		//set icon on MenuItem
		if (imageToLoad != null) {
			documentMenuItem.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
		}
		//add menuItem at popupMenu
		popUp.add(documentMenuItem);
	}

	
	/**
	 * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#applicationClosing()
	 */
	@Override
	public boolean applicationClosing() {
		return true;
	}

	@Override
	public void setButtonsEnable(boolean state) {
		documentMenuItem.setEnabled(state);
		toolbarButton.setEnabled(state);
	}

	
	
}