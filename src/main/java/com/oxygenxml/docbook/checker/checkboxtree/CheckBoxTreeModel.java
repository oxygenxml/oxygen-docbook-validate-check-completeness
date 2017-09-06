package com.oxygenxml.docbook.checker.checkboxtree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
/**
 * The model used by JCheckBoxTree.
 * @author intern4
 *
 */
public class CheckBoxTreeModel implements TreeModel {

	/**
	 * Map with conditions.
	 */
	private LinkedHashMap<String, LinkedHashSet<LeafNode>> conditionsMapping = new LinkedHashMap<String, LinkedHashSet<LeafNode>>();
	
	/**
	 * List with listeners
	 */
	private ArrayList<TreeModelListener> listenerList = new ArrayList<TreeModelListener>();
	
	/**
	 * The root of the tree.
	 */
	private static final String ROOT = "Conditions";
	
	/**
	 * Setter for conditionsMapping.
	 * @param conditionsMapping
	 */
	public void setConditionsMapping(LinkedHashMap<String, LinkedHashSet<LeafNode>> conditionsMapping) {
		this.conditionsMapping = conditionsMapping;

		for (int i = 0; i < listenerList.size(); i++) {
			listenerList.get(i).treeStructureChanged(new TreeModelEvent(this, new TreePath(getRoot())));
		}

	}

	/**
	 * Add a condition in conditionsMapping.
	 * @param key the attribute
	 * @param value the value
	 */
	public void addInConditionsMapping(String key, String value) {
		if (conditionsMapping.containsKey(key)) {
			conditionsMapping.get(key).add(new LeafNode(key, value));
		} else {
			LinkedHashSet<LeafNode> set = new LinkedHashSet<LeafNode>();
			set.add(new LeafNode(key, value));
			conditionsMapping.put(key, set);
		}
		for (int i = 0; i < listenerList.size(); i++) {
			listenerList.get(i).treeStructureChanged(new TreeModelEvent(this, new TreePath(getRoot())));
		}

	}

	/**
	 * Get the root of the tree.
	 */
	@Override
	public Object getRoot() {
		return ROOT;
	}

	/**
	 * Returns the child of parent at index index in the parent's child array. 
	 * parent must be a node previously obtained from this data source. 
	 * This will return null if index is invalid.
	 */
	@Override
	public Object getChild(Object parent, int index) {
		if (getRoot().equals(parent)) {
			return new ArrayList(conditionsMapping.keySet()).get(index);
		} else {
			if (conditionsMapping.containsKey(parent)) {
				// It is the key
				return new ArrayList(conditionsMapping.get(parent)).get(index);
			} else {
				return null;
			}
		}
	}

	@Override
	public int getChildCount(Object parent) {
		if (getRoot().equals(parent)) {
			return conditionsMapping.keySet().size();
		} else {
			if (conditionsMapping.containsKey(parent)) {
				// It is the key
				return conditionsMapping.get(parent).size();
			} else {
				// Leaf
				return 0;
			}
		}
	}

	@Override
	public boolean isLeaf(Object node) {
		return getChildCount(node) == 0;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		int chCount = getChildCount(parent);
		for (int i = 0; i < chCount; i++) {
			Object ch = getChild(parent, i);
			if (child == ch || child.equals(ch)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Get the path of the given node.
	 * @param obj The node.
	 * @return	The path of the node.
	 */
	public TreePath getPath(Object obj) {

		if (getRoot().equals(obj)) {
			return new TreePath(obj);
		} else {
			if (conditionsMapping.containsKey(obj)) {
				// It is the key
				return new TreePath(new Object[] { getRoot(), obj });
			} else {
				// It is a value
				Iterator<String> iter = conditionsMapping.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					if (conditionsMapping.get(key).contains(obj)) {
						return new TreePath(new Object[] { getRoot(), key, obj });
					}
				}
				return null;
			}
		}
	}

	/**
	 * Adds a listener for the TreeModelEvent posted after the tree changes.
	 *
	 * @see #removeTreeModelListener
	 * @param l
	 *          the listener to add
	 */
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(l);
	}

	/**
	 * Removes a listener previously added with <B>addTreeModelListener()</B>.
	 *
	 * @see #addTreeModelListener
	 * @param l
	 *          the listener to remove
	 */
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(l);
	}
}
