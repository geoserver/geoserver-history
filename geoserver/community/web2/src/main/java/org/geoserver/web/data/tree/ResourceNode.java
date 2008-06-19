/**
 * 
 */
package org.geoserver.web.data.tree;

import java.util.Collections;
import java.util.List;

import org.geoserver.catalog.ResourceInfo;

class ResourceNode extends CatalogNode {

    private Class clazz;

    public ResourceNode(String id, CatalogNode parent, Class clazz) {
        super(id, parent);
        this.clazz = clazz;
    }

    protected List buildChildNodes() {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected ResourceInfo getModel() {
        return getCatalog().getResourceByName(name, clazz);
    }

    @Override
    protected String getNodeLabel() {
        return getModel().getName();
    }
}