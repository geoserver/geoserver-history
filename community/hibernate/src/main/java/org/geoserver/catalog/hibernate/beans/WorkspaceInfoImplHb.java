package org.geoserver.catalog.hibernate.beans;

import org.geoserver.catalog.impl.*;
import org.geoserver.hibernate.Hibernable;

/**
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class WorkspaceInfoImplHb 
        extends WorkspaceInfoImpl
        implements Hibernable {

    protected boolean _default; // todo: check how this property is handled in Main

    public WorkspaceInfoImplHb() {
    }

    public boolean isDefault() {
        return _default;
    }

    public void setDefault(boolean _default) {
        this._default = _default;
    }

}
