package com.oxygenxml.docbookChecker.checkbox.tree;

//Defining data structure that will enable to fast check-indicate the state
// of each node
// It totally replaces the "selection" mechanism of the JTree
public class CheckedNode {
	boolean isSelected;
	boolean hasChildren;
	boolean allChildrenSelected;
	boolean isWarning;

	public CheckedNode(boolean isSelected_, boolean hasChildren_, boolean allChildrenSelected_, boolean isWarning_) {
		isSelected = isSelected_;
		hasChildren = hasChildren_;
		allChildrenSelected = allChildrenSelected_;
		isWarning = isWarning_;
	}

	@Override
	public String toString() {
		return "CheckedNode [isSelected=" + isSelected + ", hasChildren=" + hasChildren + ", allChildrenSelected="
				+ allChildrenSelected + ", isWarning=" + isWarning + "]";
	}

}
