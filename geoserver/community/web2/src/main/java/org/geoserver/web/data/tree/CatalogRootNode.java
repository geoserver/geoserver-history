/**
 * 
 */
package org.geoserver.web.data.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.geoserver.catalog.WorkspaceInfo;

class CatalogRootNode extends CatalogNode {

    public CatalogRootNode() {
        super("", null);
    }

    protected List<TreeNode> buildChildNodes() {
        List<WorkspaceInfo> workspaces = getCatalog().getWorkspaces();
        List<TreeNode> childNodes = new ArrayList<TreeNode>();
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