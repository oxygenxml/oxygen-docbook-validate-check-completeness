package com.oxygenxml.docbook.checker.checkboxtree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
	 * map with conditions
	 */
	private LinkedHashMap<String, LinkedHashSet<ConditionValue>> conditionsMapping = new LinkedHashMap<String, LinkedHashSet<ConditionValue>>();
	private ArrayList<TreeModelListener> listenerList = new ArrayList<TreeModelListener>();

	public void setConditionsMapping(LinkedHashMap<String, LinkedHashSet<ConditionValue>> conditionsMapping) {
		this.conditionsMapping = conditionsMapping;
		System.out.println("condMap "+ conditionsMapping);

		for (int i = 0; i < listenerList.size(); i++) {
			listenerList.get(i).treeStructureChanged(new TreeModelEvent(this, new TreePath(getRoot())));
		}

	}

	/**
	 * Add a conditions in map.
	 * @param key the attribute
	 * @param value the value
	 */
	public void addInConditionsMapping(String key, String value) {
		if (conditionsMapping.containsKey(key)) {
			conditionsMapping.get(key).add(new ConditionValue(key, value));
		} else {
			LinkedHashSet<ConditionValue> set = new LinkedHashSet<ConditionValue>();
			set.add(new ConditionValue(key, value));
			conditionsMapping.put(key, set);
		}
		for (int i = 0; i < listenerList.size(); i++) {
			listenerList.get(i).treeStructureChanged(new TreeModelEvent(this, new TreePath(getRoot())));
		}

	}

	@Override
	public Object getRoot() {
		return "Conditions";
	}

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
