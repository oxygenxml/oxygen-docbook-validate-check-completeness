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
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.oxygenxml.profiling.ProfilingConditionsInformations;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

public class JCheckBoxTree extends ro.sync.exml.workspace.api.standalone.ui.Tree {

	private final String root = "Conditions";

	HashMap<TreePath, CheckedNode> nodesCheckingState;
	HashSet<TreePath> checkedPaths = new HashSet<TreePath>();

	// Defining a new event type for the checking mechanism and preparing
	// event-handling mechanism
	protected EventListenerList listenerList = new EventListenerList();

	public class CheckChangeEvent extends EventObject {

		public CheckChangeEvent(Object source) {
			super(source);
		}
	}

	public interface CheckChangeEventListener extends EventListener {
		public void checkStateChanged(CheckChangeEvent event);
	}

	public void addCheckChangeEventListener(CheckChangeEventListener listener) {
		listenerList.add(CheckChangeEventListener.class, listener);
	}

	public void removeCheckChangeEventListener(CheckChangeEventListener listener) {
		listenerList.remove(CheckChangeEventListener.class, listener);
	}

	void fireCheckChangeEvent(CheckChangeEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] == CheckChangeEventListener.class) {
				((CheckChangeEventListener) listeners[i + 1]).checkStateChanged(evt);
			}
		}
	}
	
//Overriding cell renderer by a class that ignores the original "selection"
	// mechanism
	// It decides how to show the nodes due to the checking-mechanism
	private class CheckBoxCellRenderer extends JPanel implements TreeCellRenderer {
		JCheckBox checkBox;

		public CheckBoxCellRenderer() {
			super();
			this.setLayout(new BorderLayout());
			checkBox = new JCheckBox();
			add(checkBox, BorderLayout.CENTER);
			setOpaque(false);
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
			TreePath tp = model.getPath(value);

			CheckedNode cn = nodesCheckingState.get(tp);
			if (cn != null) {
				checkBox.setSelected(cn.isSelected);
				if (value instanceof ConditionValue) {
					checkBox.setText(((ConditionValue) value).getValue());
				} else {
					checkBox.setText(String.valueOf(value));
				}
				if (cn.isWarning) {
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
	
	

	@Override
	public void setModel(TreeModel newModel) {
		super.setModel(newModel);
		resetCheckingState();
	}

	public void setModel(LinkedHashMap<String, LinkedHashSet<String>> result) {
		((CheckBoxTreeModel) getModel()).setConditionsMapping(MapConvertor.convert(result));
		resetCheckingState();
	}

	// Method that returns only the checked paths (totally ignores original
	// "selection" mechanism)
	public TreePath[] getCheckedPaths() {
		return checkedPaths.toArray(new TreePath[checkedPaths.size()]);
	}

	// Method that returns only the leaf checked paths  (totally ignores original
	// "selection" mechanism)
	public Set<TreePath> getCheckedLeafPaths() {
		Set<TreePath> toReturn = new HashSet<TreePath>(); 
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
	
		Iterator<TreePath> iter = checkedPaths.iterator();
		while(iter.hasNext()){
			TreePath tp = iter.next();
			Object node = (Object) tp.getLastPathComponent();
			if(model.getChildCount(node) == 0){
				toReturn.add(new TreePath(new Object[]{tp.getPath()[0].toString(), 
						tp.getPath()[1].toString(), ((ConditionValue)tp.getPath()[2]).getValue() }));
			}
		}
		
		return toReturn;
	}
	

	private void resetCheckingState() {
		nodesCheckingState = new HashMap<TreePath, CheckedNode>();
		checkedPaths = new HashSet<TreePath>();
		Object node = getModel().getRoot();
		if (node == null) {
			return;
		}

		addSubtreeToCheckingStateTracking(node);
	}

	// Creating data structure of the current model for the checking mechanism
	private void addSubtreeToCheckingStateTracking(Object node) {
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
		if (model != null) {

			TreePath tp = model.getPath(node);
			int cnt = model.getChildCount(node);
			CheckedNode cn = new CheckedNode(false, cnt > 0, false, false);
			nodesCheckingState.put(tp, cn);
			for (int i = 0; i < cnt; i++) {
				addSubtreeToCheckingStateTracking(tp.pathByAddingChild(model.getChild(node, i)).getLastPathComponent());
			}
		}
	}


	

	public JCheckBoxTree() {
		super(new CheckBoxTreeModel());
		// Disabling toggling by double-click
		this.setToggleClickCount(0);
		// Overriding cell renderer
		CheckBoxCellRenderer cellRenderer = new CheckBoxCellRenderer();
		this.setCellRenderer(cellRenderer);

		// Overriding selection model by an empty one
		DefaultTreeSelectionModel dtsm = new DefaultTreeSelectionModel() {
			// Totally disabling the selection mechanism
			public void setSelectionPath(TreePath path) {
			}

			public void addSelectionPath(TreePath path) {
			}

			public void removeSelectionPath(TreePath path) {
			}

			public void setSelectionPaths(TreePath[] pPaths) {
			}
		};
		// Calling checking mechanism on mouse click
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				TreePath tp = JCheckBoxTree.this.getPathForLocation(arg0.getX(), arg0.getY());
				if (tp == null) {
					return;
				}
				boolean checkMode = !nodesCheckingState.get(tp).isSelected;
				checkSubTree(tp, checkMode);
				updatePredecessorsWithCheckMode(tp, checkMode);
				// Firing the check change event
				fireCheckChangeEvent(new CheckChangeEvent(new Object()));
				// Repainting tree after the data structures were updated
				JCheckBoxTree.this.repaint();

			}
		});
		this.setSelectionModel(dtsm);
	}

	// When a node is checked/unchecked, updating the states of the predecessors
	protected void updatePredecessorsWithCheckMode(TreePath tp, boolean check) throws NullPointerException {
		TreePath parentPath = tp.getParentPath();
		// If it is the root, stop the recursive calls and return
		if (parentPath == null) {
			return;
		}
		CheckedNode parentCheckedNode = nodesCheckingState.get(parentPath);
		Object parentNode = (Object) parentPath.getLastPathComponent();
		parentCheckedNode.allChildrenSelected = true;
		parentCheckedNode.isSelected = false;
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
		int cnt = model.getChildCount(parentNode);
		for (int i = 0; i < cnt; i++) {
			TreePath childPath = parentPath.pathByAddingChild(model.getChild(parentNode, i));
			CheckedNode childCheckedNode = nodesCheckingState.get(childPath);
			// It is enough that even one subtree is not fully selected
			// to determine that the parent is not fully selected
			if (!childCheckedNode.allChildrenSelected) {
				parentCheckedNode.allChildrenSelected = false;
			}
			// If at least one child is selected, selecting also the parent
			if (childCheckedNode.isSelected) {
				parentCheckedNode.isSelected = true;
			}
		}
		if (parentCheckedNode.isSelected) {
			checkedPaths.add(parentPath);
		} else {
			checkedPaths.remove(parentPath);
		}
		// Go to upper predecessor
		updatePredecessorsWithCheckMode(parentPath, check);
	}

	// Recursively checks/unchecks a subtree
	protected void checkSubTree(TreePath tp, boolean check) throws NullPointerException {
		CheckedNode cn = nodesCheckingState.get(tp);
		cn.isSelected = check;
		Object node = (Object) tp.getLastPathComponent();
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
		int cnt = model.getChildCount(node);
		for (int i = 0; i < cnt; i++) {
			checkSubTree(tp.pathByAddingChild(model.getChild(node, i)), check);
		}
		cn.allChildrenSelected = check;
		if (check) {
			checkedPaths.add(tp);
		} else {
			checkedPaths.remove(tp);
		}
	}

	// Expand all nodes.
	public void expandAllNodes() {
		int size = this.getRowCount();
		for (int i = 0; i < size; ++i) {
			this.expandRow(i);
		}
	}

	/**
	 * Check the given paths in map format, add those paths that aren't in tree and verify if they are defined in preferences.
	 * @param selectedPaths
	 * @param typeOfDocument
	 * @return
	 */
	public boolean checkPathsInTreeAndVerify(LinkedHashMap<String, LinkedHashSet<String>> selectedPaths,
			String typeOfDocument) {

		boolean toReturn = false;

		LinkedHashMap<String, LinkedHashSet<String>> listToCheck = new LinkedHashMap<String, LinkedHashSet<String>>();

		ProfilingConditionsInformations conditionsInformations = new ProfilingConditionsInformationsImpl();

		if (ProfilingConditionsInformations.DOCBOOK.equals(typeOfDocument)) {
			listToCheck = conditionsInformations.getProfileConditions(ProfilingConditionsInformations.DOCBOOK4);
			listToCheck.putAll(conditionsInformations.getProfileConditions(ProfilingConditionsInformations.DOCBOOK5));
		} else if (ProfilingConditionsInformations.DOCBOOK4.equals(typeOfDocument)) {
			listToCheck = conditionsInformations.getProfileConditions(ProfilingConditionsInformations.DOCBOOK5);
		} else if (ProfilingConditionsInformations.DOCBOOK5.equals(typeOfDocument)) {
			listToCheck = conditionsInformations.getProfileConditions(ProfilingConditionsInformations.DOCBOOK4);
		}

		Iterator<String> itKeys = selectedPaths.keySet().iterator();
		while (itKeys.hasNext()) {
			String key = itKeys.next();

			Iterator<String> itValues = selectedPaths.get(key).iterator();
			while (itValues.hasNext()) {
				String value = itValues.next();

				TreePath path = new TreePath(new Object[] { root, key, new ConditionValue(key, value) });
				try {
					// check path in tree
					checkSubTree(path, true);
					updatePredecessorsWithCheckMode(path, true);
					checkedPaths.add(path);

				} catch (NullPointerException e) {
					if (!(listToCheck.containsKey(key) && listToCheck.get(key).contains(value))) {
						addWarningNode(path, key, value);

						toReturn = true;

					}

				}
			}
		}
		return toReturn;
	}

	/**
	 * Add a given path in tree and mark as warning node.
	 * @param path
	 * @param key
	 * @param value
	 */
	private void addWarningNode(TreePath path, String key, String value) {
		// add condition in model
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
		model.addInConditionsMapping(key, value);

		int cnt = model.getChildCount(path);
		CheckedNode cn = new CheckedNode(false, cnt > 0, false, true);

		setWarningOnParents(path);

		nodesCheckingState.put(path, cn);
		checkSubTree(path, true);
		updatePredecessorsWithCheckMode(path, true);
		checkedPaths.add(path);

	}

	/**
	 * Set the model and validate if conditions are defined in preferences.
	 * @param toSet
	 * @return
	 */
	public boolean setModelAndValidateConditions(LinkedHashMap<String, LinkedHashSet<String>> toSet) {
		boolean toReturn = false;
		ProfilingConditionsInformations conditionsInformations = new ProfilingConditionsInformationsImpl();

		LinkedHashMap<String, LinkedHashSet<String>> listWithDefinedConditions = conditionsInformations
				.getProfileConditions(ProfilingConditionsInformations.ALL_DOCBOOKS);

		setModel(toSet);

		Iterator<String> iterKey = toSet.keySet().iterator();
		while (iterKey.hasNext()) {
			String key = iterKey.next();

			if (!listWithDefinedConditions.containsKey(key)) {
				toReturn = true;
				setWarningSubTree(new TreePath(new Object[] { root, key }));
			} else {
				Iterator<String> iterValue = toSet.get(key).iterator();
				while (iterValue.hasNext()) {
					String value = iterValue.next();
					if (!listWithDefinedConditions.get(key).contains(value)) {
						toReturn = true;
						setWarningSubTree(new TreePath(new Object[] { root, key, new ConditionValue(key, value) }));
						setWarningOnParents(new TreePath(new Object[] { root, key, new ConditionValue(key, value) }));
					}
				}
			}
		}

		return toReturn;
	}

	/**
	 * Set warning the given path and the subTree.
	 * @param path
	 */
	private void setWarningSubTree(TreePath path) {
		CheckedNode cn = nodesCheckingState.get(path);
		cn.isWarning = true;

		Object node = (Object) path.getLastPathComponent();
		CheckBoxTreeModel model = ((CheckBoxTreeModel) getModel());
		int cnt = model.getChildCount(path);
		for (int i = 0; i < cnt; i++) {
			setWarningSubTree(path.pathByAddingChild(model.getChild(node, i)));
		}

	}

	/**
	 * Set warning the parents of given path.
	 * @param path
	 */
	private void setWarningOnParents(TreePath path) {

		TreePath parentPath = path.getParentPath();
		// If it is the root, stop the recursive calls and return
		if (parentPath == null) {
			return;
		} else {
			CheckedNode parentCheckedNode = nodesCheckingState.get(parentPath);
			parentCheckedNode.isWarning = true;
			setWarningOnParents(parentPath);
		}
	}
}