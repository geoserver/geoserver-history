package org.geoserver.catalog.hibernate;

import org.geoserver.catalog.impl.WorkspaceInfoImpl;

public class HbWorkspaceInfo extends WorkspaceInfoImpl {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6850342434147452503L;
	private boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

}
