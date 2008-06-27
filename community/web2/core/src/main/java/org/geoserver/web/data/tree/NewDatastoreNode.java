/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import org.geoserver.catalog.WorkspaceInfo;

public class NewDatastoreNode extends PlaceholderNode {

    public NewDatastoreNode(String name, CatalogNode parent) {
        super(name, parent);
    }

    

    @Override
    protected WorkspaceInfo getModel() {
        return getCatalog().getWorkspace(name);
    }

    @Override
    public String getNodeLabel() {
        return "Add Data";
    }

}
