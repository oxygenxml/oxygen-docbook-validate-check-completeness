package com.oxygenxml.docbook.checker.hierarchy.report;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.oxygenxml.docbook.checker.parser.Link;

/**
 * A tree data structure for store the information after parsing.
 * @author intern4
 *
 */
public class HierarchyReportStorageTreeNode {

	/**
	 * The URL of the parsed document.
	 */
	private URL documentURL;
	
	/**
	 * External links found.
	 */
	private List<Link> externalLink;
	
	/**
	 * Images found.
	 */
	private List<Link> images;
	
	/**
	 * Internal links found.
	 */
	private List<Link> internalLink;
	
	/**
	 * Parent node.
	 */
	private HierarchyReportStorageTreeNode parent;
	
	/**
	 * Xi-included Child 
	 */
	private	List<HierarchyReportStorageTreeNode> xiIncluded;
	
	/**
	 * Topics child.
	 */
	private List<HierarchyReportStorageTreeNode> topicsFiles;

	/**
	 * Constructor.
	 * @param documentUrl The URL of the document;
	 */
	public HierarchyReportStorageTreeNode(URL documentUrl) {
		this.documentURL = documentUrl;
		this.externalLink = new ArrayList<Link>();
		this.images = new ArrayList<Link>();
		this.internalLink = new ArrayList<Link>();
		xiIncluded = new ArrayList<HierarchyReportStorageTreeNode>();
		topicsFiles = new ArrayList<HierarchyReportStorageTreeNode>();
		parent = null;
	}
	
	// Getters
	public URL getDocumentURL() {
		return documentURL;
	}

	public List<Link> getExternalLink() {
		return externalLink;
	}

	public List<Link> getImages() {
		return images;
	}

	public List<Link> getInternalLink() {
		return internalLink;
	}

	public List<HierarchyReportStorageTreeNode> getXiIncluded(){
		return xiIncluded;
	}

	public List<HierarchyReportStorageTreeNode> getTopicsFiles() {
		return topicsFiles;
	}

	/**
	 * Add xi-include child
	 * @param xiIncluded The xi-include child
	 */
	public void addXiInclude(HierarchyReportStorageTreeNode xiIncluded) {
		xiIncluded.parent = this;
		this.xiIncluded.add(xiIncluded);
	}

	/**
	 * Add topic file child.
	 * @param topicFile The topic file.
	 */
	public void addTopicFile(HierarchyReportStorageTreeNode topicFile) {
		topicFile.parent = this;
		this.topicsFiles.add(topicFile);
	}
	
	/**
	 * Add external link.
	 * @param external The link
	 */
	public void addExternal(Link external) {
		this.externalLink.add( external);
	}
	
	/**
	 * Add image link
	 * @param image The image
	 */
	public void addImage(Link image) {
		this.images.add( image);
	}
	
	/**
	 * Add internal link.
	 * @param internal The link
	 */
	public void addInternal(Link internal) {
		this.internalLink.add( internal);
	}

	 
	/**
	 * Check if it contains a valid external links(external links that aren't empty).
	 * @return <code>true</code> if it contains a valid external link, <code>false</code> otherwise.
	 * 
	 */
	public boolean containsExternalValidLinks(){
		boolean toReturn = false;
		int size = externalLink.size();
		
		for (int i = 0; i < size; i++) {
			if(!externalLink.get(i).getRef().isEmpty()){
				toReturn = true;
			}
		}
		return toReturn;
	}

	/**
	 * Get the node according to given location stack. If the node doesn't exist,
	 * a new one is created and returned.
	 * 
	 * @param location
	 *          The stack with location.
	 * @return The node.
	 */
	public HierarchyReportStorageTreeNode getNode(Stack<URL> location) {
		// node to be return.
		HierarchyReportStorageTreeNode toReturn = this;

		// size of stack
		int size = location.size();

		if (size > 1) {
			
			// iterate over stack locations.
			URL anteriorLocation = location.get(0);
			URL currentLocation;
			for (int i = 1; i < size; i++) {
				currentLocation = location.get(i);
				
				//if was found a new location in stack
				if (!anteriorLocation.equals(currentLocation)) {
					anteriorLocation = currentLocation;
					
					//check if the new location is in tree.
					HierarchyReportStorageTreeNode aux = new HierarchyReportStorageTreeNode(currentLocation);
					int index = toReturn.xiIncluded.indexOf(aux);
					if (index == -1) {
						// the node doesn't exist
						//add the node in tree.
						toReturn.addXiInclude(aux);
						toReturn = aux;
					} else {
						//the node exist
						toReturn = toReturn.xiIncluded.get(index);
					}
				}
			}
		}

		return toReturn;
	}


	/**
	 * Add operation.
	 * @param toAdd Tree to be add.
	 * @return The result of add operation.
	 */
	public HierarchyReportStorageTreeNode add( HierarchyReportStorageTreeNode toAdd) {
		this.externalLink.addAll(toAdd.externalLink);
		this.images.addAll(toAdd.images);
		this.internalLink.addAll(toAdd.internalLink);
		this.xiIncluded.addAll(toAdd.xiIncluded);
		this.topicsFiles.addAll(toAdd.topicsFiles);
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.documentURL.equals( ((HierarchyReportStorageTreeNode)obj).documentURL);
	}

}
