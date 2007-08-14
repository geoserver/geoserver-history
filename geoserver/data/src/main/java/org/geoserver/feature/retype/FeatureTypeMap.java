/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.feature.retype;

import org.geotools.feature.FeatureType;

/**
 * A support class containing the old feature type name, the new one, the old
 * feature type, and the new one
 * 
 * @author Andrea Aime
 */
class FeatureTypeMap {
    String originalName;

    String name;

    FeatureType originalFeatureType;

    FeatureType featureType;

    public FeatureTypeMap(String originalName, String name) {
        this.originalName = originalName;
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getName() {
        return name;
    }

    public FeatureType getOriginalFeatureType() {
        return originalFeatureType;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public boolean isUnchanged() {
        return originalName.equals(name);
    }

    public void setFeatureTypes(FeatureType original, FeatureType transformed) {
        this.originalFeatureType = original;
        this.featureType = transformed;
    }

}
