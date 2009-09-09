/* 
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.hibernate.beans;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.hibernate.Hibernable;

public class StyleInfoImplHb 
        extends StyleInfoImpl
        implements StyleInfo, Hibernable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6463661589509009337L;

	public StyleInfoImplHb(Catalog catalog) {
        super(catalog);
    }

    public StyleInfoImplHb() {
    }
}
