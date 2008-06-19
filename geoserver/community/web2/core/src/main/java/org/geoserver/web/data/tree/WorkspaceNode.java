/**
 * 
 */
package org.geoserver.web.data.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;

class WorkspaceNode extends AbstractCatalogNode {

    public WorkspaceNode(String id, AbstractCatalogNode parent) {
        super(id, parent);
    }

    protected List<TreeNode> buildChildNodes() {
        List<StoreInfo> stores = getCatalog().getStoresByWorkspace(
                ((WorkspaceInfo) getModel()), StoreInfo.class);
        List<TreeNode> childNodes = new ArrayList<TreeNode>();
        for (StoreInfo store : stores) {
            // hack here: if the node is a datastore that has just one
            // possible child
            // we're replacing it with its child, that we have to reparent
            // to this node as well
            if (store instanceof DataStoreInfo) {
                DataStoreNode dsn = new DataStoreNode(store.getName(), this);
                DataStoreInfo ds = ((DataStoreInfo) store);
                try {
                    if (ds.getDataStore(null).getTypeNames().length == 1
                            && getCatalog().getFeatureTypesByStore(ds).size() == 1)
                        childNodes.add(new ResourceNode(getCatalog()
                                .getFeatureTypesByStore(ds).get(0).getName(),
                                this, FeatureTypeInfo.class));
                    else
                        childNodes.add(dsn);
                } catch (Exception e) {
                    childNodes.add(dsn);
                }
            } else {
                CoverageStoreNode cs = new CoverageStoreNode(store.getName(),
                        this);
                if (cs.getChildCount() != 1)
                    childNodes.add(cs);
                else
                    childNodes.add(new ResourceNode(getCatalog()
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