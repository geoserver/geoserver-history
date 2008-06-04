package org.geoserver.security.decorators;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;

public class ReadOnlyLayerInfo extends DecoratingLayerInfo {

    public ReadOnlyLayerInfo(LayerInfo delegate) {
        super(delegate);
    }

    @Override
    public ResourceInfo getResource() {
        ResourceInfo r = super.getResource();
        if (r instanceof FeatureTypeInfo)
            return new ReadOnlyFeatureTypeInfo((FeatureTypeInfo) r);
        else if (r instanceof CoverageInfo)
            return r;
        else
            throw new RuntimeException("Don't know how to make resource of type " + r.getClass());
    }

}
