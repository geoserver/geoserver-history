/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.StoreInfo;
import org.geotools.data.DataStore;

public class UnconfiguredResourcesNode extends PlaceholderNode {

    Class storeType;

    public UnconfiguredResourcesNode(String name, CatalogNode parent, Class storeType) {
        super(name, parent);
        this.storeType = storeType;
    }

    @Override
    protected StoreInfo getModel() {
        return getCatalog().getStoreByName(name, storeType);
    }

    @Override
    protected String getNodeLabel() {
        int unconfiguredLayers = -1;
        if(DataStoreInfo.class.isAssignableFrom(storeType)) {
            try {
                final DataStore ds = ((DataStoreInfo) getModel()).getDataStore(null);
                Set<String> typeNames = new HashSet<String>(Arrays.asList(ds.getTypeNames()));
                for (FeatureTypeInfo ft : getCatalog().getFeatureTypesByStore(
                        (DataStoreInfo) getModel())) {
                    typeNames.remove(ft.getName());
                }
                
                unconfiguredLayers = typeNames.size();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE,
                        "Error trying to compute unconfigured types");
            }
        } else if(CoverageStoreInfo.class.isAssignableFrom(storeType)) {
            unconfiguredLayers = 1;
        }   
        
        if(unconfiguredLayers != -1) {
            return unconfiguredLayers + " unconfigured layer(s) ...";
        } else {
            return "Show unconfigured layer(s) ...";
        }
    }
}
