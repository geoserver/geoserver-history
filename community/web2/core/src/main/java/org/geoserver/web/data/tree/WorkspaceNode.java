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
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;

class WorkspaceNode extends CatalogNode {

    public WorkspaceNode(String id, CatalogNode parent) {
        super(id, parent);
    }

    protected List<CatalogNode> buildChildNodes() {
        List<StoreInfo> stores = getCatalog().getStoresByWorkspace(
                ((WorkspaceInfo) getModel()), StoreInfo.class);
        List<CatalogNode> childNodes = new ArrayList<CatalogNode>();
        for (StoreInfo store : stores) {
            // hack here: if the node is a datastore that has just one
            // possible child we're replacing it with its child, that we have to
            // reparent to this node as well
            if (store instanceof DataStoreInfo) {
                DataStoreNode dsn = new DataStoreNode(store.getName(), this);
                DataStoreInfo ds = ((DataStoreInfo) store);
                try {
                    if (ds.getDataStore(null).getTypeNames().length == 1
                            && getCatalog().getFeatureTypesByStore(ds).size() == 1)
                        childNodes.add(new ResourceNode(dsn.name, getCatalog()
                                .getFeatureTypesByStore(ds).get(0).getName(),
                                this, FeatureTypeInfo.class));
                    else
                        childNodes.add(dsn);
                } catch (Exception e) {
                    childNodes.add(dsn);
                }
            } else {
                CoverageStoreNode csn = new CoverageStoreNode(store.getName(),
                        this);
                if (catalog.getCoveragesByStore(csn.getModel()).size() != 1)
                    childNodes.add(csn);
                else
                    childNodes.add(new ResourceNode(csn.name, getCatalog()
                            .getCoveragesByStore((CoverageStoreInfo) store)
                            .get(0).getName(), this, CoverageInfo.class));
            }

        }
        childNodes.add(new NewDatastoreNode(name, this));
        return childNodes;

    }

    @Override
    protected WorkspaceInfo getModel() {
        return getCatalog().getWorkspace(name);
    }

    @Override
    protected String getNodeLabel() {
        return getModel().getName();
    }
}