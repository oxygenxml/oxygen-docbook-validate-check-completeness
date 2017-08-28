package com.oxygenxml.docbook.checker.plugin;

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

import com.oxygenxml.docbook.checker.DocBookCheckerOxygen;
import com.oxygenxml.docbook.checker.OxygenInteractor;
import com.oxygenxml.docbook.checker.OxygenSourceDescription;
import com.oxygenxml.docbook.checker.gui.Images;
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
public class CustomWorkspaceAccessPluginExtension implements WorkspaceAccessPluginExtension, OxygenInteractor{

	private OxygenSourceDescription sourceDescription = new OxygenSourceDescription();
	private JMenuItem menuItem = new JMenuItem();
	
	private ToolbarButton toolbarButton ;
	
	
	
	/**
	 * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#applicationStarted(ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace)
	 */
	@Override
	public void applicationStarted(final StandalonePluginWorkspace pluginWorkspaceAccess) {

		// A action which will be mounted on the toolbar.
		final Action checkerDocBook = createCheckerDialog(pluginWorkspaceAccess);

		
		//Mount the action on the contextual menus for the Text and Author modes.
		pluginWorkspaceAccess.addMenusAndToolbarsContributorCustomizer(new MenusAndToolbarsContributorCustomizer() {
			
			// Get the image for JMenuItem button
			URL imageToLoad = getClass().getClassLoader().getResource(Images.CONTEXTUAL_ICON);
			
			/**
			 * Customize the author popUp menu.
			 */
			@Override
			public void customizeAuthorPopUpMenu(JPopupMenu popUp, AuthorAccess authorAccess) {
				
				menuItem.setAction(checkerDocBook);
				if (imageToLoad != null) {
					menuItem.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
				}
				popUp.add(menuItem);
			}
			

			/**
			 * Customize the text popUp menu.
			 */
			@Override
			public void customizeTextPopUpMenu(JPopupMenu popUp, WSTextEditorPage textPage) {
				menuItem.setAction(checkerDocBook);
				if (imageToLoad != null) {
					menuItem.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
				}
				popUp.add(menuItem);
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
				
				WSEditor editorAccess = pluginWorkspaceAccess.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA);

				if(editorAccess != null){
					sourceDescription.setCurrentUrl(editorAccess.getEditorLocation().toString());					
				}
				else{
					sourceDescription.setCurrentUrl(null);
				}
				
				
				if (e.getSource() instanceof JMenuItem ){
					//set the source of action
					sourceDescription.setSource(OxygenSourceDescription.CONTEXTUAL);
				}
				else if (e.getSource() instanceof ToolbarButton){
					sourceDescription.setSource(OxygenSourceDescription.TOOLBAR);
				}
				
				
				//open check frame
				DocBookCheckerOxygen docBookChecker = new DocBookCheckerOxygen(sourceDescription, CustomWorkspaceAccessPluginExtension.this
						,(JFrame) pluginWorkspaceAccess.getParentFrame());

			}
		};
	}

	/**
	 * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#applicationClosing()
	 */
	@Override
	public boolean applicationClosing() {
		return true;
	}
	
	@Override
	public void setButtonsEnable(boolean state){
		menuItem.setEnabled(state);
		toolbarButton.setEnabled(state);
	}

}