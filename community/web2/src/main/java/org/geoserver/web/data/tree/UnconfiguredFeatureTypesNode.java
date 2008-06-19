package org.geoserver.web.data.tree;

import org.geoserver.catalog.DataStoreInfo;

public class UnconfiguredFeatureTypesNode extends AbstgractPlaceholderNode {

    public UnconfiguredFeatureTypesNode(String id, AbstractCatalogNode parent) {
        super(id, parent);
    }

    @Override
    protected DataStoreInfo getModel() {
        return getCatalog().getDataStoreByName(name);
    }
    
    @Override
    protected String getNodeLabel() {
        int count = 0;
        
        return count + " unconfigured layers";
    }

}
