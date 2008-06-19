package org.geoserver.web.data.tree;

import org.geoserver.catalog.DataStoreInfo;

public class UnconfiguredFeatureTypesNode extends PlaceholderNode {

    public UnconfiguredFeatureTypesNode(String id, CatalogNode parent) {
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
