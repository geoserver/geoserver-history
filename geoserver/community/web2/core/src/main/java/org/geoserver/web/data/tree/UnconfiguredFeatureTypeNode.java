/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import org.geoserver.catalog.DataStoreInfo;

public class UnconfiguredFeatureTypeNode extends AbstractPlaceholderNode {
    public UnconfiguredFeatureTypeNode(String storeName, String typeName,
            AbstractCatalogNode parent) {
        super(storeName, parent);
        this.typeName = typeName;
    }

    String typeName;

    @Override
    protected DataStoreInfo getModel() {
        return getCatalog().getDataStore(name);
    }

    @Override
    protected String getNodeLabel() {
        return typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    @Override
    public boolean isSelectable() {
        return true;
    }

}
