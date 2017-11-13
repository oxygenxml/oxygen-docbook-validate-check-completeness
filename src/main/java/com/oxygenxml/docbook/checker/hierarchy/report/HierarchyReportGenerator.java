package com.oxygenxml.docbook.checker.hierarchy.report;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

import com.oxygenxml.docbook.checker.parser.DocumentDetails;
import com.oxygenxml.docbook.checker.parser.Link;

/**
 * Generator for resource hierarchy report.
 * 
 * @author Cosmin Duna
 *
 */
public class HierarchyReportGenerator {

	/**
	 * Map with hierarchyReportStorageTreeNodeId(documentURL and conditionSetName) and hierarchyReportStorageTreeNode for generate resource
	 * hierarchy report.
	 */
	private final Map<HierarchyReportStorageTreeNodeId, HierarchyReportStorageTreeNode> hierarchyReportItems = new HashMap<HierarchyReportStorageTreeNodeId, HierarchyReportStorageTreeNode>();

	/**
	 * Add document details to be reported.
	 * 
	 * @param documentDetails
	 *          the document details
	 * @param documentURL
	 *          The URL of document.
	 * @param conditionSet The name of conditionSet.   
	 */
	public void addDocumentdetailsForReport(DocumentDetails documentDetails, URL documentURL, String conditionSet) {

		// convert the documentDetails in hierarchyReportTree (tree structure for
		// storage)
		HierarchyReportStorageTreeNode toAdd = convertToHierarchyReportStorageTree(documentDetails, documentURL);

		HierarchyReportStorageTreeNodeId nodeId = new HierarchyReportStorageTreeNodeId(documentURL, conditionSet);
		// check if hierarchyReportItem already exists
		HierarchyReportStorageTreeNode assemblyFileTree = hierarchyReportItems.get(nodeId);
		if (assemblyFileTree == null) {
			// put the hierarchyReportItem in map
			hierarchyReportItems.put(nodeId, toAdd);
		} else {
			// add the hierarchyReportItem in map
			assemblyFileTree.add(toAdd);
		}

	}

	/**
	 * Add topic documentDetails to be reported.
	 * 
	 * @param documentDetails
	 *          The document details.
	 * @param assemblyFileUrl
	 *          The URL of assembly file.
	 * @param topicFileUrl
	 *          The URL of topic file.
	 * @param conditionSet The name of conditionSet.         
	 */
	public void addTopicDocumentDetailsForReport(DocumentDetails documentDetails, URL assemblyFileUrl, URL topicFileUrl, String conditionSet){

		//create the hierarchyReportStorageTreeNodeId according to assemblyFileUrl and conditionSet
		HierarchyReportStorageTreeNodeId nodeId = new HierarchyReportStorageTreeNodeId(assemblyFileUrl, conditionSet);
		
		// get the hierarchyReportItem for assemblyFileUrl
		HierarchyReportStorageTreeNode assemblyFileTree = hierarchyReportItems.get(nodeId);

		if (assemblyFileTree == null) {
			// if the hierarchyReportItem doesn't exist, create one.
			assemblyFileTree = new HierarchyReportStorageTreeNode(assemblyFileUrl);
			hierarchyReportItems.put(nodeId, assemblyFileTree);
		}

		// convert the documentDetails in hierarchyReportTree (tree structure for
		// storage)
		HierarchyReportStorageTreeNode toAdd = convertToHierarchyReportStorageTree(documentDetails, topicFileUrl);

		// add the node in assembly file child.
		assemblyFileTree.addTopicFile(toAdd);
	}

	/**
	 * Convert the given documentDetails in hierarchyReportTree (tree structure for
	 * storage)
	 * 
	 * @param documentDetails The document details(object that contains all links).
	 * @param documentURL The URL of document.
	 * @return
	 */
	private HierarchyReportStorageTreeNode convertToHierarchyReportStorageTree(DocumentDetails documentDetails,
			URL documentURL) {

		HierarchyReportStorageTreeNode tree = new HierarchyReportStorageTreeNode(documentURL);

		HierarchyReportStorageTreeNode node;

		List<Link> currentLinkList;
		Link currentLink;
		Stack<URL> currentLocationStack;
		int size;

		//add xi-include document
		List<Stack<URL>> xiIncludeFiles = documentDetails.getXiIncludeFiles();
		size = xiIncludeFiles.size();
		for (int i = 0; i < size; i++) {
			tree.getNode(xiIncludeFiles.get(i));
		}
		
		// add external
		currentLinkList = documentDetails.getExternalLinks();

		size = currentLinkList.size();
		for (int j = 0; j < size; j++) {
			currentLink = currentLinkList.get(j);
			currentLocationStack = currentLink.getLocationStack();

			node = tree.getNode(currentLocationStack);
			node.addExternal(currentLink);

		}
		// add internal
		currentLinkList = documentDetails.getInternalLinks();

		size = currentLinkList.size();
		for (int j = 0; j < size; j++) {
			currentLink = currentLinkList.get(j);
			currentLocationStack = currentLink.getLocationStack();

			node = tree.getNode(currentLocationStack);
			node.addInternal(currentLink);

		}
		// add images
		currentLinkList = documentDetails.getImgLinks();

		size = currentLinkList.size();
		for (int j = 0; j < size; j++) {
			currentLink = currentLinkList.get(j);
			currentLocationStack = currentLink.getLocationStack();

			node = tree.getNode(currentLocationStack);
			node.addImage(currentLink);

		}

		return tree;
	}

	/**
	 * Create a extensive tree from all hierarchyReportItems( storage trees for
	 * document details).
	 * 
	 * @return The root node of tree.
	 */
	public DefaultMutableTreeNode getSwingTreeNode() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Resource Hierarchy Report");

		Iterator<HierarchyReportStorageTreeNodeId> iterator = hierarchyReportItems.keySet().iterator();

		while (iterator.hasNext()) {
			HierarchyReportStorageTreeNodeId hierarchyReportTreeNodeId = iterator.next();

			root.add(getSwingTreeNodePerItem(hierarchyReportItems.get(hierarchyReportTreeNodeId) , hierarchyReportTreeNodeId));
		}

		return root;
	}

	/**
	 * 
	 * Create a extensive tree from given hierarchyReportItem( storage tree for
	 * document detail).
	 * @param hierarchyItem  The hierarchyReportItem
	 * @param hierarchyItemId The hierarchyReportItemId
	 * @return The root node of tree.
	 */
	public DefaultMutableTreeNode getSwingTreeNodePerItem(HierarchyReportStorageTreeNode hierarchyItem, HierarchyReportStorageTreeNodeId hierarchyItemId) {
		
		//create the root according to conditions set
		DefaultMutableTreeNode root;
		if(hierarchyItemId.getConditionSet().isEmpty()){
			 root = new DefaultMutableTreeNode(hierarchyItem.getDocumentURL());
		}else{
			root = new DefaultMutableTreeNode(hierarchyItemId);
		}

		//external links node
		DefaultMutableTreeNode external = new DefaultMutableTreeNode("external links");
		//internal links node
		DefaultMutableTreeNode internal = new DefaultMutableTreeNode("internal links");
		//image links node
		DefaultMutableTreeNode images = new DefaultMutableTreeNode("images");
		//xi-includes file node
		DefaultMutableTreeNode xiInclude = new DefaultMutableTreeNode("xi-includes");
		//topic files node
		DefaultMutableTreeNode topic = new DefaultMutableTreeNode("topics");

		//create nodes for all external links and add in external links node.
		List<Link> externalList = hierarchyItem.getExternalLink();
		int externalSize = externalList.size();
		if (hierarchyItem.containsExternalValidLinks()) {
			root.add(external);
		}
		for (int i = 0; i < externalSize; i++) {
			Link currentExternal = externalList.get(i);
			if(!currentExternal.getRef().isEmpty()){
				external.add(new DefaultMutableTreeNode(externalList.get(i)));
			}
		}

		//create nodes for all internal links and add in internal links node.
		List<Link> internalList = hierarchyItem.getInternalLink();
		int internalSize = internalList.size();
		if (internalSize > 0) {
			root.add(internal);
		}
		for (int i = 0; i < internalSize; i++) {
			internal.add(new DefaultMutableTreeNode(internalList.get(i)));
		}

		//create nodes for all images and add in images node.
		List<Link> imageList = hierarchyItem.getImages();
		int imageSize = imageList.size();
		if (imageSize > 0) {
			root.add(images);
		}
		for (int i = 0; i < imageSize; i++) {
			images.add(new DefaultMutableTreeNode(imageList.get(i)));
		}

		//create tree for all xi-include files and add in xi-includes node.
		List<HierarchyReportStorageTreeNode> xiIncludeList = hierarchyItem.getXiIncluded();
		int xiIncludeSize = xiIncludeList.size();
		if (xiIncludeSize > 0) {
			root.add(xiInclude);
		}
		for (int i = 0; i < xiIncludeSize; i++) {
			xiInclude.add(getSwingTreeNodePerItem(xiIncludeList.get(i), new HierarchyReportStorageTreeNodeId(null, "")));
		}

		//create tree for all topics files and add in topics node.
		List<HierarchyReportStorageTreeNode> topicsList = hierarchyItem.getTopicsFiles();
		int topicsSize = topicsList.size();
		if (topicsSize > 0) {
			root.add(topic);
		}
		for (int i = 0; i < topicsSize; i++) {
			topic.add(getSwingTreeNodePerItem(topicsList.get(i),  new HierarchyReportStorageTreeNodeId(null, "")));
		}

		return root;
	}

}
