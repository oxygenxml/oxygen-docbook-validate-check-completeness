package com.oxygenxml.docbook.checker.checkboxtree;

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


}
