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

import org.geoserver.catalog.WorkspaceInfo;

class CatalogRootNode extends AbstractCatalogNode {

    public CatalogRootNode() {
        super("", null);
    }

    protected List<AbstractCatalogNode> buildChildNodes() {
        List<WorkspaceInfo> workspaces = getCatalog().getWorkspaces();
        List<AbstractCatalogNode> childNodes = new ArrayList<AbstractCatalogNode>();
        for (WorkspaceInfo ws : workspaces) {
            childNodes.add(new WorkspaceNode(ws.getName(), this));
        }
        return childNodes;

    }

    @Override
    protected String getNodeLabel() {
        return "Data";
    }

    @Override
    protected Object getModel() {
        return getCatalog().getClass();
    }

}