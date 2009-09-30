/* 
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.hibernate.beans;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.impl.CoverageInfoImpl;
import org.geoserver.hibernate.Hibernable;

/**
 * Mainly copied from CoverageInfoImpl. <BR>
 * Hibernate needs a void constructor and getters/setters for all the persisting attributes. GS
 * original implementations did not provide all of the needed accessors.
 * 
 * @author ETj <etj at geo-solutions.it>
 */

public class CoverageInfoImplHb extends CoverageInfoImpl implements Hibernable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2375653788168952215L;

    /** needed by hibernate */
    protected CoverageInfoImplHb() {
    }

    public CoverageInfoImplHb(Catalog catalog) {
        super(catalog);
    }

    public CoverageInfoImplHb(Catalog catalog, String id) {
        super(catalog, id);
    }
}
