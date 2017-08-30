package com.oxygenxml.docbook.checker.plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import com.oxygenxml.docbook.checker.proxy.ProjectPopupMenuCustomizerInvocationHandler;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

public class ProjectManagerEditor {

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
					new ProjectPopupMenuCustomizerInvocationHandler(checkerDocBook));

			// get the project manager object
			Object projectManager = getProjectManager.invoke(pluginWorkspaceAccess);

			// get the addPopUpMenuCustomizer method
			Method addPopUpMenuCustomizerMethod = projectManagerClass.getMethod("addPopUpMenuCustomizer",
					new Class[] { projectPopupMenuCustomizerClass });
			// invoke addPopUpMenuCustomizer method
			addPopUpMenuCustomizerMethod.invoke(projectManager, proxyProjectPopupMenuCustomizerImpl);

		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		} catch (IllegalArgumentException e2) {
			e2.printStackTrace();
		} catch (InvocationTargetException e2) {
			e2.printStackTrace();
		}
		// The method wasn't found because it's used a older version
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * For 19.1 oxygen version, get the selected .xml files in projectManager.
	 * 
	 * @param pluginWorkspaceAccess
	 * @return	If oxygen version is 19.1 return a list with URLs in String format, else return a empty list.
	 */
	public static List<String> getSelectedXmlFiles(StandalonePluginWorkspace pluginWorkspaceAccess) {
		List<String> toReturn = new ArrayList<String>();
		try {
			// get the getProjectManager method
			Method getProjectManager = pluginWorkspaceAccess.getClass().getMethod("getProjectManager");
			
			//get the projectManager class
			Class projectManagerClass = getProjectManager.getReturnType();

			//get the projectManager
			Object projectManager = getProjectManager.invoke(pluginWorkspaceAccess);

			//get the getSelectedFiles method
			Method getSelectedFiles = projectManagerClass.getMethod("getSelectedFiles");

			//get the selected files
			File[] selectedFiles = (File[]) getSelectedFiles.invoke(projectManager);

			//iterate over files
			int size = selectedFiles.length;
			for (int i = 0; i < size; i++) {
				//if current file is a directory, get the files from this
				if (selectedFiles[i].isDirectory()) {
					getAllXmlUrlFiles(selectedFiles[i], toReturn);
				} else {
					//add in return list only the xml files
					String fileName = selectedFiles[i].getPath();
					try {
						if (fileName.substring(fileName.lastIndexOf(".")).contains("xml")) {
							toReturn.add(fileName);
						}
					} catch (Exception e) {
					}
				}

			}

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
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
	private static void getAllXmlUrlFiles(File folder, List<String> listUrlFiles) {
		//get the files from folder
		File[] listOfFiles = folder.listFiles();

		//iterate over files 
		int size = listOfFiles.length;
		for (int i = 0; i < size; i++) {
			//check if is a file
			if (listOfFiles[i].isFile()) {
				String fileName = listOfFiles[i].getPath();
				//check if is a xml file
				if (fileName.substring(fileName.lastIndexOf(".")).contains("xml")) {
					listUrlFiles.add(fileName);
				}
				
				//check if is a directory
			} else if (listOfFiles[i].isDirectory()) {
				getAllXmlUrlFiles(listOfFiles[i], listUrlFiles);
			}
		}

	}
}
