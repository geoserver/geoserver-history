/**
 * 
 */
package org.geoserver.web.data.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;

class DataStoreNode extends AbstractCatalogNode {

    public DataStoreNode(String id, AbstractCatalogNode parent) {
        super(id, parent);
    }

    protected List<TreeNode> buildChildNodes() {
        List<FeatureTypeInfo> types = getCatalog().getFeatureTypesByStore(
                getModel());
        List<TreeNode> childNodes = new ArrayList<TreeNode>();
        for (FeatureTypeInfo type : types) {
            childNodes.add(new ResourceNode(type.getName(), this,
                    FeatureTypeInfo.class));
        }
        childNodes.add(new UnconfiguredFeatureTypesNode(name, this));
        return childNodes;
    }

    @Override
    protected DataStoreInfo getModel() {
        return getCatalog().getDataStoreByName(name);
    }

    @Override
    protected String getNodeLabel() {
        return getModel().getName();
    }
}