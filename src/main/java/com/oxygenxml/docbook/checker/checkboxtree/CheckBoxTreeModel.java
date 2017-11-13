package com.oxygenxml.docbook.checker.checkboxtree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
/**
 * The model used by JCheckBoxTree.
 * @author Cosmin Duna
 *
 */
public class CheckBoxTreeModel implements TreeModel {

	/**
	 * Map with conditions.
	 */
	private Map<String, LinkedHashSet<LeafNode>> conditionsMapping = new LinkedHashMap<String, LinkedHashSet<LeafNode>>();
	
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
	 * @param conditionsMapping conditions mapping
	 */
	public void setConditionsMapping(Map<String, LinkedHashSet<LeafNode>> conditionsMapping) {
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
		Object toReturn = null;

		if (getRoot().equals(parent)) {
			toReturn = new ArrayList(conditionsMapping.keySet()).get(index);
		} else {
			if (conditionsMapping.containsKey(parent)) {
				// It is the key
				toReturn = new ArrayList(conditionsMapping.get(parent)).get(index);
			}
		}
		return toReturn;
	}

	@Override
	public int getChildCount(Object parent) {
		int toReturn = 0;
		
		if (getRoot().equals(parent)) {
			toReturn = conditionsMapping.keySet().size();
		} else {
			if (conditionsMapping.containsKey(parent)) {
				// It is the key
				toReturn = conditionsMapping.get(parent).size();
			} else {
				// Leaf
			}
		}
		
		return toReturn;
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
		int toReturn = -1;
		int chCount = getChildCount(parent);
		for (int i = 0; i < chCount; i++) {
			Object ch = getChild(parent, i);
			if (child == ch || child.equals(ch)) {
				toReturn = i;
				break;
			}
		}
		return toReturn;
	}

	/**
	 * Get the path of the given node.
	 * @param obj The node.
	 * @return	The path of the node.
	 */
	public TreePath getPath(Object obj) {
		TreePath toReturn = null;
		
		if (getRoot().equals(obj)) {
			toReturn =  new TreePath(obj);
		} else {
			if (conditionsMapping.containsKey(obj)) {
				// It is the key
				toReturn = new TreePath(new Object[] { getRoot(), obj });
			} else {
				// It is a value
				Iterator<String> iter = conditionsMapping.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					if (conditionsMapping.get(key).contains(obj)) {
						toReturn = new TreePath(new Object[] { getRoot(), key, obj });
						break;
					}
				}
			}
		}
		return toReturn;
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
