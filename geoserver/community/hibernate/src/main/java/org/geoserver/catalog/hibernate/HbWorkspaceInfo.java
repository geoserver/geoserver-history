package org.geoserver.catalog.hibernate;

import org.geoserver.catalog.impl.WorkspaceInfoImpl;

public class HbWorkspaceInfo extends WorkspaceInfoImpl {

    private boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

}
