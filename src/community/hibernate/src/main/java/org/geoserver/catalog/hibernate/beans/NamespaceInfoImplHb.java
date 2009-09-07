package org.geoserver.catalog.hibernate.beans;

import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.hibernate.Hibernable;

public class NamespaceInfoImplHb 
        extends NamespaceInfoImpl
        implements Hibernable {

    protected boolean _default;

    public NamespaceInfoImplHb() {
    }

    public boolean isDefault() {
        return _default;
    }

    public void setDefault(boolean _default) {
        this._default = _default;
    }

}
