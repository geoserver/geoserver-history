/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.impl;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geotools.coverage.io.Driver;

/**
 * Default implementation of {@link CoverageStoreInfo}.
 */
public class CoverageStoreInfoImpl extends StoreInfoImpl implements
        CoverageStoreInfo {

    String type;

    String url;
    
    public CoverageStoreInfoImpl() {
        super(null);
    }
    
    public CoverageStoreInfoImpl(Catalog catalog) {
        super(catalog);
    }

    public CoverageStoreInfoImpl(Catalog catalog,String id) {
        super(catalog,id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }
    
    public Driver getDriver() {
        return catalog.getResourcePool().getDriver(this);
    }
}
