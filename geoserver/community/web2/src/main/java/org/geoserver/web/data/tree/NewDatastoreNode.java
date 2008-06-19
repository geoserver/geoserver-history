package org.geoserver.web.data.tree;

import java.util.Collections;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.geoserver.catalog.WorkspaceInfo;

public class NewDatastoreNode extends AbstgractPlaceholderNode {

    public NewDatastoreNode(String name, AbstractCatalogNode parent) {
        super(name, parent);
    }

    

    @Override
    protected WorkspaceInfo getModel() {
        return getCatalog().getWorkspace(name);
    }

    @Override
    protected String getNodeLabel() {
        return "Add new datastore";
    }

}
