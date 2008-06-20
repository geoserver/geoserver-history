package org.geoserver.web.data.tree;

import org.geoserver.catalog.WorkspaceInfo;

public class NewDatastoreNode extends AbstractPlaceholderNode {

    public NewDatastoreNode(String name, AbstractCatalogNode parent) {
        super(name, parent);
    }

    

    @Override
    protected WorkspaceInfo getModel() {
        return getCatalog().getWorkspace(name);
    }

    @Override
    protected String getNodeLabel() {
        return "Add Data";
    }

}
