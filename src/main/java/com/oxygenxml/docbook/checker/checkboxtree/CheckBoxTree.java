package com.oxygenxml.docbook.checker.checkboxtree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * CheckBoxTree 
 * @author intern4
 *
 */
public class CheckBoxTree extends ro.sync.exml.workspace.api.standalone.ui.Tree {

	/**
	 * The root of the tree.
	 */
	private static final String ROOT = "Conditions";

	/**
	 * Map with state of nodes.
	 */
	private HashMap<TreePath, NodeState> nodesCheckingState;
	
	/**
	 * Set with checkedPaths
	 */
	private HashSet<TreePath> checkedPaths = new HashSet<TreePath>();



	public interface CheckChangeEventListener extends EventListener {
		 void checkStateChanged(EventObject event);
	}

	void fireCheckChangeEvent(EventObject evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] == CheckChangeEventListener.class) {
				((CheckChangeEventListener) listeners[i + 1]).checkStateChanged(evt);
			}
		}
	}
	
	/**
	 * Overriding cell renderer by a class that ignores the original "selection" mechanism
	 *  It decides how to show the nodes due to the checking-mechanism
	 * @author intern4
	 *
	 */
	private class CheckBoxCellRenderer extends JPanel implements TreeCellRenderer {
		/**
		 * Node checkBox
		 */
		JCheckBox checkBox;

		/**
		 * Constructor
		 */
		 CheckBoxCellRenderer() {
			super();
			this.setLayout(new BorderLayout());
			
			checkBox = new JCheckBox();
			//add the checkBox
			add(checkBox, BorderLayout.CENTER);
			setOpaque(false);
		}

		/**
		 * Sets the value of the current tree cell to value. Selected, expanded, leaf, row, and hasFocus will be ignored.
		 * The component will be render according to the NodeState from nodesCheckingState map.
		 * @return the Component that the renderer uses to draw the node.   
		 */
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
		
			//get the model
			CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
			
			//get the treePath
			TreePath tp = model.getPath(value);

			// get the checkedNode state
			NodeState nodeState = nodesCheckingState.get(tp);
		
			if (nodeState != null) {
				//set the state of checkBox
				checkBox.setSelected(nodeState.isSelected());
				
				//set the text of checkBox
				if (value instanceof LeafNode) {
					checkBox.setText(((LeafNode) value).getValue());
				} else {
					checkBox.setText(String.valueOf(value));
				}
				
				//mark checkBox with warning
				if (nodeState.isWarning()) {
					String text = checkBox.getText();
					checkBox.setText("<HTML>" + text + "<font color=\"orange\">*</font></HTML>");
				} else {
					String text = checkBox.getText();
					if (text.contains("<font color=\"orange\">*")) {
						checkBox.setText(text.substring(text.indexOf("<HTML>") + 6, text.indexOf("<font")));
					}
				}
			}
			return this;
		}
	}
	
	
	/**
	 * Set the model of the Tree.
	 */
	@Override
	public void setModel(TreeModel newModel) {
		super.setModel(newModel);
		resetCheckingState();
	}

	/**
	 * Set the model of the Tree.
	 * @param result The list that provide the data.
	 */
	public void setModel(Map<String, LinkedHashSet<String>> result) {
		((CheckBoxTreeModel) getModel()).setConditionsMapping(ConditionValueUtil.convert(result));
		resetCheckingState();
	}

	
	/**
	 * Get only the leaf checked paths.
	 * 
	 * @return return a LinkedHashMap with checked leaf paths
	 */
	public LinkedHashMap<String, LinkedHashSet<String>> getCheckedLeafPaths() {
		// map to return
		LinkedHashMap<String, LinkedHashSet<String>> toReturn = new LinkedHashMap<String, LinkedHashSet<String>>();

		//get the model
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());

		//iterate over checked paths
		Iterator<TreePath> iter = checkedPaths.iterator();
		while (iter.hasNext()) {
			TreePath tp = iter.next();
			Object node = tp.getLastPathComponent();
			
			if (model.getChildCount(node) == 0) {
				//it's a leaf node
				String key = tp.getPath()[1].toString();
				String value = ((LeafNode) tp.getPath()[2]).getValue();

				if (toReturn.containsKey(key)) {
					toReturn.get(key).add(value);
				} else {
					LinkedHashSet<String> setValue = new LinkedHashSet<String>();
					setValue.add(value);
					toReturn.put(key, setValue);
				}

			}
		}
		return toReturn;
	}

	/**
	 * Reset the checking state. 
	 */
	private void resetCheckingState() {
		//add a new map 
		nodesCheckingState = new HashMap<TreePath, NodeState>();
		//add a new set
		checkedPaths = new HashSet<TreePath>();
		
		Object node = getModel().getRoot();
		if (node == null) {
			return;
		}
		
		//Creating data structure of the current model
		addSubtreeToCheckingStateTracking(node);
	}

	/**
	 * Creating data structure of the current model for the checking mechanism
	 * @param node The root node.
	 */
	private void addSubtreeToCheckingStateTracking(Object node) {
		//get the model
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());

		if (model != null) {
			// get the treePath for this node
			TreePath nodeTreePath = model.getPath(node);
			
			// get the number of child
			int childCnt = model.getChildCount(node);
			
			//create a checkedNode state for this node.
			NodeState checkedNode = new NodeState(false, childCnt > 0, false, false);
			
			//put path of node  and checkedNode in map
			nodesCheckingState.put(nodeTreePath, checkedNode);
			
			//call this method for every child
			for (int i = 0; i < childCnt; i++) {
				addSubtreeToCheckingStateTracking(nodeTreePath.pathByAddingChild(model.getChild(node, i)).getLastPathComponent());
			}
		}
	}


	
/**
 * Constructor
 */
	public CheckBoxTree() {
		super(new CheckBoxTreeModel());
		
		// Disabling toggling by double-click
		this.setToggleClickCount(0);
		
		// Overriding cell renderer
		CheckBoxCellRenderer cellRenderer = new CheckBoxCellRenderer();
		this.setCellRenderer(cellRenderer);

		// Overriding selection model by an empty one
		DefaultTreeSelectionModel dtsm = new DefaultTreeSelectionModel() {
			// Totally disabling the selection mechanism
			@Override
			public void setSelectionPath(TreePath path) {
			}

			@Override
			public void addSelectionPath(TreePath path) {
			}

			@Override
			public void removeSelectionPath(TreePath path) {
			}

			@Override
			public void setSelectionPaths(TreePath[] pPaths) {
			}
		};
		
		// Calling checking mechanism on mouse click
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				TreePath tp = CheckBoxTree.this.getPathForLocation(arg0.getX(), arg0.getY());
				if (tp == null) {
					return;
				}
				boolean checkMode = !nodesCheckingState.get(tp).isSelected();
				checkSubTree(tp, checkMode);
				updatePredecessorsWithCheckMode(tp, checkMode);
				// Firing the check change event
				fireCheckChangeEvent(new EventObject(new Object()));
				// Repainting tree after the data structures were updated
				CheckBoxTree.this.repaint();

			}
		});
		this.setSelectionModel(dtsm);
	}

	/**
	 *  When a node is checked/unchecked, updating the states of the predecessors
	 * @param tp The treePath of the node.
	 * @param check <code>true</code> if node was checked, <code>false</code> if node was unchecked.
	 */
	protected void updatePredecessorsWithCheckMode(TreePath tp, boolean check) {
		//get the parentPath
		TreePath parentPath = tp.getParentPath();

		// If it is the root, stop the recursive calls and return
		if (parentPath == null) {
			return;
		}
		
		NodeState parentNodeState = nodesCheckingState.get(parentPath);
		Object parentNode =  parentPath.getLastPathComponent();
		parentNodeState.setAllChildrenSelected(true);
		parentNodeState.setSelected(false);
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
		
		int cnt = model.getChildCount(parentNode);
		for (int i = 0; i < cnt; i++) {
			
			TreePath childPath = parentPath.pathByAddingChild(model.getChild(parentNode, i));
			
			NodeState childCheckedNode = nodesCheckingState.get(childPath);
			// It is enough that even one subtree is not fully selected
			// to determine that the parent is not fully selected
			if (!childCheckedNode.isAllChildrenSelected()) {
				parentNodeState.setAllChildrenSelected(false);
			}
			// If at least one child is selected, selecting also the parent
			if (childCheckedNode.isSelected()) {
				parentNodeState.setSelected(true);
			}
		}
		if (parentNodeState.isSelected()) {
			checkedPaths.add(parentPath);
		} else {
			checkedPaths.remove(parentPath);
		}
		// Go to upper predecessor
		updatePredecessorsWithCheckMode(parentPath, check);
	}

	/**
	 *  Recursively checks/unchecks a subtree of node.
	 * @param tp The treePath of node.
	 * @param check <code>true</code> to check, <code>false</code> otherwise.
	 * @throws NullPointerException
	 */
	protected void checkSubTree(TreePath tp, boolean check) {
		NodeState cn = nodesCheckingState.get(tp);
		cn.setSelected(check);
		Object node =  tp.getLastPathComponent();
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
		int cnt = model.getChildCount(node);
		for (int i = 0; i < cnt; i++) {
			checkSubTree(tp.pathByAddingChild(model.getChild(node, i)), check);
		}
		cn.setAllChildrenSelected(check);
		if (check) {
			checkedPaths.add(tp);
		} else {
			checkedPaths.remove(tp);
		}
	}

	/**
	 * Expand all nodes
	 */
	public void expandAllNodes() {
		TreePath currentPath;
		Iterator<TreePath> key =  nodesCheckingState.keySet().iterator();
		while(key.hasNext()){
			currentPath = key.next();
			this.expandPath(currentPath);
		}
	}

	/**
	 * Select(check) the paths from given map and add with warning those paths that aren't in tree.
	 * @param pathsToSelect	The paths to be selected
	 * @return <code>true</code> if was found a undefined conditions in given paths, <code>False</code>otherwise.
	 */
	public boolean checkPathsInTreeAndVerify(Map<String, LinkedHashSet<String>> pathsToSelect) {

		//boolean to return
		boolean toReturn = false;

		//iterate over attributes
		Iterator<String> itKeys = pathsToSelect.keySet().iterator();
		while (itKeys.hasNext()) {
			String attribute = itKeys.next();

			//iterate over values of current attribute
			Iterator<String> itValues = pathsToSelect.get(attribute).iterator();
			while (itValues.hasNext()) {
				String value = itValues.next();

				TreePath path = new TreePath(new Object[] { ROOT, attribute, new LeafNode(attribute, value) });
				try {
					// check path in tree
					checkSubTree(path, true);
					updatePredecessorsWithCheckMode(path, true);
					checkedPaths.add(path);

				} catch (NullPointerException e) {
					// if the path isn't in tree, will be catch this exception

					// add a warning node
					addWarningNode(path, attribute, value);

					// check this path in tree
					checkSubTree(path, true);
					updatePredecessorsWithCheckMode(path, true);
					checkedPaths.add(path);

					toReturn = true;

				}
			}
		}
		return toReturn;
	}

	

	/**
	 * Set the model and mark path with warning if its conditions aren't found in given defined conditions.
	 * @param toSet Model to set.
	 * @param definedConditions  The defined conditions. Used for find paths with undefined conditions. 
	 * @return <code>true</code> if was found a undefined conditions, <code>false</code> otherwise
	 */
	public boolean setModelAndValidateConditions(Map<String, LinkedHashSet<String>> toSet, LinkedHashMap<String, LinkedHashSet<String>> definedConditions) {
		boolean toReturn = false;

		//set the model
		setModel(toSet);

		//iterate over set conditions
		Iterator<String> iterKey = toSet.keySet().iterator();
		while (iterKey.hasNext()) {
			String key = iterKey.next();

				Iterator<String> iterValue = toSet.get(key).iterator();
				while (iterValue.hasNext()) {
					String value = iterValue.next();
					if (!definedConditions.get(key).contains(value)) {
						// was found a undefined condition
						toReturn = true;
						
						//mark this path with warning
						setWarningSubTree(new TreePath(new Object[] { ROOT, key, new LeafNode(key, value) }));
						setWarningOnParents(new TreePath(new Object[] { ROOT, key, new LeafNode(key, value) }));
					}
				}
		}

		return toReturn;
	}
	
	
	/**
	 * Add a node at given path in tree and mark as warning node.
	 * @param path The path.
	 * @param key The key of node.
	 * @param value The value of node.
	 */
	private void addWarningNode(TreePath path, String key, String value) {
		// add condition in model
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
		model.addInConditionsMapping(key, value);

		int cnt = model.getChildCount(path);
		NodeState cn = new NodeState(false, cnt > 0, false, true);

		nodesCheckingState.put(path, cn);
		
		setWarningOnParents(path);

	}
	

	/**
	 * Set warning the given path and the subTree.
	 * @param path The path.
	 */
	private void setWarningSubTree(TreePath path) {
		NodeState cn = nodesCheckingState.get(path);
		cn.setWarning(true);

		Object node = path.getLastPathComponent();
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
		int cnt = model.getChildCount(path);
		for (int i = 0; i < cnt; i++) {
			setWarningSubTree(path.pathByAddingChild(model.getChild(node, i)));
		}

	}

	/**
	 * Set warning the parents of given path.
	 * @param path The path.
	 */
	private void setWarningOnParents(TreePath path) {

		TreePath parentPath = path.getParentPath();
		// If it is the root, stop the recursive calls and return
		if (parentPath == null) {
			return;
		} else {
			NodeState parentCheckedNode = nodesCheckingState.get(parentPath);
			if(parentCheckedNode == null){
				int cnt = ((CheckBoxTreeModel) getModel()).getChildCount(parentPath);
				NodeState cn = new NodeState(false, cnt > 0, false, true);

				nodesCheckingState.put(parentPath, cn);
			}else{
				parentCheckedNode.setWarning(true);
			}
			setWarningOnParents(parentPath);
		}
	}
}