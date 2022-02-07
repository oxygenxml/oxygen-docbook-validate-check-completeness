package com.oxygenxml.docbook.checker.plugin;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oxygenxml.docbook.checker.proxy.ProjectPopupMenuCustomizerInvocationHandler;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.util.URLUtil;

public class ProjectManagerEditor {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProjectManagerEditor.class);

	private ProjectManagerEditor() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * For 19.1 oxygen version add a MenuItem with given action in contextual menu
	 * of project manager. For older version than 19.1 do nothing.
	 * 
	 * @param pluginWorkspaceAccess
	 *          The StandalonePluginWorkspace.
	 * @param checkerDocBook
	 *          The action
	 */
	public static void addPopUpMenuCustomizer(StandalonePluginWorkspace pluginWorkspaceAccess, Action checkerDocBook) {
		// try to get method from 19.1 version
		try {
			// get the getProjectManager method
			Method getProjectManager = pluginWorkspaceAccess.getClass().getMethod("getProjectManager");

			// get the projectManager class
			Class projectManagerClass = getProjectManager.getReturnType();

			// get the projectPopupMenuCustomizer interface
			Class projectPopupMenuCustomizerClass = Class
					.forName("ro.sync.exml.workspace.api.standalone.project.ProjectPopupMenuCustomizer");

			// create a ProxyInstance of projectPopupMenuCustomizer
			Object proxyProjectPopupMenuCustomizerImpl = Proxy.newProxyInstance(
					projectPopupMenuCustomizerClass.getClassLoader(), new Class[] { projectPopupMenuCustomizerClass },
					new ProjectPopupMenuCustomizerInvocationHandler(pluginWorkspaceAccess, checkerDocBook));

			// get the project manager object
			Object projectManager = getProjectManager.invoke(pluginWorkspaceAccess);

			// get the addPopUpMenuCustomizer method
			Method addPopUpMenuCustomizerMethod = projectManagerClass.getMethod("addPopUpMenuCustomizer",
					projectPopupMenuCustomizerClass);
			// invoke addPopUpMenuCustomizer method
			addPopUpMenuCustomizerMethod.invoke(projectManager, proxyProjectPopupMenuCustomizerImpl);

		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
	}

	/**
	 * For 19.1 oxygen version, get the selected .xml files in projectManager.
	 * 
	 * @param pluginWorkspaceAccess
	 * @return If oxygen version is 19.1 return a list with URLs in String format,
	 *         else return a empty list.
	 */
	public static List<URL> getSelectedXmlFiles(StandalonePluginWorkspace pluginWorkspaceAccess) {
		List<URL> toReturn = new ArrayList<URL>();
		try {
			// get the getProjectManager method
			Method getProjectManager = pluginWorkspaceAccess.getClass().getMethod("getProjectManager");

			// get the projectManager class
			Class projectManagerClass = getProjectManager.getReturnType();

			// get the projectManager
			Object projectManager = getProjectManager.invoke(pluginWorkspaceAccess);

			// get the getSelectedFiles method
			Method getSelectedFiles = projectManagerClass.getMethod("getSelectedFiles");

			// get the selected files
			File[] selectedFiles = (File[]) getSelectedFiles.invoke(projectManager);

			// iterate over files
			int size = selectedFiles.length;
			for (int i = 0; i < size; i++) {

				// if current file is a directory, get the files from this
				if (selectedFiles[i].isDirectory()) {
					getAllXmlUrlFiles(selectedFiles[i], toReturn);
				} else {
					URL fileUrl;
					fileUrl = URLUtil.correct(selectedFiles[i]);
					if (!PluginWorkspaceProvider.getPluginWorkspace().getUtilAccess().isUnhandledBinaryResourceURL(fileUrl)) {
						toReturn.add(fileUrl);
					}
				}

			}
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}

		return toReturn;
	}

	
	/**
	 * Get all urls of xml files for given folder and add in given list
	 * 
	 * @param folder
	 *          The file of folder.
	 * @param listUrlFiles
	 *          The list.
	 */
	private static void getAllXmlUrlFiles(File folder, List<URL> listUrlFiles) {
		// get the files from folder
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles != null) {

			// iterate over files
			int size = listOfFiles.length;
			for (int i = 0; i < size; i++) {
				// check if is a file
				if (listOfFiles[i].isFile()) {
					URL fileUrl;
					try {
						fileUrl = URLUtil.correct(listOfFiles[i]);
						if (!PluginWorkspaceProvider.getPluginWorkspace().getUtilAccess().isUnhandledBinaryResourceURL(fileUrl)) {
							listUrlFiles.add(fileUrl);
						}
					} catch (MalformedURLException e) {
						logger.debug(e.getMessage(), e);
					}
					// check if is a directory
				} else if (listOfFiles[i].isDirectory()) {
					getAllXmlUrlFiles(listOfFiles[i], listUrlFiles);
				}
			}
		}

	}
}
