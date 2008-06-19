/**
 * 
 */
package org.geoserver.web.data.tree;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.tree.TreeNode;

import org.apache.wicket.model.IDetachable;
import org.geoserver.catalog.Catalog;
import org.geoserver.platform.GeoServerExtensions;
import org.geotools.util.logging.Logging;

abstract class CatalogNode implements TreeNode, Serializable, IDetachable {
    
    static final Logger LOGGER = Logging.getLogger(CatalogNode.class);

    String name;

    TreeNode parent;

    transient List<TreeNode> childNodes;
    
    transient Catalog catalog;
    
    public CatalogNode(String id, CatalogNode parent) {
        if (id == null)
            throw new NullPointerException("Id cannot be null");
        this.name = id;
        this.parent = parent;
    }
    
    protected Catalog getCatalog() {
        if(catalog == null) {
            catalog = (Catalog) GeoServerExtensions.bean("catalog2");
        }
        return catalog;
    }

    protected CatalogNode setParent(TreeNode parent) {
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

    public TreeNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return getChildCount() <= 0;
    }

    List<TreeNode> childNodes() {
        if (childNodes == null) {
            synchronized (this) {
                if (childNodes == null) {
                    childNodes = buildChildNodes();
                }
            }
        }

        return childNodes;
    }

    protected abstract List<TreeNode> buildChildNodes();

    protected String getNodeLabel() {
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
        childNodes = null;
        catalog = null;
    }

    protected abstract Object getModel();

}