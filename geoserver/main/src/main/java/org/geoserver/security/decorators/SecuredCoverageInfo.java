package org.geoserver.security.decorators;

import java.io.IOException;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.security.SecureCatalogImpl;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.ProgressListener;

public class SecuredCoverageInfo extends DecoratingCoverageInfo {

    WrapperPolicy policy;

    public SecuredCoverageInfo(CoverageInfo delegate, WrapperPolicy policy) {
        super(delegate);
        this.policy = policy;
    }

    @Override
    public GridCoverage getGridCoverage(ProgressListener listener, Hints hints)
            throws IOException {
        if(policy == WrapperPolicy.METADATA) 
            throw SecureCatalogImpl.unauthorizedAccess(this.getName());
        return super.getGridCoverage(listener, hints);
    }

    @Override
    public GridCoverage getGridCoverage(ProgressListener listener,
            ReferencedEnvelope envelope, Hints hints) throws IOException {
        if(policy == WrapperPolicy.METADATA) 
            throw SecureCatalogImpl.unauthorizedAccess(this.getName());
        return super.getGridCoverage(listener, envelope, hints);
    }

    @Override
    public CoverageAccess getCoverageAccess(ProgressListener listener,
            Hints hints) throws IOException {
        if(policy == WrapperPolicy.METADATA)
            throw SecureCatalogImpl.unauthorizedAccess(this.getName());
        return super.getCoverageAccess(listener, hints);
    }

    @Override
    public CoverageStoreInfo getStore() {
        return new SecuredCoverageStoreInfo(super.getStore(), policy);
    }

}
