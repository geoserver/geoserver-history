/*
 */

package org.geoserver.catalog.hibernate.beans;

import org.geoserver.catalog.AttributeTypeInfo;
import org.geoserver.catalog.impl.AttributeTypeInfoImpl;
import org.geoserver.hibernate.Hibernable;

/**
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class AttributeTypeInfoImplHb 
    extends AttributeTypeInfoImpl
    implements AttributeTypeInfo, Hibernable{

    protected String id;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(String id) {
        this.id = id;
    }

}
