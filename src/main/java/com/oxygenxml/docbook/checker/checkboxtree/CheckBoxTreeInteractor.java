package com.oxygenxml.docbook.checker.checkboxtree;

import java.util.HashMap;

import javax.swing.tree.TreePath;

/**
 * Interactor with checkBoxTree
 * @author Cosmin Duna
 *
 */
public interface CheckBoxTreeInteractor {

	/**
	 * Get the map that contains the state of every node.
	 * @return The map.
	 */
	 HashMap<TreePath, NodeState> getNodeCheckingState();
}
