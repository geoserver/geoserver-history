package org.geoserver.catalog.hibernate.beans;

import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.hibernate.Hibernable;

public class NamespaceInfoImplHb 
        extends NamespaceInfoImpl
        implements Hibernable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2667725101814407281L;
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
