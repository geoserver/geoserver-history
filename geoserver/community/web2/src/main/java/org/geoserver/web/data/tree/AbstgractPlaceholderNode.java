package org.geoserver.web.data.tree;

import java.util.Collections;
import java.util.List;

import javax.swing.tree.TreeNode;

public abstract class AbstgractPlaceholderNode extends AbstractCatalogNode {

    public AbstgractPlaceholderNode(String id, AbstractCatalogNode parent) {
        super(id, parent);
    }

    // override equals and hash code so that they don't clash with the ones in
    // DataStoreInfo node
    public boolean equals(Object obj) {
        return this == obj;
    }

    public int hashCode() {
        return 17 + 3 * getModel().hashCode();
    }

    @Override
    protected List<TreeNode> buildChildNodes() {
        return Collections.emptyList();
    }

}
