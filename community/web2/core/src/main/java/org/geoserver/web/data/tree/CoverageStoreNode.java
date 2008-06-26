/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
/**
 * 
 */
package org.geoserver.web.data.tree;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;

class CoverageStoreNode extends CatalogNode {

    public CoverageStoreNode(String id, CatalogNode parent) {
        super(id, parent);
    }

    protected List<CatalogNode> buildChildNodes() {
        List<CatalogNode> childNodes = new ArrayList<CatalogNode>();
        List<CoverageInfo> coverages = getCatalog().getCoveragesByStore(
                getModel());
        
        // STRONG assumption here: each coverage store has just one possible
        // coverage inside with the same name as the store. Works for now
        // but will have to be replaced with code similar to DataStoreNode
        // when we'll have a real CoverageStore
        if(coverages.size() == 1) {
            childNodes.add(new ResourceNode(name, coverages.get(0).getName(), this,
                    CoverageInfo.class));
        } else {
            childNodes.add(new UnconfiguredResourcesNode(name, this, CoverageStoreInfo.class));
        }
        return childNodes;

    }

    @Override
    protected CoverageStoreInfo getModel() {
        return getCatalog().getCoverageStoreByName(name);
    }

    @Override
    protected String getNodeLabel() {
        return getModel().getName();
    }
}