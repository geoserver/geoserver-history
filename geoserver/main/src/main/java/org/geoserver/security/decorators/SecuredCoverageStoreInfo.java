/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.security.SecureCatalogImpl;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geotools.coverage.grid.io.AbstractGridFormat;

public class SecuredCoverageStoreInfo extends DecoratingCoverageStoreInfo {
    WrapperPolicy policy;

    public SecuredCoverageStoreInfo(CoverageStoreInfo delegate, WrapperPolicy policy) {
        super(delegate);
        this.policy = policy;
    }

    @Override
    public AbstractGridFormat getFormat() {
        if(policy == WrapperPolicy.METADATA)
            throw SecureCatalogImpl.unauthorizedAccess(getName());
        return super.getFormat();
    }

}
