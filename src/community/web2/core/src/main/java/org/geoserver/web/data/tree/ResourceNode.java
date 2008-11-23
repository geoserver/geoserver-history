/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
/**
 * 
 */
package org.geoserver.web.data.tree;

import java.util.Collections;
import java.util.List;

import org.geoserver.catalog.ResourceInfo;

@SuppressWarnings("serial")
class ResourceNode extends CatalogNode {

    private Class<? extends ResourceInfo> resourceType;

    private String storeName;

    public ResourceNode(String storeName, String name,
            CatalogNode parent, Class<? extends ResourceInfo> clazz) {
        super(name, parent);
        this.storeName = storeName;
        this.resourceType = clazz;
    }

    protected List<CatalogNode> buildChildNodes() {
        return Collections.emptyList();
    }

    @Override
    protected ResourceInfo getModel() {
        return getCatalog().getResourceByName(name, resourceType);
    }

    @Override
    public String getNodeLabel() {
        return getModel().getName();
    }

    protected Class<? extends ResourceInfo> getResourceType() {
        return resourceType;
    }

    public String getStoreName() {
        return storeName;
    }

}