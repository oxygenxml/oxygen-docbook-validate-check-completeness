package com.oxygenxml.docbook.checker.checkboxtree;

/**
 * The state of a node.
 * It totally replaces the "selection" mechanism of the JTree
 * @author intern4
 *
 */
public class NodeState {
	/**
	 * Node is selected
	 */
	private boolean isSelected;
	
	/**
	 * Node has children.
	 */
	private boolean hasChildren;
	
	/**
	 * All children are selected.
	 */
	private boolean allChildrenSelected;
	
	/**
	 * Node mark with warning.
	 */
	private boolean isWarning;

	/**
	 * Constructor.
	 * @param isSelected_ <code>true</code> node is selected, <code>false</code> otherwise
	 * @param hasChildren_ <code>true</code> node has children, <code>false</code> otherwise
	 * @param allChildrenSelected_ <code>true</code> all children of node are selected, <code>false</code> otherwise
	 * @param isWarning_ <code>true</code> node is mark with warning, <code>false</code> otherwise
	 */
	public NodeState(boolean isSelected_, boolean hasChildren_, boolean allChildrenSelected_, boolean isWarning_) {
		isSelected = isSelected_;
		hasChildren = hasChildren_;
		allChildrenSelected = allChildrenSelected_;
		isWarning = isWarning_;
	}

	
	//Getters and setters
	public boolean isSelected() {
		return isSelected;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public boolean isAllChildrenSelected() {
		return allChildrenSelected;
	}

	public boolean isWarning() {
		return isWarning;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public void setAllChildrenSelected(boolean allChildrenSelected) {
		this.allChildrenSelected = allChildrenSelected;
	}

	public void setWarning(boolean isWarning) {
		this.isWarning = isWarning;
	}



	

}
