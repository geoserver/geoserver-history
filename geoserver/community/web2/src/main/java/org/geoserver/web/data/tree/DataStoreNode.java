/**
 * 
 */
package org.geoserver.web.data.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.tree.TreeNode;

import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;

class DataStoreNode extends AbstractCatalogNode {
    
    boolean unconfiguredChildrenVisible;

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
        if(!unconfiguredChildrenVisible) {      
            childNodes.add(new UnconfiguredFeatureTypesNode(name, this));
        } else {
            try {
                Set<String> typeNames = new HashSet<String>(Arrays.asList(getModel().getDataStore(null).getTypeNames()));
                for (FeatureTypeInfo type : types) {
                    typeNames.remove(type.getName());
                }
                for (String typeName : typeNames) {
                    childNodes.add(new UnconfiguredFeatureTypeNode(name, typeName, this));
                }
            } catch(IOException e) {
                LOGGER.log(Level.SEVERE, "Problem occurred while computing unconfigured elements");
            }
        }
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

    public void setUnconfiguredChildrenVisible(boolean unconfiguredChildren) {
        this.unconfiguredChildrenVisible = unconfiguredChildren;
        this.childNodes = null;
    }
}