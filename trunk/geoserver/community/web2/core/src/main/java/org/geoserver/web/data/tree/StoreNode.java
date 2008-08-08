package org.geoserver.web.data.tree;

import java.util.List;

import org.geoserver.catalog.StoreInfo;

/**
 * Abstract node representing a store with multiple, eventually non fully
 * configured children.
 * 
 * @author Andrea Aime - TOPP
 * 
 */
@SuppressWarnings("serial")
abstract class StoreNode extends CatalogNode {

    boolean unconfiguredChildrenVisible;

    public StoreNode(String id, CatalogNode parent) {
        super(id, parent);
    }

    @Override
    protected abstract StoreInfo getModel();

    @Override
    public String getNodeLabel() {
        return getModel().getName();
    }

    /**
     * Builds a list containing all the unconfigured resources under this store
     * 
     * @return
     */
    protected abstract List<CatalogNode> buildUnconfiguredChildren();

    /**
     * Shows/hides unconfigured children
     * 
     * @param unconfiguredChildren
     */
    public void setUnconfiguredChildrenVisible(boolean unconfiguredChildren) {
        this.unconfiguredChildrenVisible = unconfiguredChildren;
        if (childNodes != null) {
            if (childNodes.get(childNodes.size() - 1) instanceof UnconfiguredResourcesNode)
                childNodes.remove(childNodes.size() - 1);
            childNodes.addAll(buildUnconfiguredChildren());
            checkPartialSelection();
        }
    }

}
