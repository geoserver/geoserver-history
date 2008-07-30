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
import org.geoserver.web.util.WebUtils;
import org.geotools.data.DataStore;

@SuppressWarnings("serial")
public class UnconfiguredResourcesNode extends PlaceholderNode {

    Class<? extends StoreInfo> storeType;

    public UnconfiguredResourcesNode(String name, CatalogNode parent, Class<? extends StoreInfo> storeType) {
        super(name, parent);
        if(storeType == null)
            throw new NullPointerException("StoreType argument cannot be null");
        this.storeType = storeType;
    }

    @Override
    protected StoreInfo getModel() {
        return getCatalog().getStoreByName(name, storeType);
    }

    @Override
    public String getNodeLabel() {
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
            return WebUtils.localize("unconfiguredLayersCount", null, unconfiguredLayers);

        } else {
            return WebUtils.localize("unconfiguredLayers", null);
        }
    }
}
