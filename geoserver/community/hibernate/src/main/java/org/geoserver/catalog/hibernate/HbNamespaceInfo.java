package org.geoserver.catalog.hibernate;

import org.geoserver.catalog.impl.NamespaceInfoImpl;

public class HbNamespaceInfo extends NamespaceInfoImpl {
    private boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
