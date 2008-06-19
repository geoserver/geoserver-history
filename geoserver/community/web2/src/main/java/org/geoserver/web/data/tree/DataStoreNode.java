/**
 * 
 */
package org.geoserver.web.data.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
//        try {
//           
//            if(getModel().getDataStore(null).getTypeNames().length > childNodes.size())
//               childNodes.add(new NewDatastoreNode(name, this)); 
//        } catch(Exception e) {
//            LOGGER.log(Level.SEVERE, "Could not aquire type names from data store", e);
//        }
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