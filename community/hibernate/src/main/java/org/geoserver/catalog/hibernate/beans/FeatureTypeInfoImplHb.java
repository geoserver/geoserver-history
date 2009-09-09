/* 
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.hibernate.beans;


import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.impl.FeatureTypeInfoImpl;
import org.geoserver.hibernate.Hibernable;

/**
 * Mainly copied from FeatureTypeInfoImpl.
 * <BR>
 * Hibernate needs a void constructor and getters/setters for all the persisting
 * attributes. GS original implementations did not provide all of the
 * needed accessors.
 *
 * @author ETj <etj at geo-solutions.it>
 */

public class FeatureTypeInfoImplHb 
        extends FeatureTypeInfoImpl
        implements FeatureTypeInfo, Hibernable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6519421313603985545L;

	protected FeatureTypeInfoImplHb() {
    }
    
    public FeatureTypeInfoImplHb(Catalog catalog) {
        super(catalog);
    }

    public FeatureTypeInfoImplHb(Catalog catalog, String id) {
        super(catalog,id);
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName())
                .append('[')
                .append("id:").append(getId())
                .append(" name:").append(getName())
                .append(" ").append(getStore()!=null? getStore().toString() : " noStore")
                .append(" llbbox:").append(getLatLonBoundingBox())
                .append(" natbbox:").append(getNativeBoundingBox())
                .append(']')
                .toString();
    }


}
