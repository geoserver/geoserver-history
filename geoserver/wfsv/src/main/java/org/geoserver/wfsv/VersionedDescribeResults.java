/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv;

import org.vfny.geoserver.global.FeatureTypeInfo;

public class VersionedDescribeResults {
    private FeatureTypeInfo[] featureTypeInfo;

    private boolean versioned;

    public VersionedDescribeResults(FeatureTypeInfo[] featureTypeInfo,
            boolean versioned) {
        this.featureTypeInfo = featureTypeInfo;
        this.versioned = versioned;
    }

    public FeatureTypeInfo[] getFeatureTypeInfo() {
        return featureTypeInfo;
    }

    public boolean isVersioned() {
        return versioned;
    }

}
