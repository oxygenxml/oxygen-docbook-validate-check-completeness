package com.oxygenxml.docbook.checker.plugin;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.oxygenxml.docbookChecker.DocBookCheckerOxygen;

import ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.standalone.ToolbarComponentsCustomizer;
import ro.sync.exml.workspace.api.standalone.ToolbarInfo;
import ro.sync.exml.workspace.api.standalone.ui.ToolbarButton;

/**
 * Plugin extension - workspace access extension.
 */
public class CustomWorkspaceAccessPluginExtension implements WorkspaceAccessPluginExtension {


	/**
	 * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#applicationStarted(ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace)
	 */
	@Override
	public void applicationStarted(final StandalonePluginWorkspace pluginWorkspaceAccess) {

		// A action which will be mounted on the toolbar.
		final Action checkerDocBook = createCheckerDialog(pluginWorkspaceAccess);

			
		// It is used to populate an existing Oxygen toolbar.
		pluginWorkspaceAccess.addToolbarComponentsCustomizer(new ToolbarComponentsCustomizer() {
			/**
			 * @see ro.sync.exml.workspace.api.standalone.ToolbarComponentsCustomizer#customizeToolbar(ro.sync.exml.workspace.api.standalone.ToolbarInfo)
			 */
			public void customizeToolbar(ToolbarInfo toolbarInfo) {
				// The toolbar ID is defined in the "plugin.xml"
				if ("SampleWorkspaceAccessToolbarID".equals(toolbarInfo.getToolbarID())) {
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
					ToolbarButton customButton = new ToolbarButton(checkerDocBook, true);
					
					// Get the image for toolbar button
					URL imageToLoad = getClass().getClassLoader().getResource("img/DocBookValidateAndCheck24.png");
					if (imageToLoad != null) {
						customButton.setText("");
						customButton.setIcon(ro.sync.ui.Icons.getIcon(imageToLoad.toString()));
					}
				
					comps.add(customButton);
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
		
		return new AbstractAction("DocBook Checker") {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//get current file url
				WSEditor editorAccess = pluginWorkspaceAccess.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA);
				String currentUrl;
				if(editorAccess != null){
					 currentUrl = editorAccess.getEditorLocation().toString();					
				}
				else{
					currentUrl = null;
				}
				
				//open check frame
				DocBookCheckerOxygen docBookChecker = new DocBookCheckerOxygen(currentUrl,
						(JFrame) pluginWorkspaceAccess.getParentFrame());

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

}