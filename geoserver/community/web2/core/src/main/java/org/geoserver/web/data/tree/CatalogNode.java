/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.tree.TreeNode;

import org.apache.wicket.model.IDetachable;
import org.geoserver.catalog.Catalog;
import org.geoserver.platform.GeoServerExtensions;
import org.geotools.util.logging.Logging;

/**
 * <p>
 * Base class for all model objects backing the data tree contained in
 * {@link DataPage}.
 * <p>
 * This base class provides common functionality such as:
 * <ul>
 * <li>providing most of the methods of TreeNode, requiring subclasses just to
 * implement {@link #buildChildNodes()}</li>
 * <li>detaching the actual Catalog object on detach, avoiding serialization
 * issues</li>
 * <li>grabbing on demand the model and its children</li>
 * <li>sorting the children according to the label and the type</li>
 * <li>managing the selectability and hierarchical selection of the current node and its parents</li>
 * </ul>
 * @author Andrea Aime - TOPP
 * 
 */
abstract class CatalogNode implements TreeNode, Serializable, IDetachable,
        Comparable<CatalogNode> {

    /**
     * Selection state of the node
     * @author Andrea Aime - TOPP
     *
     */
    public enum SelectionState {
        SELECTED, UNSELECTED, PARTIAL
    };

    static final Logger LOGGER = Logging.getLogger(CatalogNode.class);

    String name;

    CatalogNode parent;

    transient List<CatalogNode> childNodes;

    transient Catalog catalog;

    SelectionState selectionState;

    public CatalogNode(String id, CatalogNode parent) {
        if (id == null)
            throw new NullPointerException("Id cannot be null");
        this.name = id;
        this.parent = parent;
    }

    protected Catalog getCatalog() {
        if (catalog == null) {
            catalog = (Catalog) GeoServerExtensions.bean("catalog2");
        }
        return catalog;
    }

    protected CatalogNode setParent(CatalogNode parent) {
        this.parent = parent;
        return this;
    }

    public Enumeration children() {
        final Iterator i = childNodes().iterator();
        return new Enumeration() {

            public boolean hasMoreElements() {
                return i.hasNext();
            }

            public Object nextElement() {
                return i.next();
            }

        };
    }

    public boolean getAllowsChildren() {
        return childNodes().size() > 0;
    }

    public TreeNode getChildAt(int childIndex) {
        return childNodes().get(childIndex);
    }

    public int getChildCount() {
        return childNodes().size();
    }

    public int getIndex(TreeNode node) {
        return childNodes().indexOf(node);
    }

    public CatalogNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return getChildCount() <= 0;
    }

    List<CatalogNode> childNodes() {
        if (childNodes == null) {
            synchronized (this) {
                if (childNodes == null) {
                    childNodes = buildChildNodes();
                    // sort child nodes
                    Collections.sort(childNodes);
                    // manage selection
                    if (selectionState == SelectionState.SELECTED)
                        for (CatalogNode child : childNodes) {
                            child.setSelectionState(SelectionState.SELECTED);
                        }
                    else
                        for (CatalogNode child : childNodes) {
                            child.setSelectionState(SelectionState.UNSELECTED);
                        }
                }
            }
        }

        return childNodes;
    }

    protected abstract List<CatalogNode> buildChildNodes();

    public String getNodeLabel() {
        return getModel().toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CatalogNode)) {
            return false;
        }

        CatalogNode other = (CatalogNode) obj;
        return getModel().equals(other.getModel());
    }

    public int hashCode() {
        return getModel().hashCode();
    }

    public void detach() {
        // childNodes = null;
        catalog = null;
    }

    @Override
    public String toString() {
        return getNodeLabel();
    }

    protected abstract Object getModel();

    public void nextSelectionState() {
        if (selectionState == SelectionState.SELECTED || selectionState == SelectionState.PARTIAL)
            setSelectionState(SelectionState.UNSELECTED);
        else
            setSelectionState(SelectionState.SELECTED);
    }

    public void setSelectionState(SelectionState state) {
        if (isSelectable()) {
            this.selectionState = state;
            if (state != SelectionState.PARTIAL && childNodes != null)
                for (CatalogNode child : childNodes) {
                    child.setSelectionState(state);
                }
        }
    }

    public SelectionState getSelectionState() {
        return selectionState;
    }

    public boolean isSelectable() {
        return true;
    }

    /**
     * Returns all selected nodes (including children)
     * 
     * @return
     */
    public List<CatalogNode> getSelectedNodes() {
        List<CatalogNode> result = new ArrayList<CatalogNode>();
        if (selectionState == SelectionState.SELECTED)
            result.add(this);
        if (childNodes != null)
            for (CatalogNode child : childNodes) {
                result.addAll(child.getSelectedNodes());
            }
        return result;
    }

    /**
     * Updates the partial selection state of node and recurses up to the root,
     * and returns the higher node that got updated during the process. The
     * rules are as follows:
     * <ul>
     * <li>if any of the children is partially selected, this node is partially
     * selected as well</li> <li>if all of the children are unselected, this
     * node is unselected as well</li> <li>if any of the children is selected,
     * this node goes into partial selection (even if all the children are
     * selected, since this root was not explicitly selected... what if the user
     * presses the delete button after the selection?)</li>
     * </ul>
     */
    public CatalogNode checkPartialSelection() {
        // check if we have children
        List<CatalogNode> children = childNodes;
        if (children == null || children.size() == 0)
            return this;

        // scan the children checking if we have any in selected or unselected
        // state
        boolean selected = false;
        boolean unselected = false;
        SelectionState result = null;
        for (CatalogNode child : children) {
            if (!child.isSelectable())
                continue;

            SelectionState childState = child.getSelectionState();
            selected = selected || childState == SelectionState.SELECTED;
            unselected = unselected || childState == SelectionState.UNSELECTED;
            if ((selected && unselected) || childState == SelectionState.PARTIAL) {
                result = SelectionState.PARTIAL;
                break;
            }
        }

        // apply the above rules
        if (result == null && unselected)
            result = selectionState.UNSELECTED;
        if (result == null && selected)
            result = selectionState.PARTIAL;
        if (result != null && result != selectionState) {
            selectionState = result;
        }

        // recurse up (maybe we could avoid this if the selection change of this
        // node did not change...)
        if (parent != null)
            return parent.checkPartialSelection();
        return this;
    }

    public int compareTo(CatalogNode other) {
        if (this instanceof PlaceholderNode && !(other instanceof PlaceholderNode))
            return 1;
        else if (other instanceof PlaceholderNode
                && !(this instanceof PlaceholderNode))
            return -1;
        String label1 = this.getNodeLabel();
        String label2 = other.getNodeLabel();
        return label1.compareTo(label2);

    }

}