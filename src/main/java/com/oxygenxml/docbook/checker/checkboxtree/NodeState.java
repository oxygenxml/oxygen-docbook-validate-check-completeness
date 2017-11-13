package com.oxygenxml.docbook.checker.checkboxtree;

/**
 * The state of a node.
 * It totally replaces the "selection" mechanism of the JTree
 * @author Cosmin Duna
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
	 * @param isSelected <code>true</code> node is selected, <code>false</code> otherwise
	 * @param hasChildren <code>true</code> node has children, <code>false</code> otherwise
	 * @param allChildrenSelected <code>true</code> all children of node are selected, <code>false</code> otherwise
	 * @param isWarning <code>true</code> node is mark with warning, <code>false</code> otherwise
	 */
	public NodeState(boolean isSelected, boolean hasChildren, boolean allChildrenSelected, boolean isWarning) {
		this.isSelected = isSelected;
		this.hasChildren = hasChildren;
		this.allChildrenSelected = allChildrenSelected;
		this.isWarning = isWarning;
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
