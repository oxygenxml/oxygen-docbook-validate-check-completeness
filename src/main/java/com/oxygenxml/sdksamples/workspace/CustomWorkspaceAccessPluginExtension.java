package com.oxygenxml.sdksamples.workspace;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.oxygenxml.docbookChecker.DocBookCheckerOxygen;

import ro.sync.exml.editor.EditorPageConstants;
import ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.listeners.WSEditorChangeListener;
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

		// You can access the content inside each opened WSEditor depending on the
		// current editing page (Text/Grid or Author).
		// A sample action which will be mounted on the main menu, toolbar and
		// contextual menu.
		final Action checkerDocBook = createCheckCurrentPanel(pluginWorkspaceAccess);

//		// Create your own main menu and add it to Oxygen 
//		pluginWorkspaceAccess.addMenuBarCustomizer(new MenuBarCustomizer() {
//			/**
//			 * @see ro.sync.exml.workspace.api.standalone.MenuBarCustomizer#customizeMainMenu(javax.swing.JMenuBar)
//			 */
//			@Override
//			public void customizeMainMenu(JMenuBar mainMenuBar) {
//				JMenu myMenu = new JMenu("DocBook");
//				myMenu.add(checkerCurrent);
//
//				// Add your menu before the Help menu
//				mainMenuBar.add(myMenu, mainMenuBar.getMenuCount() - 1);
//			}
//		});

		pluginWorkspaceAccess.addEditorChangeListener(new WSEditorChangeListener() {
			@Override
			public boolean editorAboutToBeOpenedVeto(URL editorLocation) {
				// You can reject here the opening of an URL if you want
				return true;
			}

			@Override
			public void editorOpened(URL editorLocation) {
				checkActionsStatus(editorLocation);
			}

			// Check actions status
			private void checkActionsStatus(URL editorLocation) {
				WSEditor editorAccess = pluginWorkspaceAccess
						.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA);

				if (editorAccess != null) {
					checkerDocBook.setEnabled((EditorPageConstants.PAGE_AUTHOR.equals(editorAccess.getCurrentPageID())	
							|| EditorPageConstants.PAGE_TEXT.equals(editorAccess.getCurrentPageID())) );
				}
			}

			@Override
			public void editorClosed(URL editorLocation) {
				// An edited XML document has been closed.
			}

			/**
			 * @see ro.sync.exml.workspace.api.listeners.WSEditorChangeListener#editorAboutToBeClosed(java.net.URL)
			 */
			@Override
			public boolean editorAboutToBeClosed(URL editorLocation) {
				// You can veto the closing of an XML document.
				// Allow close
				return true;
			}

			/**
			 * The editor was relocated (Save as was called).
			 * 
			 * @see ro.sync.exml.workspace.api.listeners.WSEditorChangeListener#editorRelocated(java.net.URL,
			 *      java.net.URL)
			 */
			@Override
			public void editorRelocated(URL previousEditorLocation, URL newEditorLocation) {
				//
			}

			@Override
			public void editorPageChanged(URL editorLocation) {
				checkActionsStatus(editorLocation);
			}

			@Override
			public void editorSelected(URL editorLocation) {
				checkActionsStatus(editorLocation);
			}

			@Override
			public void editorActivated(URL editorLocation) {
				checkActionsStatus(editorLocation);
			}
		}, StandalonePluginWorkspace.MAIN_EDITING_AREA);

		
		// You can use this callback to populate your custom toolbar (defined in
		// the plugin.xml) or to modify an existing Oxygen toolbar
		// (add components to it or remove them)
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

					// Add your own toolbar button using our
					// "ro.sync.exml.workspace.api.standalone.ui.ToolbarButton" API
					// component
					ToolbarButton customButton = new ToolbarButton(checkerDocBook, true);
					comps.add(customButton);
					toolbarInfo.setComponents(comps.toArray(new JComponent[0]));
				}
			}
		});

	}


	/**
	 * Create the Swing action for check current file.
	 * 
	 */
	private AbstractAction createCheckCurrentPanel(final StandalonePluginWorkspace pluginWorkspaceAccess) {
		return new AbstractAction("Checker DocBook") {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//get current file url
				WSEditor editorAccess = pluginWorkspaceAccess.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA);
				String url = editorAccess.getEditorLocation().toString();
				
				//open check frame
				DocBookCheckerOxygen docBookChecker = new DocBookCheckerOxygen(url,
						(JFrame) pluginWorkspaceAccess.getParentFrame());

			}
		};
	}

	/**
	 * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#applicationClosing()
	 */
	@Override
	public boolean applicationClosing() {
		// You can reject the application closing here
		return true;
	}

}