/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import org.geoserver.catalog.DataStoreInfo;

/**
 * Represents a data resource that's waiting to be configured into GeoServer
 * @author Andrea Aime - TOPP
 */
public class UnconfiguredResourceNode extends PlaceholderNode {
    public UnconfiguredResourceNode(String storeName, String resourceName,
            CatalogNode parent) {
        super(storeName, parent);
        this.resourceName = resourceName;
    }

    String resourceName;

    @Override
    protected DataStoreInfo getModel() {
        return getCatalog().getDataStore(name);
    }

    @Override
    protected String getNodeLabel() {
        return resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResouirceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    @Override
    public boolean isSelectable() {
        return true;
    }

}
