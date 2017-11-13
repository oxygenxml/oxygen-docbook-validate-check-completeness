package com.oxygenxml.docbook.checker.plugin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.oxygenxml.docbook.checker.ApplicationInteractor;
import com.oxygenxml.docbook.checker.ApplicationSourceDescription;
import com.oxygenxml.docbook.checker.ApplicationSourceDescription.Source;
import com.oxygenxml.docbook.checker.gui.DocBookCheckerDialog;
import com.oxygenxml.docbook.checker.resources.Images;
import com.oxygenxml.docbook.checker.translator.OxygenTranslator;
import com.oxygenxml.docbook.checker.translator.Tags;
import com.oxygenxml.docbook.checker.translator.Translator;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension;
import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.documenttype.DocumentTypeInformation;
import ro.sync.exml.workspace.api.editor.page.text.WSTextEditorPage;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.standalone.ToolbarComponentsCustomizer;
import ro.sync.exml.workspace.api.standalone.ToolbarInfo;
import ro.sync.exml.workspace.api.standalone.actions.MenusAndToolbarsContributorCustomizer;
import ro.sync.exml.workspace.api.standalone.ui.ToolbarButton;

/**
 * Plugin extension - workspace access extension.
 */
public class CustomWorkspaceAccessPluginExtension implements WorkspaceAccessPluginExtension, ApplicationInteractor {

	/**
	 * A part of document type name. 
	 */
	private static final String DOCUMENT_NAME = "docbook";
	
	/**
	 * Application source description.
	 */
	private ApplicationSourceDescription sourceDescription = new ApplicationSourceDescription();
	
	/**
	 * Menu item
	 */
	private JMenuItem documentMenuItem = new JMenuItem();

	/**
	 * Toolbar button
	 */
	private ToolbarButton toolbarButton;

	/**
	 * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#applicationStarted(ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace)
	 */
	@Override
	public void applicationStarted(final StandalonePluginWorkspace pluginWorkspaceAccess) {

		// A action which will be mounted on the toolbar and in contextual menu.
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
				//get the documentTypeInformation
				DocumentTypeInformation documentTypeInformation = authorAccess.getEditorAccess().getParentEditor().getDocumentTypeInformation();
				//check the name
				if(documentTypeInformation != null && documentTypeInformation.getName().toLowerCase().contains(DOCUMENT_NAME)){
					addMenuItem(popUp, checkerDocBook, imageToLoad);
				}
			}

			/**
			 * Customize the text popUp menu.
			 */
			@Override
			public void customizeTextPopUpMenu(JPopupMenu popUp, WSTextEditorPage textPage) {
				//get the documentTypeInformation
				DocumentTypeInformation documentTypeInformation = textPage.getParentEditor().getDocumentTypeInformation();
				//check the name
				if(documentTypeInformation != null && documentTypeInformation.getName().toLowerCase().contains(DOCUMENT_NAME)){
					addMenuItem(popUp, checkerDocBook, imageToLoad);
				}
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

			@SuppressWarnings("unused")
			@Override
			public void actionPerformed(ActionEvent e) {

				WSEditor editorAccess = pluginWorkspaceAccess
						.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA);

				if (editorAccess != null) {
					sourceDescription.setCurrentUrl(editorAccess.getEditorLocation());
				} else {
					sourceDescription.setCurrentUrl(null);
				}

				Object source = e.getSource();
				if (source instanceof JMenuItem) {
					if (((JMenuItem) source).equals(documentMenuItem)) {
						// set the source of action
						sourceDescription.setSource(Source.CONTEXTUAL);
					} else {
						// set selected files
						sourceDescription.setSelectedFilesInProject(ProjectManagerEditor.getSelectedXmlFiles(pluginWorkspaceAccess));

						// set the source of action
						sourceDescription.setSource(Source.PROJECT_MANAGER);
					}
				} else if (source instanceof ToolbarButton) {
					// set the source of action
					sourceDescription.setSource(Source.TOOLBAR);
				}

				// open check frame
				DocBookCheckerDialog docBookCheckerDialog = new DocBookCheckerDialog(sourceDescription, CustomWorkspaceAccessPluginExtension.this, new OxygenTranslator());

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
		
		//add a separator
		popUp.addSeparator();
		
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

	/**
	 * Set enable the toolbat button and the menu item.
	 * @param <code>true</code> if set enable, <code>false</code> if set disable.
	 */
	@Override
	public void setOperationInProgress(boolean isOperationInProgress) {
		documentMenuItem.setEnabled(!isOperationInProgress);
		toolbarButton.setEnabled(!isOperationInProgress);
	}

	/**
	 * Get the parent main frame.
	 *@return the frame.
	 */
	@Override
	public JFrame getApplicationFrame() {
		return (JFrame)PluginWorkspaceProvider.getPluginWorkspace().getParentFrame();
	}

	
	
}