/* 
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.hibernate.beans;


import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.impl.CoverageStoreInfoImpl;
import org.geoserver.hibernate.Hibernable;

/**
 * Mainly copied from CoverageStoreInfoImpl.
 * <BR>
 * Hibernate needs a void constructor and getters/setters for all the persisting
 * attributes. GS original implementations did not provide all of the
 * needed accessors.
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class CoverageStoreInfoImplHb
        extends CoverageStoreInfoImpl
        implements Hibernable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6033145669780453836L;

	public CoverageStoreInfoImplHb(Catalog catalog, String id) {
        super(catalog, id);
    }

    public CoverageStoreInfoImplHb(Catalog catalog) {
        super(catalog);
    }

    public CoverageStoreInfoImplHb() {
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName())
                    .append('[')
                    .append(getWorkspace() != null ? getWorkspace().getName() : "--").append(':')
                    .append(name)
                    .append(']')
                .toString();
    }

}
