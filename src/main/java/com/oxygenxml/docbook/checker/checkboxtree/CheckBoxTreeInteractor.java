package com.oxygenxml.docbook.checker.checkboxtree;

import java.util.Map;

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
	 Map<TreePath, NodeState> getNodeCheckingState();
}
